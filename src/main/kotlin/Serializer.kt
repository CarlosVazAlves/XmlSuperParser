import genericConversion.GenericSerializer
import java.io.File
import java.nio.charset.Charset

class Serializer {

    private lateinit var path: String
    private var isPathSet = false
    private val genericSerializer = GenericSerializer()

    /**
     * Loads file to parse
     * @param path - sets the path where the file should be saved, file name and extension included!
     * @return Serializer - Returns this Serializer instance
     */
    fun setFilePath(path: String): Serializer {
        this.path = path
        isPathSet = true
        return this
    }

    /**
     * Starts XmlElement object serialization to specified file Path
     *
     * @param xmlElement - xmlElement to be serialized to file
     * @param charset - charset intended for the XML file
     * @param pretty - if true, XML file will be generated with indentation
     * @return File - Returns the generated file reference
     * @Throws XmlSuperParserFileCreationErrorException if impossible to create file or grant write access to it
     */
    fun startGenericXmlSerializationToFile(xmlElement: XmlElement, charset: Charset, pretty: Boolean): File {
        if (!isPathSet) throw XmlSuperParserNoPathLoadedException()

        val file = File(path)
        val success = try {
            file.createNewFile()
        } catch (exception: Exception) {
            false
        }
        if (!success) throw XmlSuperParserFileCreationErrorException()

        val xmlString = genericSerializer.serializeGenericXmlToString(xmlElement, charset, pretty)
        file.writeText(xmlString, charset)
        return file
    }
}