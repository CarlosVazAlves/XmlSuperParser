package genericConversion

import XmlElement
import XmlFinalElement
import java.nio.charset.Charset

class GenericSerializer {

    private lateinit var xmlString: String
    private var indentationLevel = 0
    private var pretty: Boolean = false

    /**
     * Starts reading the xmlElement and parses each element to the global String
     *
     * @param xmlElement - xmlElement to be serialized to file
     * @param charset - charset intended for the XML file
     * @param pretty - if true, XML file will be generated with indentation
     * @return String - Returns the entire xmlElement parsed to the global String
     */
    fun serializeGenericXmlToString(xmlElement: XmlElement, charset: Charset, pretty: Boolean): String {
        this.pretty = pretty
        xmlString = String().toByteArray(charset).toString()
        generateHeader(charset)
        populateXmlString(xmlElement)
        return xmlString
    }

    /**
     * Generates the header of the file in the global String
     *
     * @param charset - charset intended for the XML file
     */
    private fun generateHeader(charset: Charset) {
        xmlString = "<?xml version=\"1.0\" encoding=\"${charset.name()}\"?>\r\n"
    }

    /**
     * Starts populating the global String recursively with the xmlElement provided
     *
     * @param xmlElement - xmlElement to be serialized to file
     */
    private fun populateXmlString(xmlElement: XmlElement) {
        val xmlTagName = xmlElement.name!!
        readProperties(xmlElement.properties, xmlTagName)
        indentationLevel++
        readFinalChildren(xmlElement.finalChildren)
        readChildren(xmlElement.children)
        addTabs(true)
        xmlString = xmlString.plus("</${xmlTagName}>\r\n")
    }

    /**
     * Parses the current xmlElement children to the global String
     *
     * @param elements - the current xmlElement children List
     */
    private fun readChildren(elements: List<XmlElement>) {
        for (element in elements) {
            populateXmlString(element)
        }
        indentationLevel--
    }

    /**
     * Parses the current xmlElement final children to the global String
     *
     * @param xmlFinalChildren - the current xmlElement final children List
     */
    private fun readFinalChildren(xmlFinalChildren: List<XmlFinalElement>) {
        var returnString = "\r\n"
        for (finalChild in xmlFinalChildren) {
            returnString = returnString.plus(addTabs(false) + "<${finalChild.name}>${finalChild.value}</${finalChild.name}>\r\n")
        }
        xmlString = xmlString.plus(returnString)
    }

    /**
     * Parses the current xmlElement properties to the global String
     *
     * @param properties - the current xmlElement properties Map<String, String>
     * @param name - the current tag name
     */
    private fun readProperties(properties: Map<String, String>, name: String) {
        var propertiesString = startOpeningTag(name)
        addTabs(true)
        for (property in properties) {
            propertiesString = propertiesString.plus("${property.key}=\"${property.value}\" ")
        }
        xmlString = xmlString.plus(finishOpeningTag(propertiesString))
    }

    /**
     * Add tabs to the global String to provide indentation
     *
     * @param addToGlobalString - if true, the tabs will be added to the global String
     * @return String - Returns a String filled only with tabs (tabs amount = indentationLevel)
     */
    private fun addTabs(addToGlobalString: Boolean): String {
        if (!pretty) return ""
        var tabs = String()
        for (level in 0 until indentationLevel) {
            tabs = tabs.plus("\t")
        }
        if (addToGlobalString) xmlString = xmlString.plus(tabs)
        return tabs
    }

    /**
     * Generates an opening tag (not closed yet)
     *
     * @param name - tag name
     * @return String - Returns a String with an opening tag (not closed yet)
     */
    private fun startOpeningTag(name: String): String {
        return "<${name} "
    }

    /**
     * Closes a previously generated opening tag
     *
     * @param tag - opening tag to be closed
     * @return String - Returns a String with an opening tag fully formed
     */
    private fun finishOpeningTag(tag: String): String {
        val tagArray = tag.toCharArray()
        tagArray[tag.lastIndex] = '>'
        return tagArray.concatToString()
    }
}