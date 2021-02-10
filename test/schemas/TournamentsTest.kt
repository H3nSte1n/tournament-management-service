package schemas

import DatabaseConnection
import DatabaseUtil.assertColumnExist
import DatabaseUtil.assertColumnType
import data.Tournament
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Test
import kotlin.test.*

class TournamentsTest : DatabaseConnection() {

    @BeforeTest
    fun prepare() {
        prepareTables()
    }

    @Nested
    inner class integration_tests {
        @Test
        @DisplayName("All Columns are avaiable")
        fun testColumnsExist() {
            transaction {
                assertColumnExist("id", Tournaments)
                assertColumnExist("name", Tournaments)
                assertColumnExist("teamList", Tournaments)
            }
        }

        @Test
        @DisplayName("All Columns has the correct sql-type")
        fun testColumnsType() {
            transaction {
                assertColumnType("id", Tournaments, "serial")
                assertColumnType("name", Tournaments, "varchar(60)")
                assertColumnType("teamList", Tournaments, "bigint[]")
            }
        }
    }

    @Nested
    inner class getTournament {

        @Test
        fun should_find_and_return_Tournament_by_name() {
            val persistedTournament = createAndStoreTournament()

            val selectedTournament = Tournaments.getTournament(persistedTournament.id!!)
            assertNotNull(selectedTournament.id)
            assertEquals(persistedTournament.name, selectedTournament.name)
            assertEquals(persistedTournament.teamList, selectedTournament.teamList)
        }
    }

    @Nested
    inner class getTournaments {

        @Test
        fun should_find_and_return_Tournaments_Collection() {
            val persistedTournament = createAndStoreTournament()

            val TournamentCollection = Tournaments.getTournaments()
            assertNotNull(TournamentCollection.first().id)
            assertEquals(persistedTournament.name, TournamentCollection.first().name)
            assertEquals(persistedTournament.teamList, TournamentCollection.first().teamList)
        }
    }

    @Nested
    inner class TournamentExistById {

        @Test
        fun should_return_true_if_Tournament_exists() {
            val persistedTournament = createAndStoreTournament()

            val TournamentExist = Tournaments.TournamentExistById(persistedTournament.id!!)
            assertTrue(TournamentExist)
        }

        @Test
        fun should_return_false_if_Tournament_exists() {
            val persistedTournament = createAndStoreTournament()

            val TournamentExist = Tournaments.TournamentExistById(persistedTournament.id!! + 1)
            assertFalse(TournamentExist)
        }
    }

    @Nested
    inner class TournamentExistByName {
        lateinit var persistedTournament: Tournament

        @BeforeEach
        fun prepare() {
            persistedTournament = createAndStoreTournament()
        }

        @Test
        fun should_return_true_if_Tournament_exists() {
            val TournamentExist = Tournaments.TournamentExistByName(persistedTournament.name)
            assertTrue(TournamentExist)
        }

        @Test
        fun should_return_false_if_Tournament_exists() {
            val TournamentExist = Tournaments.TournamentExistByName(persistedTournament.name)
            assertTrue(TournamentExist)
        }
    }

    @Nested
    inner class deleteTournament {
        lateinit var persistedTournament: Tournament

        @BeforeEach
        fun prepare() {
            persistedTournament = createAndStoreTournament()
        }

        @Test
        fun should_remove_Tournament_if_exists() {
            Tournaments.deleteTournament(persistedTournament.id!!)

            val TournamentExist = transaction {
                Tournaments.select { Tournaments.id eq persistedTournament.id!! }.empty().not()
            }
            assertFalse(TournamentExist)
        }

        @Test
        fun should_ignore_call_if_Tournament_not_exists() {
            assertDoesNotThrow { Tournaments.deleteTournament(persistedTournament.id!! + 1) }
        }
    }

    @Nested
    inner class updateTournament {
        lateinit var persistedTournament: Tournament

        @BeforeEach
        fun prepare() {
            persistedTournament = createAndStoreTournament()
        }

        @Test
        fun should_update_Tournament_if_exists() {
            val newTournamentData = Tournament(
                null,
                "hallo",
                listOf(1, 2, 3),
            )
            val updatedTournament = Tournaments.updateTournament(persistedTournament.id!!, newTournamentData)

            assertNotNull(persistedTournament.id)
            assertEquals(updatedTournament.name, newTournamentData.name)
            assertEquals(updatedTournament.teamList, newTournamentData.teamList)
        }
    }

    @Nested
    inner class createTournament {

        @Test
        fun should_create_and_persist_a_new_Tournament() {
            val Tournament = factories.Tournament.instance
            Tournaments.createTournament(Tournament)
            val persistedTournament = transaction {
                Tournaments.select { Tournaments.name eq Tournament.name }.first()
            }.let {
                Tournament(it[Tournaments.id], it[Tournaments.name], it[Tournaments.teamList])
            }

            assertNotNull(persistedTournament.id)
            assertEquals(Tournament.name, persistedTournament.name)
            assertEquals(Tournament.teamList, persistedTournament.teamList)
        }
    }

    private fun createAndStoreTournament(): Tournament {
        val Tournament = factories.Tournament.instance

        return transaction {
            Tournaments.insert {
                it[name] = Tournament.name
                it[teamList] = Tournament.teamList!!
            }
        }.let {
            Tournament(it[Tournaments.id], it[Tournaments.name], it[Tournaments.teamList])
        }
    }

    private fun prepareTables() {
        transaction {
            exec("TRUNCATE Tournaments RESTART IDENTITY CASCADE;")
        }
    }
}
