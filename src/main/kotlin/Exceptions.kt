class XmlSuperParserInvalidTagTypeException(line: String) : Exception() {
    override val message = "Line \"$line\" does not match any known tag type"
}

class XmlSuperParserInvalidFileException() : Exception() {
    override val message = "This file does not have a valid xml header. This parser only supports XML files with header"
}