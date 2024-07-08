package zebra

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SimpleConstraintTest {
    val constraint = SimpleConstraint("color" to "Green", "plant" to "Tree")

    @Test
    fun `try assign unexpected, valid`() {
        val house = House(1)
        assertTrue(constraint.valid(house, "pet", "Dog", listOf(house)))
    }

    @Test
    fun `try assign first expected, no second's fields, valid`() {
        val house = House(1, mutableMapOf("pet" to "Dog"))
        assertTrue(constraint.valid(house, "color", "Green", listOf(house)))
    }

    @Test
    fun `try assign first expected, valid`() {
        val house = House(1, mutableMapOf("plant" to "Tree"))
        assertTrue(constraint.valid(house, "color", "Green", listOf(house)))
    }

    @Test
    fun `try assign first expected, invalid`() {
        val house = House(1, mutableMapOf("plant" to "Bush"))
        assertFalse(constraint.valid(house, "color", "Green", listOf(house)))
    }

    @Test
    fun `try assign second expected, no first's fields, valid`() {
        val house = House(1, mutableMapOf("pet" to "Dog"))
        assertTrue(constraint.valid(house, "plant", "Tree", listOf(house)))
    }

    @Test
    fun `try assign second expected, valid`() {
        val house = House(1, mutableMapOf("color" to "Green"))
        assertTrue(constraint.valid(house, "plant", "Tree", listOf(house)))
    }

    @Test
    fun `try assign second expected, invalid`() {
        val house = House(1, mutableMapOf("color" to "Red"))
        assertFalse(constraint.valid(house, "plant", "Tree", listOf(house)))
    }
}

class AnySideNeighbourConstraintTest {
    @Test
    fun `try assign first expected, valid neighbour is to the left, valid`() {
        val constraint = AnySideNeighbourConstraint(
            1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0, mutableMapOf("color" to "White", "plant" to "Bush")),
            House(1, mutableMapOf("plant" to "Tree", "pet" to "Dog")),
            House(2, mutableMapOf("color" to "Red"))
        )
        assertTrue(constraint.valid(houses[1], "color", "Green", houses))
    }

    @Test
    fun `try assign first expected, valid neighbour is to the right, valid`() {
        val constraint = AnySideNeighbourConstraint(
            2,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0, mutableMapOf("color" to "Red")),
            House(1, mutableMapOf("plant" to "Tree", "pet" to "Dog")),
            House(2, mutableMapOf("color" to "Yellow")),
            House(3, mutableMapOf("color" to "White", "plant" to "Bush")),
        )
        assertTrue(constraint.valid(houses[1], "color", "Green", houses))
    }

    @Test
    fun `try assign first expected, no valid neighbour, invalid`() {
        val constraint = AnySideNeighbourConstraint(
            1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0, mutableMapOf("color" to "Red")),
            House(1, mutableMapOf("plant" to "Tree", "pet" to "Dog")),
            House(2, mutableMapOf("color" to "Yellow", "plant" to "Bush")),
            House(3, mutableMapOf("color" to "White")),
        )
        assertFalse(constraint.valid(houses[1], "color", "Green", houses))
    }

    @Test
    fun `try assign second expected, valid neighbour is to the right, valid`() {
        val constraint = AnySideNeighbourConstraint(
            1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0, mutableMapOf("plant" to "Bush")),
            House(1, mutableMapOf("color" to "Green", "plant" to "Tree", "pet" to "Dog")),
            House(2, mutableMapOf("color" to "Red"))
        )
        assertTrue(constraint.valid(houses[0], "color", "White", houses))
    }

    @Test
    fun `try assign second expected, valid neighbour is to the left, valid`() {
        val constraint = AnySideNeighbourConstraint(
            2,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0, mutableMapOf("color" to "Red")),
            House(1, mutableMapOf("color" to "Green", "plant" to "Tree", "pet" to "Dog")),
            House(2, mutableMapOf("color" to "Yellow")),
            House(3, mutableMapOf("plant" to "Bush")),
        )
        assertTrue(constraint.valid(houses[3], "color", "White", houses))
    }

    @Test
    fun `try assign second expected, no valid neighbour, invalid`() {
        val constraint = AnySideNeighbourConstraint(
            2,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0, mutableMapOf("color" to "Red")),
            House(1, mutableMapOf("color" to "Yellow", "plant" to "Bush")),
            House(2, mutableMapOf("color" to "Green", "plant" to "Tree", "pet" to "Dog")),
            House(3),
        )
        assertFalse(constraint.valid(houses[3], "color", "White", houses))
    }

    @Test
    fun `try assign unexpected, valid`() {
        val constraint = AnySideNeighbourConstraint(
            2,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0, mutableMapOf("color" to "Red")),
            House(1, mutableMapOf("color" to "Green", "plant" to "Tree", "pet" to "Dog")),
            House(2, mutableMapOf("plant" to "Bush")),
            House(3, mutableMapOf("color" to "White")),
        )
        assertTrue(constraint.valid(houses[2], "color", "Yellow", houses))
    }
}

class ExactSideNeighbourConstraintTest {
    @Test
    fun `try assign first expected, neighbor corresponds, valid`() {
        val constraint = ExactSideNeighbourConstraint(
            -1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0, mutableMapOf("color" to "White")),
            House(1),
            House(2, mutableMapOf("color" to "Yellow"))
        )
        assertTrue(constraint.valid(houses[1], "color", "Green", houses))
    }

    @Test
    fun `try assign first expected, neighbor doesn't correspond, invalid`() {
        val constraint = ExactSideNeighbourConstraint(
            -1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0, mutableMapOf("color" to "Yellow")),
            House(1),
            House(2, mutableMapOf("color" to "White"))
        )
        assertFalse(constraint.valid(houses[1], "color", "Green", houses))
    }

    @Test
    fun `try assign first expected, neighbor hasn't been assigned, valid`() {
        val constraint = ExactSideNeighbourConstraint(
            -1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0),
            House(1),
            House(2, mutableMapOf("color" to "White"))
        )
        assertTrue(constraint.valid(houses[1], "color", "Green", houses))
    }

    @Test
    fun `try assign unexpected, neighbor corresponds to second expected, invalid`() {
        val constraint = ExactSideNeighbourConstraint(
            -1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0, mutableMapOf("color" to "White")),
            House(1),
            House(2, mutableMapOf("color" to "Green"))
        )
        assertFalse(constraint.valid(houses[1], "color", "Red", houses))
    }

    @Test
    fun `try assign second, neighbor corresponds, valid`() {
        val constraint = ExactSideNeighbourConstraint(
            -1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0),
            House(1, mutableMapOf("color" to "Green")),
            House(2, mutableMapOf("color" to "Yellow"))
        )
        assertTrue(constraint.valid(houses[0], "color", "White", houses))
    }

    @Test
    fun `try assign second, neighbor doesn't correspond, invalid`() {
        val constraint = ExactSideNeighbourConstraint(
            -1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0, mutableMapOf("color" to "Green")),
            House(1),
            House(2, mutableMapOf("color" to "Yellow"))
        )
        assertFalse(constraint.valid(houses[1], "color", "White", houses))
    }

    @Test
    fun `try assign second, neighbor hasn't been assigned, valid`() {
        val constraint = ExactSideNeighbourConstraint(
            -1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0, mutableMapOf("color" to "Red")),
            House(1),
            House(2)
        )
        assertTrue(constraint.valid(houses[1], "color", "White", houses))
    }

    @Test
    fun `try assign unexpected, neighbor corresponds to first expected, invalid`() {
        val constraint = ExactSideNeighbourConstraint(
            -1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0),
            House(1, mutableMapOf("color" to "Green")),
            House(2, mutableMapOf("color" to "White"))
        )
        assertFalse(constraint.valid(houses[0], "color", "Red", houses))
    }

    @Test
    fun `try assign unexpected value, neighbor doesn't correspond, valid`() {
        val constraint = ExactSideNeighbourConstraint(
            -1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0),
            House(1, mutableMapOf("color" to "Yellow")),
            House(2, mutableMapOf("color" to "White"))
        )
        assertTrue(constraint.valid(houses[0], "color", "Red", houses))
    }

    @Test
    fun `try assign unexpected variable, neighbor corresponds, valid`() {
        val constraint = ExactSideNeighbourConstraint(
            -1,
            "color" to "Green",
            "color" to "White"
        )
        val houses = listOf(
            House(0),
            House(1, mutableMapOf("color" to "Green")),
            House(2, mutableMapOf("color" to "White"))
        )
        assertTrue(constraint.valid(houses[0], "plant", "Red", houses))
    }
}