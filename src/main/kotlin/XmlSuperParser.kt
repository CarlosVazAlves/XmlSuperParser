import java.io.File

class XmlSuperParser {

    private lateinit var xmlVersion: String
    private lateinit var xmlEncoding: String
    private lateinit var xmlFile: File
    private lateinit var xmlToStringList: List<String>
    private val tagsCurrentlyOpen: ArrayList<String> = ArrayList()

    fun setFile(path: String) {
        setFile(File(path))
    }

    fun setFile(file: File) {
        xmlFile = file
        loadXmlToStringList()
    }

    /**
     * Returns XmlVersion
     */
    fun getXmlVersion(): String = xmlVersion

    /**
     * Returns XmlEncoding
     */
    fun getXmlEncoding(): String = xmlEncoding

    /**
     * After reading the XML file and convert it to String,
     * the String will be parsed so all tags will be separated by \r\n
     * It happened to test an XML file that had in the last line an empty string.
     * That line will be removed as well.
     * Remaining global variables will be populated
     */
    private fun loadXmlToStringList() {
        val xmlString = clearMainStringFromParagraphsAndTabs(String(xmlFile.readBytes()))
        val xmlStringSplit = xmlString.split("\r\n").toMutableList()
        if (xmlStringSplit.last() == "") xmlStringSplit.removeAt(xmlStringSplit.lastIndex)
        readXmlVersionAndEncoding(xmlStringSplit)
        xmlToStringList = xmlStringSplit
    }

    /**
     * Replaces from the initial string:
     * ">\r\n   " -> " "
     * "\r\n    " -> "\r\n"
     * " \r\n" -> " "
     * ",\r\n*" -> " "
     */
    private fun clearMainStringFromParagraphsAndTabs(mainString: String): String {
        var mainStringToReturn = mainString
        mainStringToReturn = mainStringToReturn.replace("[^>]\r\n\\s+".toRegex(), " ")
        mainStringToReturn = mainStringToReturn.replace("\r\n\\s+".toRegex(), "\r\n")
        mainStringToReturn = mainStringToReturn.replace("\\s\r\n".toRegex(), " ")
        mainStringToReturn = mainStringToReturn.replace(",\r\n".toRegex(), " ")
        return mainStringToReturn
    }

    /**
     * Will parse the XML file loaded into a generic DOM (returns a XmlElement)
     */
    fun getXmlGenericDom(): XmlElement {
        val xmlStringList = xmlToStringList.toMutableList()
        return parseToGenericXmlElement(xmlStringList, XmlElement())
    }

    /**
     * Populates XmlVersion and XmlEncoding
     * Throws XmlSuperParserInvalidFileException if header does not have XmlVersion or XmlEncoding
     */
    private fun readXmlVersionAndEncoding(xmlStringList: MutableList<String>) {
        val headerLine = xmlStringList[0]
        val cleanLine = headerLine.subSequence(2 until headerLine.length - 2)
        val splitHeaderLine = cleanLine.split("\\s".toRegex())
        if (splitHeaderLine.count() != 3) throw XmlSuperParserInvalidFileException()
        xmlVersion = splitHeaderLine[1].split("=")[1].replace("\"", "")
        xmlEncoding = splitHeaderLine[2].split("=")[1].replace("\"", "")
        xmlStringList.removeAt(0)
    }

    /**
     * Starts parsing the XmlStringList line by line recursively
     */
    private fun parseToGenericXmlElement(xmlStringList: MutableList<String>, currentXmlElement: XmlElement): XmlElement {
        if (xmlStringList.isEmpty()) return currentXmlElement
        val tagLine = Tag(xmlStringList[0])

        return when (tagLine.tagType) {
            TagType.OPENING_TAG_WITH_PARAMETERS -> parseOpeningTag(tagLine, xmlStringList, currentXmlElement, true)
            TagType.OPENING_TAG_WITHOUT_PARAMETERS -> parseOpeningTag(tagLine, xmlStringList, currentXmlElement, false)
            TagType.OPENING_AND_CLOSING_TAG -> parseOpeningAndClosingTag(tagLine, xmlStringList, currentXmlElement)
            TagType.CLOSING_TAG -> parseClosingTag(tagLine, xmlStringList, currentXmlElement)
        }
    }

    /**
     * Parses an Opening Tag
     * ex: <CLIENTE id="514408852"> with parameters
     * ex: <IDENTIFICADOR> without parameters
     */
    private fun parseOpeningTag(tag: Tag, xmlStringList: MutableList<String>, currentXmlElement: XmlElement, hasParameters: Boolean): XmlElement {
        val name = tag.name
        currentXmlElement.name = name

        tagsCurrentlyOpen.add(name)

        if (hasParameters) {
            populateCurrentXmlElementParameters(currentXmlElement, tag.parameters!!)
        }

        xmlStringList.removeAt(0)
        val xmlElementToContinue: XmlElement

        val nextTagLine = Tag(xmlStringList[0])
        if (nextTagLine.tagType == TagType.OPENING_TAG_WITH_PARAMETERS || nextTagLine.tagType == TagType.OPENING_TAG_WITHOUT_PARAMETERS ) {
            val newXmlElement = XmlElement()
            currentXmlElement.children.add(newXmlElement)
            newXmlElement.parent = currentXmlElement
            xmlElementToContinue = newXmlElement
        } else if (nextTagLine.tagType == TagType.CLOSING_TAG) {
            xmlElementToContinue = currentXmlElement.parent!!
        } else {
            xmlElementToContinue = currentXmlElement
        }

        return parseToGenericXmlElement(xmlStringList, xmlElementToContinue)
    }

    /**
     * Parses an Opening and Closing Tag
     * <SAIDA>Torres Novas</SAIDA>
     */
    private fun parseOpeningAndClosingTag(tag: Tag, xmlStringList: MutableList<String>, currentXmlElement: XmlElement): XmlElement {
        val name = tag.name
        val value = tag.value!!
        val newXmlFinalElement = XmlFinalElement(name, value, currentXmlElement)
        currentXmlElement.finalChildren.add(newXmlFinalElement)

        xmlStringList.removeAt(0)
        val xmlElementToContinue: XmlElement

        val nextTagLine = Tag(xmlStringList[0])
        if (nextTagLine.tagType == TagType.OPENING_TAG_WITH_PARAMETERS || nextTagLine.tagType == TagType.OPENING_TAG_WITHOUT_PARAMETERS) {
            val newXmlElement = XmlElement()
            currentXmlElement.children.add(newXmlElement)
            newXmlElement.parent = currentXmlElement
            xmlElementToContinue = newXmlElement
        } else {
            xmlElementToContinue = currentXmlElement
        }

        return parseToGenericXmlElement(xmlStringList, xmlElementToContinue)
    }

    /**
     * Parses a Closing Tag
     * </TRANSACCAO>
     */
    private fun parseClosingTag(tag: Tag, xmlStringList: MutableList<String>, currentXmlElement: XmlElement): XmlElement {
        val name = tag.name
        if (tagsCurrentlyOpen.last() == name) {
            val lastIndex = tagsCurrentlyOpen.lastIndex
            tagsCurrentlyOpen.removeAt(lastIndex)
        }

        xmlStringList.removeAt(0)
        val xmlElementToContinue: XmlElement

        if (xmlStringList.isEmpty()) return parseToGenericXmlElement(xmlStringList, currentXmlElement)

        val nextTagLine = Tag(xmlStringList[0])
        if (nextTagLine.tagType == TagType.OPENING_TAG_WITH_PARAMETERS || nextTagLine.tagType == TagType.OPENING_TAG_WITHOUT_PARAMETERS) {
            val newXmlChildElement = XmlElement()
            currentXmlElement.parent!!.children.add(newXmlChildElement)
            newXmlChildElement.parent = currentXmlElement.parent
            xmlElementToContinue = currentXmlElement.parent!!.children.last()
        } else {
            xmlElementToContinue = currentXmlElement.parent!!
        }

        return parseToGenericXmlElement(xmlStringList, xmlElementToContinue)
    }

    /**
     * Populates currentXmlElement with parameters passed as argument
     */
    private fun populateCurrentXmlElementParameters(currentXmlElement: XmlElement, parameters: Map<String, String>) {
        val keys = parameters.keys
        for (key in keys) {
            currentXmlElement.properties.add(key)
            currentXmlElement.propertiesValue.add(parameters.getValue(key))
        }
    }
}