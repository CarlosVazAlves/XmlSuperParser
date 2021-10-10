class XmlElement {
    var name: String? = null
    var parent: XmlElement? = null
    var children: MutableList<XmlElement> = mutableListOf()
    var finalChildren: MutableList<XmlFinalElement> = mutableListOf()
    var properties: MutableMap<String, String> = mutableMapOf()
}

data class XmlFinalElement(val name: String, val value: String, val parent: XmlElement)