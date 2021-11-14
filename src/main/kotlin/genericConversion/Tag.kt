package genericConversion

import XmlSuperParserInvalidTagTypeException

class Tag(private val line: String) {

    val tagType: TagType
    val name: String
    val value: String?
    val parameters: Map<String, String>?

    init {
        tagType = checkType()
        name = tagType.getName(line)
        value = tagType.getValue(line)
        parameters = tagType.getParameters(line)
    }

    /**
     * Checks if this genericConversion.TagType fits any type defined within enum
     *
     * @return TagType - Identifies the tag type and returns it
     * @Throws XmlSuperParserInvalidTagTypeException if TagType does not match any known format
     */
    private fun checkType(): TagType {
        if (isOpeningAndClosingTag()) return TagType.OPENING_AND_CLOSING_TAG
        if (isClosingTag()) return TagType.CLOSING_TAG
        if (isOpeningTagWithParameters()) return TagType.OPENING_TAG_WITH_PARAMETERS
        if (isOpeningTagWithoutParameters()) return TagType.OPENING_TAG_WITHOUT_PARAMETERS
        throw XmlSuperParserInvalidTagTypeException(line)
    }

    /**
     * Questionable way to determine if is an Opening and Closing Tag
     *
     * @return Boolean - Returns true if is an "OpeningAndClosingTag"
     */
    private fun isOpeningAndClosingTag(): Boolean {
        var greater = 0
        var lesser = 0
        var slash = 0
        for (ch in line) {
            if (ch == '<') lesser++
            if (ch == '>') greater++
            if (ch == '/') slash++
        }
        return greater >= 2 && lesser >= 2 && slash >= 1
    }

    /**
     * Questionable way to determine if is a Closing Tag
     *
     * @return Boolean - Returns true if is an "ClosingTag"
     */
    private fun isClosingTag(): Boolean {
        return !line.contains(' ') && line.contains('/')
    }

    /**
     * Questionable way to determine if is an Opening Tag with parameters
     *
     * @return Boolean - Returns true if is an "OpeningTagWithParameters"
     */
    private fun isOpeningTagWithParameters(): Boolean {
        return line.contains(' ') && line.contains('\"') && line.contains('=')
    }

    /**
     * Questionable way to determine if is an Opening genericConversion.Tag without parameters
     *
     * @return Boolean - Returns true if is an "OpeningTagWithoutParameters"
     */
    private fun isOpeningTagWithoutParameters(): Boolean {
        return !line.contains(' ') && !line.contains('/')
    }
}