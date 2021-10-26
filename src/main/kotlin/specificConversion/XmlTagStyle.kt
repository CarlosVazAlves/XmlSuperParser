package specificConversion

import java.util.*

enum class XmlTagStyle {
    /**
     * Ex: <TAG>
     */
    ALL_UPPER_CASE_WITHOUT_UNDERSCORE {
        override fun formatTagName(tagName: String): String {
            val firstCharUpper = tagName.first()
            val firstCharLower = tagName.first().lowercaseChar()
            return tagName.lowercase(Locale.getDefault()).replaceFirst(firstCharLower, firstCharUpper)
        }
    },
    /**
     * Ex: <TAG_COMPLEX_NAME>
     */
    ALL_UPPER_CASE_WITH_UNDERSCORE {
        override fun formatTagName(tagName: String): String {
            val firstCharUpper = tagName.first().uppercaseChar()
            val charArrayTagName = tagName.lowercase(Locale.getDefault()).toCharArray()
            convertCharsAfterUnderScoreToUpperCase(charArrayTagName, tagName)
            return String(charArrayTagName).replace("_", "").replaceFirst(charArrayTagName.first(), firstCharUpper)
        }
    },
    /**
     * Ex: <tag>
     */
    ALL_LOWER_CASE_WITHOUT_UNDERSCORE {
        override fun formatTagName(tagName: String): String {
            val firstCharUpper = tagName.first().uppercaseChar()
            return tagName.replaceFirst(tagName.first(), firstCharUpper)
        }
    },
    /**
     * Ex: <tag_complex_name>
     */
    ALL_LOWER_CASE_WITH_UNDERSCORE {
        override fun formatTagName(tagName: String): String {
            val firstCharUpper = tagName.first().uppercaseChar()
            val charArrayTagName = tagName.toCharArray()
            convertCharsAfterUnderScoreToUpperCase(charArrayTagName, tagName)
            charArrayTagName[0].uppercaseChar()
            return String(charArrayTagName).replace("_", "").replaceFirst(tagName.first(), firstCharUpper)
        }
    },
    /**
     * Ex: <tagComplexName>
     */
    CAMEL_CASE {
        override fun formatTagName(tagName: String): String {
            return tagName
        }
    },
    /**
     * The "Unknown" status means that the Tag is not in any of the previous expected states
     * Although it will be made an attempt to convert the Tag name to the normalized format (camelCase)
     */
    UNKNOWN {
        override fun formatTagName(tagName: String): String {
            val firstCharUpper = tagName.first().uppercaseChar()
            val charArrayTagName = tagName.lowercase(Locale.getDefault()).toCharArray()
            convertCharsAfterUnderScoreToUpperCase(charArrayTagName, tagName)
            charArrayTagName[0].uppercaseChar()
            return String(charArrayTagName).replace("_", "").replaceFirst(charArrayTagName.first(), firstCharUpper)
        }
    };

    abstract fun formatTagName(tagName: String): String

    /**
     * Converts chars after UnderScores to UpperCase
     * @param charArray - Tag name converted in charArray for easier processing
     * @param tagName - Original Tag name to keep track of remaining underScores
     */
    protected fun convertCharsAfterUnderScoreToUpperCase(charArray: CharArray, tagName: String) {
        var amountUnderScore = charArray.count { it == '_' }
        var lastIndex = 0

        while (amountUnderScore > 0) {
            lastIndex = tagName.indexOf('_', lastIndex)
            val currentChar = charArray[lastIndex + 1]
            charArray[lastIndex + 1] = currentChar.uppercaseChar()
            amountUnderScore--
        }
    }
}