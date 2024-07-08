package zebra

class Zebra(private val domain: Map<String, List<Any>>, private val constraints: List<Constraint>) {
    private val variables = domain.keys.sorted()

    init {
        if (domain.map { it.value.size }.toSet().size > 1) {
            throw IllegalArgumentException("Each variable of domain must have equal number of values")
        }
    }

    private fun isConsistent(assignment: List<House>, value: Any, variable: String, house: House): Boolean {
        if (assignment.any { it != house && it.variables[variable] == value }) return false
        for (constraint in constraints) {
            if (!constraint.valid(house, variable, value, assignment)) {
                return false
            }
        }
        return true
    }

    fun nextHouseAndVariableIndex(current: Pair<Int, Int>): Pair<Int, Int> {
        return if (current.second == variables.lastIndex) {
            current.first + 1 to 0
        } else {
            current.first to current.second + 1
        }
    }

    private fun backtrack(assignment: List<House>, current: Pair<Int, Int>): Boolean {
        if (assignment.all { it.variables.size == domain.size }) {
            return true
        }

        val house = assignment[current.first]
        val variable = variables[current.second]
        val values = domain[variable] ?: throw IllegalStateException("Domain variable not found")
        for (value in values) {
            val consistent = isConsistent(assignment, value, variable, house)
            if (consistent) {
                house.variables[variable] = value
                val result = backtrack(assignment, nextHouseAndVariableIndex(current))
                if (result) {
                    return result
                }
                house.variables.remove(variable)
            }
        }

        return false
    }

    fun find(): List<House> {
        val size: Int = domain.values.minByOrNull { it.size }?.size ?: 0
        val assignment = List(size) { House(it) }
        if (backtrack(assignment, 0 to 0)) {
            return assignment
        }
        return emptyList()
    }
}
