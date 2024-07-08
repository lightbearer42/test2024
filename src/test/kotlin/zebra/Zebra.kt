package zebra

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ZebraTest {
    @Test
    fun `success solution`() {
        val domain = mapOf(
            "color" to mutableListOf("Green", "Blue", "White", "Red", "Yellow"),
            "pet" to mutableListOf("Fish", "Dog", "Cat", "Parrot", "Hamster"),
            "nationality" to mutableListOf("Chinese", "Spanish", "Ukrainian", "Belorussian", "Georgian"),
            "plant" to mutableListOf("Raspberry", "Strawberry", "Gooseberry", "Grape", "Blueberry"),
            "chocolate" to mutableListOf("Twix", "Bounty", "Snickers", "KitKat", "Mars")
        )
        val constraints = listOf(
            SimpleConstraint("color" to "Red", "nationality" to "Ukrainian"),
            SimpleConstraint("pet" to "Dog", "nationality" to "Belorussian"),
            SimpleConstraint("color" to "Green", "plant" to "Raspberry"),
            SimpleConstraint("nationality" to "Georgian", "plant" to "Grape"),
            ExactSideNeighbourConstraint(-1, "color" to "Green", "color" to "White"),
            SimpleConstraint("chocolate" to "Twix", "pet" to "Cat"),
            SimpleConstraint("color" to "Yellow", "chocolate" to "Snickers"),
            NumberConstraint(2, "plant" to "Gooseberry"),
            NumberConstraint(0, "nationality" to "Spanish"),
            AnySideNeighbourConstraint(1, "chocolate" to "Mars", "pet" to "Parrot"),
            AnySideNeighbourConstraint(1, "pet" to "Hamster", "chocolate" to "Snickers"),
            SimpleConstraint("chocolate" to "KitKat", "plant" to "Strawberry"),
            SimpleConstraint("nationality" to "Chinese", "chocolate" to "Bounty"),
            AnySideNeighbourConstraint(1, "nationality" to "Spanish", "color" to "Blue")
        )
        val assignment = Zebra(domain, constraints).find()
        val expected: List<House> = listOf(
            House(0, mutableMapOf("color" to "Yellow", "nationality" to "Spanish", "chocolate" to "Snickers", "plant" to "Blueberry", "pet" to "Parrot")),
            House(1, mutableMapOf("color" to "Blue", "nationality" to "Georgian", "chocolate" to "Mars", "plant" to "Grape", "pet" to "Hamster")),
            House(2, mutableMapOf("color" to "Red", "nationality" to "Ukrainian", "chocolate" to "Twix", "plant" to "Gooseberry", "pet" to "Cat")),
            House(3, mutableMapOf("color" to "White", "nationality" to "Belorussian", "chocolate" to "KitKat", "plant" to "Strawberry", "pet" to "Dog")),
            House(4, mutableMapOf("color" to "Green", "nationality" to "Chinese", "chocolate" to "Bounty", "plant" to "Raspberry", "pet" to "Fish")),
        )
        assertEquals(expected, assignment.sortedBy { it.number })
    }

    @Test
    fun `no solutions`() {
        val domain = mapOf(
            "color" to mutableListOf("Green", "Blue", "White", "Red", "Yellow"),
            "pet" to mutableListOf("Fish", "Dog", "Cat", "Parrot", "Hamster"),
            "nationality" to mutableListOf("Chinese", "Spanish", "Ukrainian", "Belorussian", "Georgian"),
            "plant" to mutableListOf("Raspberry", "Strawberry", "Gooseberry", "Grape", "Blueberry"),
            "chocolate" to mutableListOf("Twix", "Bounty", "Snickers", "KitKat", "Mars")
        )
        val constraints = listOf(
            SimpleConstraint("color" to "Red", "nationality" to "Ukrainian"),
            SimpleConstraint("pet" to "Cat", "nationality" to "Belorussian"),
            SimpleConstraint("color" to "Green", "plant" to "Raspberry"),
            SimpleConstraint("nationality" to "Georgian", "plant" to "Grape"),
            ExactSideNeighbourConstraint(2, "color" to "Green", "color" to "White"),
            SimpleConstraint("chocolate" to "Twix", "pet" to "Cat"),
            SimpleConstraint("color" to "Blue", "chocolate" to "Snickers"),
            NumberConstraint(2, "plant" to "Gooseberry"),
            NumberConstraint(0, "nationality" to "Spanish"),
            AnySideNeighbourConstraint(1, "chocolate" to "Bounty", "pet" to "Parrot"),
            AnySideNeighbourConstraint(1, "pet" to "Parrot", "chocolate" to "Snickers"),
            SimpleConstraint("chocolate" to "KitKat", "plant" to "Strawberry"),
            SimpleConstraint("nationality" to "Chinese", "chocolate" to "Bounty"),
            AnySideNeighbourConstraint(1, "nationality" to "Spanish", "color" to "Green")
        )
        val assignment = Zebra(domain, constraints).find()
        assertTrue(assignment.isEmpty())
    }

    @Test
    fun `domain has variables with different numbers of value`() {
        val domain = mapOf(
            "color" to mutableListOf("Green", "Blue", "White", "Red"),
            "pet" to mutableListOf("Fish", "Dog", "Cat", "Parrot", "Hamster"),
            "nationality" to mutableListOf("Chinese", "Spanish", "Ukrainian", "Belorussian", "Georgian"),
            "plant" to mutableListOf("Raspberry", "Strawberry", "Gooseberry", "Grape", "Blueberry"),
            "chocolate" to mutableListOf("Twix", "Bounty", "Snickers", "KitKat", "Mars")
        )
        assertFailsWith<IllegalArgumentException> {
            Zebra(domain, listOf()).find()
        }
    }
}