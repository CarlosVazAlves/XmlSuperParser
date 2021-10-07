enum class TagType() {

    /**
     * Ex: <CLIENTE id="514408852">
     */
    OPENING_TAG_WITH_PARAMETERS {
        override fun getName(line: String): String {
            return line.split("\\s".toRegex())[0].replace("<", "")
        }

        override fun getParameters(line: String): Map<String, String> {
            val parameters = mutableMapOf<String, String>()
            val splitLine = line.split("\\s".toRegex())
            for (item in splitLine) {
                val itemSplit = item.split("=")
                if (itemSplit.count() == 2) {
                    parameters[itemSplit[0]] = clearString(itemSplit[1])
                }
            }
            return parameters
        }

        override fun getValue(line: String): String? = null
    },

    /**
     * Ex: <IDENTIFICADOR>
     */
    OPENING_TAG_WITHOUT_PARAMETERS {
        override fun getName(line: String): String {
            val endIndex = line.length - 1
            return line.subSequence(1, endIndex).toString()
        }

        override fun getValue(line: String): String? = null
        override fun getParameters(line: String): Map<String, String>? = null
    },

    /**
     * Ex: </TRANSACCAO>
     */
    CLOSING_TAG {
        override fun getName(line: String): String {
            val endIndex = line.length - 1
            return line.subSequence(2, endIndex).toString()
        }

        override fun getValue(line: String): String? = null
        override fun getParameters(line: String): Map<String, String>? = null
    },

    /**
     * Ex: <SAIDA>Torres Novas</SAIDA>
     */
    OPENING_AND_CLOSING_TAG {
        override fun getName(line: String): String {
            val endIndex = line.indexOf(">")
            return line.subSequence(1 until endIndex).toString()
        }

        override fun getValue(line: String): String {
            val startIndex = line.indexOf(">") + 1
            val endIndex = line.indexOf("<", 1)
            return line.subSequence(startIndex, endIndex).toString()
        }

        override fun getParameters(line: String): Map<String, String>? = null
    };

    abstract fun getName(line: String): String
    abstract fun getValue(line: String): String?
    abstract fun getParameters(line: String): Map<String, String>?

    protected fun clearString(line: String): String {
        return line.replace(">","").replace("\"", "")
    }
}