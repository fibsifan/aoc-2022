package de.jball.aoc2022.day12

import org.junit.jupiter.api.Test
import kotlin.test.junit5.JUnit5Asserter.assertEquals

class Day12Test {
    @Test
    fun height() {
        assertEquals("", 1, heightDifference('y', 'z'))
        assertEquals("", 1, heightDifference('S', 'b'))
        assertEquals("", 1, heightDifference('y', 'E'))
        assertEquals("", 2, heightDifference('b', 'd'))
        assertEquals("", -5, heightDifference('g', 'b'))
    }
}
