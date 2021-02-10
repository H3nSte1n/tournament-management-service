package api.v1

import io.ktor.http.*
import io.ktor.routing.*

fun Route.v1() {
    route("/v1") {
        apiDoc()
        tournamentManagement()
    }
}
