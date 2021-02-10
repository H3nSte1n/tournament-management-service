package validation

import schemas.Tournaments

object TournamentValidation {

    fun <T> validateTournamentExist(attribute: String, attributeValue: T): Boolean {
        when (attribute) {
            "id" -> if (Tournaments.TournamentExistById(attributeValue as Int)) return true
            "name" -> if (Tournaments.TournamentExistByName(attributeValue.toString())) return true
        }

        return false
    }

    fun validateInput(input: String): Boolean {
        if (input.isEmpty()) return false
        if (input.isBlank()) return false

        return true
    }
}
