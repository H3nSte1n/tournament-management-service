package statuspages
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun StatusPages.Configuration.invalidTournamentStatusPage() {
    exception<InvalidTournamentException> { cause ->
        call.respondText(
            cause.message,
            ContentType.Text.Plain,
            status = HttpStatusCode.BadRequest
        )
    }
}

data class InvalidTournamentException(override val message: String = "Invalid Tournament Exception") : Exception()
