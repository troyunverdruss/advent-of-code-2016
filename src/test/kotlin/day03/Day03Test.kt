package day03

import org.testng.Assert.*
import org.testng.annotations.Test

class Day03Test {
    @Test
    fun testGroupByColumns() {
        val lines = mutableListOf(
                "101 301 501",
                "102 302 502",
                "103 303 503",
                "201 401 601",
                "202 402 602",
                "203 403 603"
        )

        val groupByColumns = groupByColumns(lines)
        assertTrue(groupByColumns.contains(listOf(101, 102, 103)))
        assertTrue(groupByColumns.contains(listOf(301, 302, 303)))
        assertTrue(groupByColumns.contains(listOf(501, 502, 503)))
        assertTrue(groupByColumns.contains(listOf(201, 202, 203)))
        assertTrue(groupByColumns.contains(listOf(401, 402, 403)))
        assertTrue(groupByColumns.contains(listOf(601, 602, 603)))
    }
}
