class VariableList() : HashMap<String, Double>() {
    override fun toString(): String {
        var list = "Variable list: "

        if (this.size > 0)
            for (v in this) {
                list += "\n${v.key} = ${v.value}"
            }

        else list += "No variables"
        return list
    }
}