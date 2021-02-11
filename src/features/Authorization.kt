import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import statuspages.AuthenticationException

class RoleBasedAuthorization() {

    class Configuration

    fun interceptPipeline(pipeline: ApplicationCallPipeline, acceptedRoles: Array<out String>) {
        pipeline.insertPhaseAfter(ApplicationCallPipeline.Features, Authentication.ChallengePhase)
        pipeline.insertPhaseAfter(Authentication.ChallengePhase, AuthorizationPhase)

        pipeline.intercept(AuthorizationPhase) {
            val status = khttp.post(
                url = "https://turnierverwaltung-auth.herokuapp.com/api/v1/auth",
                headers = mapOf("Authorization" to call.request.header("Authorization")),
            )
            val role = String(status.content)
            if (status.statusCode == HttpStatusCode.Unauthorized.value) throw AuthenticationException()
            if (!acceptedRoles.contains(role)) throw AuthenticationException()
        }
    }

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Configuration, RoleBasedAuthorization> {
        override val key = AttributeKey<RoleBasedAuthorization>("RoleBasedAuthorization")

        val AuthorizationPhase = PipelinePhase("Authorization")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Configuration.() -> Unit
        ): RoleBasedAuthorization {
            return RoleBasedAuthorization()
        }
    }
}

class AuthorizedRouteSelector(private val description: String) :
    RouteSelector(RouteSelectorEvaluation.qualityConstant) {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant

    override fun toString(): String = "(authorize $description)"
}

fun Route.withRole(vararg selectedRoles: String, build: Route.() -> Unit) = authorizedRoute(acceptedRoles = selectedRoles, build = build)

private fun Route.authorizedRoute(acceptedRoles: Array<out String>, build: Route.() -> Unit): Route {
    val authorizedRoute = createChild(AuthorizedRouteSelector(""))
    application.feature(RoleBasedAuthorization).interceptPipeline(authorizedRoute, acceptedRoles)
    authorizedRoute.build()
    return authorizedRoute
}
