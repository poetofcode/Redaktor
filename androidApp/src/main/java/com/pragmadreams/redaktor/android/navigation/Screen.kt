package com.pragmadreams.redaktor.android.navigation

open class Screen(
    open val route: String,
) {
    private var arguments : List<Pair<String, Any>> = emptyList()

    fun withArguments(vararg arguments : Pair<String, Any>): Screen {
        this.arguments = arguments.asList()
        return this
    }

    fun buildTargetRoute(): String {
        var res = route
        arguments.forEach { (name, value) ->
            res = res.replace("{${name}}", value.toString())
        }
        return res
    }
}