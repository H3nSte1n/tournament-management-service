/*import api.api
import com.fasterxml.jackson.databind.SerializationFeature
import controller.TournamentController
import data.Tournament
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.joda.time.DateTime
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class TournamentenManagmentTest {
    val body =
        """{"firstname":"Henry","lastname":"foobar","date":"2021-02-05 16:34:23.23"}"""
    lateinit var Tournament: data.Tournament

    @BeforeTest
    fun prepare() {
        Tournament = factories.Tournament.instance
        unmockkAll()
    }

    @Nested
    inner class api_v1_Tournaments {
        val path = "/api/v1/Tournaments"

        @BeforeTest
        fun prepare() {
            mockkObject(TournamentController)
            every { TournamentController.addTournament(any()) } returns Tournament
        }

        @Test
        fun should_call_TournamentController_addTournament() {
            fun tests(call: TestApplicationCall) {
                verify {
                    TournamentController.addTournament(any())
                }
            }
            initApplication(::tests, path, body, HttpMethod.Post)
        }

        @Test
        fun should_respond_Tournament() {
            fun tests(call: TestApplicationCall) {
                assertEquals(Tournament, call.response.content)
            }
            initApplication(::tests, path, body, HttpMethod.Post)
        }

        @Test
        fun should_return_200_http_status_code() {
            fun tests(call: TestApplicationCall) {
                assertEquals(HttpStatusCode.OK, call.response.status())
            }
            initApplication(::tests, path, body, HttpMethod.Post)
        }
    }

    @Nested
    inner class api_v1_sign_up {
        val path = "/api/v1/Tournaments/{id}"

        @BeforeTest
        fun prepare() {
            mockkObject(TournamentController)
            every { TournamentController.removeTournament(any()) } returns 1
        }

        @Test
        fun should_call_RegisterController_removeTournament() {
            fun tests(call: TestApplicationCall) {
                verify {
                    TournamentController.removeTournament(any())
                }
            }
            initApplication(::tests, path, body, HttpMethod.Delete)
        }

        @Test
        fun should_respond_count_of_deleted_Tournaments() {
            fun tests(call: TestApplicationCall) {
                assertEquals(1, call.response.content)
            }
            initApplication(::tests, path, body, HttpMethod.Delete)
        }

        @Test
        fun should_return_200_http_status_code() {
            fun tests(call: TestApplicationCall) {
                assertEquals(HttpStatusCode.OK, call.response.status())
            }
            initApplication(::tests, path, body, HttpMethod.Delete)
        }
    }

    private fun initApplication(tests: (call: TestApplicationCall) -> Unit, path: String, body: String, httpVerb: HttpMethod) {
        withTestApplication {
            application.routing {
                api()
            }

            application.install(ContentNegotiation) {
                jackson {
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }

            handleRequest(httpVerb, path) {
                addHeader("Accept", "*")
                addHeader("Content-Type", "application/json; charset=UTF-16")
                setBody(body.toByteArray(charset = Charsets.UTF_16))
            }.let { call ->
                tests(call)
            }
        }
    }
}
*/
