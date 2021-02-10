package controller

import ClassHelper
import data.Tournament
import helper.Controller.isInputValid
import schemas.Tournaments
import schemas.Tournaments.createTournament
import schemas.Tournaments.getTournaments
import statuspages.InvalidTournamentException
import statuspages.ThrowableException
import validation.TournamentValidation.validateTournamentExist

object TournamentsController {
    fun removeTournament(id: Int): Int {
        if (!validateTournamentExist("id", id)) throw InvalidTournamentException()

        return Tournaments.deleteTournament(id)
    }

    fun addTournament(receivedTournament: Tournament): Tournament {
        val inputs = listOf(receivedTournament.name)

        if (!isInputValid(inputs)) throw ThrowableException()
        if (validateTournamentExist("String", receivedTournament.name)) throw InvalidTournamentException()

        return createTournament(receivedTournament)
    }

    fun getAllTournaments(): Collection<Tournament> {
        return getTournaments()
    }

    fun getTournament(TournamentId: Int): Tournament {
        return Tournaments.getTournament(TournamentId)
    }

    fun updateTournament(id: Int, newTournamentValues: Tournament): Tournament {
        var inputs: MutableList<String> = ClassHelper.getAllPropertyValues(newTournamentValues)

        if (!isInputValid(inputs)) throw ThrowableException()
        if (!validateTournamentExist("id", id)) throw InvalidTournamentException()

        return Tournaments.updateTournament(id, newTournamentValues)
    }
}
