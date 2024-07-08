package zebra

data class House(
    val number: Int,
    val variables: MutableMap<String, Any> = mutableMapOf()
)
