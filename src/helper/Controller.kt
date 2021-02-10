package helper

import validation.TournamentValidation

object Controller {
    fun isInputValid(methods: List<String>): Boolean {
        for (input in methods) {
            if (!TournamentValidation.validateInput(input)) return false
        }

        return true
    }
}
