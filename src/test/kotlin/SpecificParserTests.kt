import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SpecificParserTests {

    @Test
    fun testTollXmlFile() {
        val xml = XmlSuperParser("./src/test/resources/Toll.xml").startSpecificParsing(Extracto::class.java)

        Assertions.assertEquals("015101413/09/2021", xml.id)

        Assertions.assertEquals("20210930", xml.mesEmissao)
        Assertions.assertEquals("10,43", xml.totalIva)

        Assertions.assertEquals("123654987", (xml.detalhes[0] as Cliente).nif)
        Assertions.assertEquals("SUPER MARIO", (xml.detalhes[0] as Cliente).nome)

        Assertions.assertEquals("58,35", (xml.detalhes[1] as Identificador).total)

        Assertions.assertEquals("21:26", (xml.detalhes[1] as Identificador).transacoes[10].horaSaida)

        Assertions.assertEquals("1,85", (xml.detalhes[1] as Identificador).transacoes[20].importancia)

    }

    @Test
    fun testBooksXmlFile() {
        val xml = XmlSuperParser("./src/test/resources/Books.xml").startSpecificParsing(Catalog::class.java)

        Assertions.assertEquals(12, xml.books.count())

        Assertions.assertEquals("bk101", xml.books[0].id)
        Assertions.assertEquals("Fantasy", xml.books[1].genre)

        Assertions.assertEquals("36.95", xml.books[10].price)
        Assertions.assertEquals("2001-04-16", xml.books[11].publishDate)

    }

    @Test
    fun testFlightTaxXmlFile() {
        val xml = XmlSuperParser("./src/test/resources/FlightTax.xml").startSpecificParsing(Movimentos::class.java)

        Assertions.assertEquals("2021", xml.documentos[0].ano)
        Assertions.assertEquals("Robert Bush", xml.documentos[0].operador)

        Assertions.assertEquals("2021028741", xml.documentos[0].movimentos[0].numMov)
        Assertions.assertEquals("9.1800", xml.documentos[0].movimentos[0].total)

        Assertions.assertEquals("3471", xml.documentos[0].movimentos[1].numVoo)
        Assertions.assertEquals("1.5300", xml.documentos[0].movimentos[1].taxa)

    }
}