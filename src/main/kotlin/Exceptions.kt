class XmlSuperParserInvalidTagTypeException(line: String) : Exception() {
    override val message = "Line \"$line\" does not match any known tag type"
}

class XmlSuperParserNoFileLoadedException : Exception() {
    override val message = "No file loaded"
}

class XmlSuperParserNoPathLoadedException : Exception() {
    override val message = "No path loaded"
}

class XmlSuperParserFileCreationErrorException : Exception() {
    override val message = "Impossible to create the file in the specified path or unable to grant write access"
}

class XmlSuperParserInvalidFileException(override val message: String) : Exception()