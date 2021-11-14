import genericConversion.GenericParser
import specificConversion.SpecificParser
import java.io.File

class Parser {

    private var isFileLoaded = false
    private var xmlGenericParsingResult: XmlElement? = null
    private lateinit var xmlVersion: String
    private lateinit var xmlEncoding: String
    private lateinit var xmlFile: File
    private lateinit var xmlToStringList: List<String>
    private lateinit var genericParser: GenericParser
    private lateinit var specificParser: SpecificParser

    /**
     * Loads file to parse
     *
     * @param path - path to file
     * @return Parser - Returns this Parser instance
     */
    fun loadFileToParser(path: String): Parser {
        xmlFile = File(path)
        loadXmlToStringList()
        genericParser = GenericParser(xmlToStringList)
        xmlGenericParsingResult = null
        isFileLoaded = true
        return this
    }

    /**
     * Returns XmlVersion
     *
     * @return String - Returns XmlVersion
     * @Throws XmlSuperParserNoFileLoadedException if file was not loaded before reading the XML file properties
     */
    fun getXmlVersion(): String {
        if (!isFileLoaded) throw XmlSuperParserNoFileLoadedException()
        return xmlVersion
    }

    /**
     * Returns XmlEncoding
     *
     * @return String - Returns XmlEncoding
     * @Throws XmlSuperParserNoFileLoadedException if file was not loaded before reading the XML file properties
     */
    fun getXmlEncoding(): String {
        if (!isFileLoaded) throw XmlSuperParserNoFileLoadedException()
        return xmlEncoding
    }

    /**
     * Starts parsing to generic DOM (returns a XmlElement)
     *
     * @return XmlElement - Returns a populated instance of XmlElement with all data from the XML file
     * @Throws XmlSuperParserNoFileLoadedException if file was not loaded before reading the XML file properties
     */
    fun startGenericParsing(): XmlElement {
        if (!isFileLoaded) throw XmlSuperParserNoFileLoadedException()
        if (xmlGenericParsingResult == null)
            xmlGenericParsingResult = genericParser.getXmlGenericDom()
        return xmlGenericParsingResult as XmlElement
    }

    /**
     * Starts converting the genericXml to specified type by the user
     *
     * @param userClass - user specified type
     * @return T - new instance populated of user specified type
     */
    fun <T> startSpecificParsing(userClass: Class<T>): T {
        specificParser = SpecificParser(startGenericParsing())
        return specificParser.getXmlSpecific(userClass)
    }

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
     *
     * @param mainString - Whole XML file in a single String
     * @return String - Returns the String passed as parameter already formatted
     */
    private fun clearMainStringFromParagraphsAndTabs(mainString: String): String {
        var mainStringToReturn = mainString
        mainStringToReturn = mainStringToReturn.replace("\t".toRegex(), "")
        mainStringToReturn = mainStringToReturn.replace("[^>]\r\n\\s+".toRegex(), " ")
        mainStringToReturn = mainStringToReturn.replace("\r\n\\s+".toRegex(), "\r\n")
        mainStringToReturn = mainStringToReturn.replace("\\s\r\n".toRegex(), " ")
        mainStringToReturn = mainStringToReturn.replace(",\r\n".toRegex(), " ")
        return mainStringToReturn
    }

    /**
     * Populates XmlVersion and XmlEncoding
     *
     * @param xmlStringList - XML file split in a List, line by line
     * @Throws XmlSuperParserInvalidFileException if header does not have XmlVersion or XmlEncoding
     */
    private fun readXmlVersionAndEncoding(xmlStringList: MutableList<String>) {
        val headerLine = xmlStringList[0]
        val cleanLine = headerLine.subSequence(2 until headerLine.length - 2)
        val splitHeaderLine = cleanLine.split("\\s".toRegex())
        if (splitHeaderLine.count() != 3) throw XmlSuperParserInvalidFileException("Header in unknown format. Example of how it should be: \"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\"")
        xmlVersion = splitHeaderLine[1].split("=")[1].replace("\"", "")
        xmlEncoding = splitHeaderLine[2].split("=")[1].replace("\"", "")
        xmlStringList.removeAt(0)
    }
}