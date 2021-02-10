package controller

import helper.Controller
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import schemas.Tournaments
import statuspages.InvalidTournamentException
import statuspages.ThrowableException
import validation.TournamentValidation
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class TournamentsControllerTest {
    lateinit var Tournament: data.Tournament

    @BeforeTest
    fun prepare() {
        Tournament = factories.Tournament.instance
        unmockkAll()
    }

    @AfterEach
    fun afterTest() {
        unmockkAll()
    }

    @Nested
    inner class when_run_removeTournament {

        @Test
        fun should_call_specific_methods() {
            mockkObject(TournamentValidation)
            mockkObject(Tournaments)

            every { TournamentValidation.validateTournamentExist<Int>(any(), any()) } returns true
            every { Tournaments.deleteTournament(any()) } returns 1

            TournamentsController.removeTournament(3)

            verify {
                TournamentValidation.validateTournamentExist<Int>(any(), any())
                Tournaments.deleteTournament(any())
            }

            verifyOrder {
                TournamentValidation.validateTournamentExist<Int>(any(), any())
                Tournaments.deleteTournament(any())
            }
        }

        @Test
        fun should_break_up_if_input_is_invalid() {
            mockkObject(TournamentValidation)

            every { TournamentValidation.validateTournamentExist<Int>(any(), any()) } returns false

            assertThrows(InvalidTournamentException::class.java) {
                TournamentsController.removeTournament(3)
            }
        }

        @Test
        fun should_return_count_of_removed_Tournaments_on_valid_request() {
            mockkObject(TournamentValidation)
            mockkObject(Tournaments)

            every { TournamentValidation.validateTournamentExist<Int>(any(), any()) } returns true
            every { Tournaments.deleteTournament(any()) } returns 1

            assertEquals(TournamentsController.removeTournament(3), 1)
        }
    }

    @Nested
    inner class when_run_addTournament {

        @Test
        fun should_call_specific_methods() {
            mockkObject(Controller)
            mockkObject(TournamentValidation)
            mockkObject(Tournaments)

            every { Controller.isInputValid(any()) } returns true
            every { TournamentValidation.validateTournamentExist<String>(any(), any()) } returns false
            every { Tournaments.createTournament(any()) } returns Tournament

            TournamentsController.addTournament(Tournament)

            verify {
                Controller.isInputValid(any())
                TournamentValidation.validateTournamentExist<String>(any(), any())
                Tournaments.createTournament(any())
            }

            verifyOrder {
                Controller.isInputValid(any())
                TournamentValidation.validateTournamentExist<String>(any(), any())
                Tournaments.createTournament(any())
            }
        }

        @Test
        fun should_break_up_if_input_is_invalid() {
            mockkObject(Controller)

            every { Controller.isInputValid(any()) } returns false

            assertThrows(ThrowableException::class.java) {
                TournamentsController.addTournament(Tournament)
            }
        }

        @Test
        fun should_break_up_if_Tournament_already_exist() {
            mockkObject(Controller)
            mockkObject(TournamentValidation)

            every { Controller.isInputValid(any()) } returns true
            every { TournamentValidation.validateTournamentExist<String>(any(), any()) } returns true

            assertThrows(InvalidTournamentException::class.java) {
                TournamentsController.addTournament(Tournament)
            }
        }

        @Test
        fun should_return_added_Tournament_on_valid_request() {
            mockkObject(Controller)
            mockkObject(TournamentValidation)
            mockkObject(Tournaments)

            every { Controller.isInputValid(any()) } returns true
            every { TournamentValidation.validateTournamentExist<String>(any(), any()) } returns false
            every { Tournaments.createTournament(any()) } returns Tournament

            assertEquals(TournamentsController.addTournament(Tournament), Tournament)
        }
    }

    @Nested
    inner class when_run_getAllTournaments {

        @Test
        fun should_call_specific_method() {
            mockkObject(Tournaments)

            every { Tournaments.getTournaments() } returns listOf(Tournament)

            TournamentsController.getAllTournaments()

            verify {
                Tournaments.getTournaments()
            }
        }

        @Test
        fun should_return_list_of_stored_Tournaments_on_valid_request() {
            mockkObject(Tournaments)

            every { Tournaments.getTournaments() } returns listOf(Tournament)

            assertEquals(TournamentsController.getAllTournaments(), listOf(Tournament))
        }
    }

    @Nested
    inner class when_run_updateTournament {

        @Test
        fun should_call_specific_method() {
            mockkObject(Controller)
            mockkObject(TournamentValidation)
            mockkObject(Tournaments)

            every { Controller.isInputValid(any()) } returns true
            every { TournamentValidation.validateTournamentExist<String>(any(), any()) } returns true
            every { Tournaments.updateTournament(any(), any()) } returns Tournament

            TournamentsController.updateTournament(1, Tournament)

            verify {
                Controller.isInputValid(any())
                TournamentValidation.validateTournamentExist<String>(any(), any())
                Tournaments.updateTournament(any(), any())
            }

            verifyOrder {
                Controller.isInputValid(any())
                TournamentValidation.validateTournamentExist<String>(any(), any())
                Tournaments.updateTournament(any(), any())
            }
        }

        @Test
        fun should_break_up_if_input_is_invalid() {
            mockkObject(Controller)

            every { Controller.isInputValid(any()) } returns false

            assertThrows(ThrowableException::class.java) {
                TournamentsController.updateTournament(1, Tournament)
            }
        }

        @Test
        fun should_break_up_if_Tournament_not_exist() {
            mockkObject(Controller)
            mockkObject(TournamentValidation)

            every { Controller.isInputValid(any()) } returns true
            every { TournamentValidation.validateTournamentExist<String>(any(), any()) } returns false

            assertThrows(InvalidTournamentException::class.java) {
                TournamentsController.updateTournament(1, Tournament)
            }
        }

        @Test
        fun should_return_updated_Tournament_on_valid_request() {
            mockkObject(Controller)
            mockkObject(TournamentValidation)
            mockkObject(Tournaments)

            every { Controller.isInputValid(any()) } returns true
            every { TournamentValidation.validateTournamentExist<String>(any(), any()) } returns true
            every { Tournaments.updateTournament(any(), any()) } returns Tournament

            assertEquals(TournamentsController.updateTournament(1, Tournament), Tournament)
        }
    }
}
