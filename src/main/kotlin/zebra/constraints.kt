package zebra

interface Constraint {
    fun valid(house: House, variable: String, value: Any, houses: List<House>): Boolean
}

class SimpleConstraint(val one: Pair<String, Any>, val other: Pair<String, Any>) : Constraint {
    override fun valid(house: House, variable: String, value: Any, houses: List<House>): Boolean {
        if (one.match(variable, value)) {
            return other.correspond(house)
        }
        if (other.match(variable, value)) {
            return one.correspond(house)
        }
        return true
    }
}

class NumberConstraint(val number: Int, val matcher: Pair<String, Any>) : Constraint {
    override fun valid(house: House, variable: String, value: Any, houses: List<House>): Boolean {
        if (matcher.match(variable, value)) {
            return number == house.number
        }
        return true
    }
}

class AnySideNeighbourConstraint(
    val offset: Int,
    val matcher: Pair<String, Any>,
    val neighbourMatcher: Pair<String, Any>
) : Constraint {

    private fun isValid(house: House, houses: List<House>, matcher: Pair<String, Any>, offset: Int): Boolean {
        val expectedNeighbors = houses.filter(matcher::match)
        val actualNeighbors = setOf(house.number + offset, house.number - offset)
        return expectedNeighbors.all { it.number in actualNeighbors }
    }

    override fun valid(house: House, variable: String, value: Any, houses: List<House>): Boolean {
        if (matcher.match(variable, value)) {
            return isValid(house, houses, neighbourMatcher, offset)
        }
        if (neighbourMatcher.match(variable, value)) {
            return isValid(house, houses, matcher, -offset)
        }
        return true
    }
}

class ExactSideNeighbourConstraint(
    val offset: Int,
    val matcher: Pair<String, Any>,
    val neighbourMatcher: Pair<String, Any>
) : Constraint {

    private fun isValid(
        house: House,
        variable: String,
        value: Any,
        houses: List<House>,
        offset: Int,
        matcher: Pair<String, Any>,
        neighbourMatcher: Pair<String, Any>
    ): Boolean {
        if (matcher.first == variable) {
            val neighborIndex = house.number + offset
            if (matcher.match(variable, value)) {
                return neighborIndex in houses.indices && neighbourMatcher.correspond(houses[neighborIndex])
            }
            return neighborIndex !in houses.indices || !neighbourMatcher.match(houses[neighborIndex])
        }
        return true
    }

    override fun valid(house: House, variable: String, value: Any, houses: List<House>): Boolean {
        return isValid(house, variable, value, houses, offset, matcher, neighbourMatcher)
                && isValid(house, variable, value, houses, -offset, neighbourMatcher, matcher)
    }
}

fun Pair<String, Any>.match(variable: String, value: Any): Boolean {
    return first == variable && second == value
}

fun Pair<String, Any>.match(house: House): Boolean {
    return second == house.variables[first]
}

fun Pair<String, Any>.correspond(house: House): Boolean {
    return null == house.variables[first] || second == house.variables[first]
}