package genericConversion

import XmlElement
import XmlFinalElement

class GenericParser(private val xmlToStringList: List<String>) {

    private val tagsCurrentlyOpen: ArrayList<String> = ArrayList()

    /**
     * Will parse the XML file loaded into a generic DOM (returns a XmlElement)
     *
     * @return XmlElement - Populated XmlElement
     */
    fun getXmlGenericDom(): XmlElement {
        val xmlStringList = xmlToStringList.toMutableList()
        return parseToGenericXmlElement(xmlStringList, XmlElement())
    }

    /**
     * Starts parsing the XmlStringList line by line recursively
     *
     * @param xmlStringList - XML file converted to a String List
     * @param currentXmlElement - currently being populated XmlElement
     * @return XmlElement - Same XmlElement received as parameter but with extra data from XML file
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
     * Parses an Opening genericConversion Tag
     * ex: <CLIENTE id="514408852"> with parameters
     * ex: <IDENTIFICADOR> without parameters
     *
     * @param tag - XML line from List converted into Tag type
     * @param xmlStringList - XML file converted to a String List
     * @param currentXmlElement - currently being populated XmlElement
     * @param hasParameters - Boolean to identify if is as opening tag with or without parameters
     * @return XmlElement - Same XmlElement received as parameter but with extra data from XML file
     */
    private fun parseOpeningTag(tag: Tag, xmlStringList: MutableList<String>, currentXmlElement: XmlElement, hasParameters: Boolean): XmlElement {
        val name = tag.name
        currentXmlElement.name = name

        tagsCurrentlyOpen.add(name)

        if (hasParameters) {
            currentXmlElement.properties = (tag.parameters as MutableMap<String, String>?)!!
        }

        xmlStringList.removeAt(0)
        val xmlElementToContinue: XmlElement

        val nextTagLine = Tag(xmlStringList[0])

        when (nextTagLine.tagType) {
            TagType.OPENING_TAG_WITH_PARAMETERS, TagType.OPENING_TAG_WITHOUT_PARAMETERS -> {
                val newXmlElement = XmlElement()
                currentXmlElement.children.add(newXmlElement)
                newXmlElement.parent = currentXmlElement
                xmlElementToContinue = newXmlElement
            }
            TagType.CLOSING_TAG -> {
                xmlElementToContinue = currentXmlElement.parent!!
            }
            TagType.OPENING_AND_CLOSING_TAG -> {
                xmlElementToContinue = currentXmlElement
            }
        }

        return parseToGenericXmlElement(xmlStringList, xmlElementToContinue)
    }

    /**
     * Parses an Opening and Closing genericConversion Tag
     * <SAIDA>Torres Novas</SAIDA>
     *
     * @param tag - XML line from List converted into Tag type
     * @param xmlStringList - XML file converted to a String List
     * @param currentXmlElement - currently being populated XmlElement
     * @return XmlElement - Same XmlElement received as parameter but with extra data from XML file
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
     * Parses a Closing genericConversion Tag
     * </TRANSACCAO>
     *
     * @param tag - XML line from List converted into Tag type
     * @param xmlStringList - XML file converted to a String List
     * @param currentXmlElement - currently being populated XmlElement
     * @return XmlElement - Same XmlElement received as parameter but with extra data from XML file
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
}