package factories

import data.Tournament

object Tournament {
    val instance by lazy {
        Tournament(0, "foo", listOf(1, 2, 3))
    }
}
