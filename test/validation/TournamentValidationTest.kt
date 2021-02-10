package validation

import DatabaseConnection
import factories.Tournament
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import schemas.Tournaments
import kotlin.test.assertEquals

class TournamentValidationTest {

    @AfterEach
    fun afterTest() {
        unmockkAll()
    }

    @Nested
    inner class validateTournamentExist : DatabaseConnection() {

        @Test
        fun should_run_TournamentExistByName_when_call_method_with_lastname() {
            val Tournament = Tournament.instance
            mockkObject(Tournaments)

            every { Tournaments.TournamentExistByName(any()) } returns true

            TournamentValidation.validateTournamentExist("name", Tournament.name)

            verify {
                Tournaments.TournamentExistByName(any())
            }
        }

        @Test
        fun should_run_TournamentExistById_when_call_method_with_id() {
            val Tournament = Tournament.instance
            mockkObject(Tournaments)

            every { Tournaments.TournamentExistById(any()) } returns true

            TournamentValidation.validateTournamentExist("id", Tournament.id)

            verify {
                Tournaments.TournamentExistById(any())
            }
        }

        @Test
        fun should_return_false_when_call_method_with_invalid_attribute() {
            val Tournament = Tournament.instance
            mockkObject(Tournaments)

            every { Tournaments.TournamentExistByName(any()) } returns true
            every { Tournaments.TournamentExistById(any()) } returns true

            TournamentValidation.validateTournamentExist("foo", Tournament.name)

            verify(exactly = 0) {
                Tournaments.TournamentExistById(any())
                Tournaments.TournamentExistByName(any())
            }
        }

        @Test
        fun should_return_true_if_Tournament_exist() {
            val Tournament = Tournament.instance

            val storedTournament = transaction {
                Tournaments.insert {
                    it[name] = Tournament.name
                    it[teamList] = Tournament.teamList!!
                }
            }.let {
                data.Tournament(it[Tournaments.id], it[Tournaments.name], it[Tournaments.teamList])
            }

            val returnValue = TournamentValidation.validateTournamentExist<String>("name", storedTournament.name)
            assertEquals(true, returnValue)
        }

        @Test
        fun should_return_false_if_Tournament_not_exist() {
            val returnValue = TournamentValidation.validateTournamentExist<String>("name", "foobar")
            assertEquals(false, returnValue)
        }
    }

    @Nested
    inner class validateInput {

        @Test
        fun should_return_false_if_string_is_blank() {
            val returnValue = TournamentValidation.validateInput(" ")
            assertEquals(false, returnValue)
        }

        @Test
        fun should_return_false_if_string_is_empty() {
            val returnValue = TournamentValidation.validateInput("")
            assertEquals(false, returnValue)
        }

        @Test
        fun should_return_true_if_string_is_not_empty_or_blank() {
            val returnValue = TournamentValidation.validateInput("foobar")
            assertEquals(true, returnValue)
        }
    }
}
