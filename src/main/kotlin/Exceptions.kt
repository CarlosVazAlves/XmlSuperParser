class XmlSuperParserInvalidTagTypeException(line: String) : Exception() {
    override val message = "Line \"$line\" does not match any known tag type"
}

class XmlSuperParserInvalidFileException(override val message: String) : Exception()