package schemas

import arrayOfLong
import data.Tournament
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Tournaments : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 60)
    val teamList = arrayOfLong("teamList")

    fun getTournament(customerUUID: Int): Tournament {
        return transaction {
            select { id eq customerUUID }.first()
        }.let { it ->
            Tournament(it[id], it[name], it[teamList])
        }
    }

    fun getTournaments(): Collection<Tournament> {
        return transaction {
            selectAll().map {
                Tournament(it[id], it[name], it[teamList])
            }
        }
    }

    fun TournamentExistById(customerId: Int): Boolean {
        return transaction {
            select { id eq customerId }.empty().not()
        }
    }

    fun TournamentExistByName(customerName: String): Boolean {
        return transaction {
            select { name eq customerName }.empty().not()
        }
    }

    fun deleteTournament(givenUUID: Int): Int {
        return transaction {
            deleteWhere { id eq givenUUID }
        }
    }

    fun updateTournament(selectedId: Int, Tournament: Tournament): Tournament {
        return transaction {
            update({ id eq selectedId }) {
                it[name] = Tournament.name
                it[teamList] = Tournament.teamList!!
            }
        }.let {
            getTournament(selectedId)
        }
    }

    fun createTournament(Tournament: Tournament): Tournament {
        return transaction {
            insert {
                it[name] = Tournament.name
                it[teamList] = Tournament.teamList!!
            }
        }.let {
            Tournament(it[id], it[name], it[teamList])
        }
    }
}
