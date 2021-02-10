
package api.v1

import controller.TournamentsController
import data.Tournament
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import withRole

fun Route.tournamentManagement() {
    withRole {
        delete("tournaments/{id}") {
            val tournamentId = call.parameters["id"]
            val removedTournament = TournamentsController.removeTournament(tournamentId!!.toInt())
            call.respond(removedTournament)
        }
        post("tournaments") {
            val receivedTournament = call.receive<Tournament>()
            val addedTournament = TournamentsController.addTournament(receivedTournament)
            call.respond(addedTournament)
            call.request.header("Authorization")
        }
        get("tournaments") {
            val storedTournaments = TournamentsController.getAllTournaments()
            call.respond(storedTournaments)
        }
        get("tournaments/{id}") {
            val tournamentId = call.parameters["id"]
            val storedTournaments = TournamentsController.getTournament(tournamentId!!.toInt())
            call.respond(storedTournaments)
        }
        put("tournaments/{id}") {
            val tournamentId = call.parameters["id"]
            val newTournamentsValues = call.receive<Tournament>()
            val updatedTournament = TournamentsController.updateTournament(tournamentId!!.toInt(), newTournamentsValues)
            call.respond(updatedTournament)
        }
    }
}
