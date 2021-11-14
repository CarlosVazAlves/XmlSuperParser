import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GenericParserTests {

    private val xmlSuperParser = XmlSuperParser()

    @Test
    fun testTollXmlFileGenericParsing() {
        val xml = xmlSuperParser.parser.loadFileToParser("./src/test/resources/Toll.xml").startGenericParsing()

        Assertions.assertEquals("1.0", xmlSuperParser.parser.getXmlVersion())
        Assertions.assertEquals(Charsets.ISO_8859_1.name(), xmlSuperParser.parser.getXmlEncoding())

        Assertions.assertEquals("EXTRACTO", xml.name)

        Assertions.assertEquals("MES_EMISSAO", xml.finalChildren[0].name)
        Assertions.assertEquals("20210930", xml.finalChildren[0].value)
        Assertions.assertEquals("TOTAL_IVA", xml.finalChildren[2].name)
        Assertions.assertEquals("10,43", xml.finalChildren[2].value)

        Assertions.assertEquals("CLIENTE", xml.children[0].name)
        Assertions.assertEquals("IDENTIFICADOR", xml.children[1].name)

        Assertions.assertEquals("NOME", xml.children[0].finalChildren[1].name)
        Assertions.assertEquals("SUPER MARIO", xml.children[0].finalChildren[1].value)

        Assertions.assertEquals("TOTAL", xml.children[1].finalChildren[2].name)
        Assertions.assertEquals("58,35", xml.children[1].finalChildren[2].value)

        Assertions.assertEquals("HORA_SAIDA", xml.children[1].children[10].finalChildren[4].name)
        Assertions.assertEquals("21:26", xml.children[1].children[10].finalChildren[4].value)

        Assertions.assertEquals("IMPORTANCIA", xml.children[1].children[20].finalChildren[6].name)
        Assertions.assertEquals("1,85", xml.children[1].children[20].finalChildren[6].value)

    }

    @Test
    fun testBooksXmlFileGenericParsing() {
        val xml = xmlSuperParser.parser.loadFileToParser("./src/test/resources/Books.xml").startGenericParsing()

        Assertions.assertEquals("1.0", xmlSuperParser.parser.getXmlVersion())
        Assertions.assertEquals(Charsets.ISO_8859_1.name(), xmlSuperParser.parser.getXmlEncoding())

        Assertions.assertEquals("catalog", xml.name)

        Assertions.assertEquals("bk101", xml.children[0].properties["id"])
        Assertions.assertEquals("Fantasy", xml.children[1].finalChildren[2].value)

        Assertions.assertEquals("bk112", xml.children[11].properties["id"])
        Assertions.assertEquals("Computer", xml.children[11].finalChildren[2].value)

    }

    @Test
    fun testFlightTaxXmlFileGenericParsing() {
        val xml = xmlSuperParser.parser.loadFileToParser("./src/test/resources/FlightTax.xml").startGenericParsing()

        Assertions.assertEquals("1.0", xmlSuperParser.parser.getXmlVersion())
        Assertions.assertEquals(Charsets.UTF_8.name(), xmlSuperParser.parser.getXmlEncoding())

        Assertions.assertEquals("Movimentos", xml.name)
        Assertions.assertEquals("Robert Bush", xml.children[0].finalChildren[0].value)

        Assertions.assertEquals("2021028741", xml.children[0].children[0].finalChildren[0].value)
        Assertions.assertEquals("9.1800", xml.children[0].children[0].finalChildren[10].value)

        Assertions.assertEquals("3471", xml.children[0].children[1].finalChildren[1].value)
        Assertions.assertEquals("1.5300", xml.children[0].children[1].finalChildren[9].value)
    }
}