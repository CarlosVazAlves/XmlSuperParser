import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GenericSerializerTests {

    private val xmlSuperParser = XmlSuperParser()

    @Test
    fun testTollXmlFileGenericSerialization() {
        val originalXml = xmlSuperParser.parser.loadFileToParser("./src/test/resources/Toll.xml").startGenericParsing()
        val file = xmlSuperParser.serializer.setFilePath("./src/test/resources/testToll.xml").startGenericXmlSerializationToFile(originalXml, Charsets.ISO_8859_1, true)
        val generatedXml = xmlSuperParser.parser.loadFileToParser("./src/test/resources/testToll.xml").startGenericParsing()

        Assertions.assertEquals(originalXml.name, originalXml.name)

        Assertions.assertEquals(originalXml.finalChildren[0].name, generatedXml.finalChildren[0].name)
        Assertions.assertEquals(originalXml.finalChildren[0].value, generatedXml.finalChildren[0].value)
        Assertions.assertEquals(originalXml.finalChildren[2].name, generatedXml.finalChildren[2].name)
        Assertions.assertEquals(originalXml.finalChildren[2].value, generatedXml.finalChildren[2].value)

        Assertions.assertEquals(originalXml.children[0].name, generatedXml.children[0].name)
        Assertions.assertEquals(originalXml.children[1].name, generatedXml.children[1].name)

        Assertions.assertEquals(originalXml.children[0].finalChildren[1].name, generatedXml.children[0].finalChildren[1].name)
        Assertions.assertEquals(originalXml.children[0].finalChildren[1].value, generatedXml.children[0].finalChildren[1].value)

        Assertions.assertEquals(originalXml.children[1].finalChildren[2].name, generatedXml.children[1].finalChildren[2].name)
        Assertions.assertEquals(originalXml.children[1].finalChildren[2].value, generatedXml.children[1].finalChildren[2].value)

        Assertions.assertEquals(originalXml.children[1].children[10].finalChildren[4].name, generatedXml.children[1].children[10].finalChildren[4].name)
        Assertions.assertEquals(originalXml.children[1].children[10].finalChildren[4].value, generatedXml.children[1].children[10].finalChildren[4].value)

        Assertions.assertEquals(originalXml.children[1].children[20].finalChildren[6].name, generatedXml.children[1].children[20].finalChildren[6].name)
        Assertions.assertEquals(originalXml.children[1].children[20].finalChildren[6].value, generatedXml.children[1].children[20].finalChildren[6].value)

        file.delete()
    }

    @Test
    fun testBooksXmlFileGenericSerialization() {
        val originalXml = xmlSuperParser.parser.loadFileToParser("./src/test/resources/Books.xml").startGenericParsing()
        val file = xmlSuperParser.serializer.setFilePath("./src/test/resources/testBooks.xml").startGenericXmlSerializationToFile(originalXml, Charsets.ISO_8859_1, true)
        val generatedXml = xmlSuperParser.parser.loadFileToParser("./src/test/resources/testBooks.xml").startGenericParsing()

        Assertions.assertEquals(originalXml.name, generatedXml.name)

        Assertions.assertEquals(originalXml.children[0].properties["id"], generatedXml.children[0].properties["id"])
        Assertions.assertEquals(originalXml.children[1].finalChildren[2].value, generatedXml.children[1].finalChildren[2].value)

        Assertions.assertEquals(originalXml.children[11].properties["id"], generatedXml.children[11].properties["id"])
        Assertions.assertEquals(originalXml.children[11].finalChildren[2].value, generatedXml.children[11].finalChildren[2].value)

        file.delete()
    }

    @Test
    fun testFlightTaxXmlFileGenericSerialization() {
        val originalXml = xmlSuperParser.parser.loadFileToParser("./src/test/resources/FlightTax.xml").startGenericParsing()
        val file = xmlSuperParser.serializer.setFilePath("./src/test/resources/testFlightTax.xml").startGenericXmlSerializationToFile(originalXml, Charsets.ISO_8859_1, true)
        val generatedXml = xmlSuperParser.parser.loadFileToParser("./src/test/resources/testFlightTax.xml").startGenericParsing()

        Assertions.assertEquals(originalXml.name, generatedXml.name)
        Assertions.assertEquals(originalXml.children[0].finalChildren[0].value, generatedXml.children[0].finalChildren[0].value)

        Assertions.assertEquals(originalXml.children[0].children[0].finalChildren[0].value, generatedXml.children[0].children[0].finalChildren[0].value)
        Assertions.assertEquals(originalXml.children[0].children[0].finalChildren[10].value, generatedXml.children[0].children[0].finalChildren[10].value)

        Assertions.assertEquals(originalXml.children[0].children[1].finalChildren[1].value, generatedXml.children[0].children[1].finalChildren[1].value)
        Assertions.assertEquals(originalXml.children[0].children[1].finalChildren[9].value, generatedXml.children[0].children[1].finalChildren[9].value)

        file.delete()
    }
}