import helper.Controller
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import validation.TournamentValidation
import kotlin.test.assertEquals

class ControllerTest {

    @Test
    @DisplayName("Should call validateInput with each item in string List")
    fun integrationTestOnIsInputValid() {
        val inputs = listOf("foo", "bar", "foobar", "next", "hello")
        mockkObject(TournamentValidation)

        every { TournamentValidation.validateInput(String()) } returns true

        Controller.isInputValid(inputs)

        verify(exactly = 5) {
            TournamentValidation.validateInput(any())
        }
    }

    @Test
    @DisplayName("Should return true if all inputs are valid")
    fun testValidRun() {
        val inputs = listOf("foo", "bar", "foobar", "next", "hello")
        mockkObject(TournamentValidation)

        every { TournamentValidation.validateInput(String()) } returns true

        val isValid = Controller.isInputValid(inputs)

        assertEquals(true, isValid)
    }

    @Test
    @DisplayName("Should break up and return false is invalid")
    fun testInvalidRun() {
        val inputs = listOf("foo", "bar", "foobar", " ", "hello")
        mockkObject(TournamentValidation)

        every { TournamentValidation.validateInput(String()) } returns true

        val isValid = Controller.isInputValid(inputs)

        assertEquals(false, isValid)

        verify(exactly = 4) {
            TournamentValidation.validateInput(any())
        }
    }
}
