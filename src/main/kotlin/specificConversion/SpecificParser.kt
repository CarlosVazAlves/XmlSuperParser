package specificConversion

import XmlElement
import XmlFinalElement
import XmlSuperParserInvalidFileException
import java.lang.reflect.Method

class SpecificParser(private val xmlGenericDom: XmlElement) {

    /**
     * Receives a class type, creates a new instance of it,
     * and populates it with the generic conversion data
     * @param userClass - user specified type
     * @return T - new instance populated of user specified type
     */
    fun <T> getXmlSpecific(userClass: Class<T>): T {
        val genericXml = xmlGenericDom
        val userObject: T = userClass.getDeclaredConstructor().newInstance()
        startConversion(genericXml, userObject)
        return userObject
    }

    /**
     * Starts converting the genericXml to specified type by the user
     * @param genericXml - already populated generic XmlElement
     * @param specificXml - instance of user specified type to populate
     */
    private fun <T> startConversion(genericXml: XmlElement, specificXml: T) {

        if(genericXml.properties.isNotEmpty()) {
            populateProperties(genericXml.properties, specificXml)
        }
        if(genericXml.finalChildren.isNotEmpty()) {
            populateFinalChildren(genericXml.finalChildren, specificXml)
        }
        if(genericXml.children.isNotEmpty()) {
            populateChildren(genericXml.children, specificXml)
        }
    }

    /**
     * Populates properties through reflection
     * It must exist a setter with the property name, otherwise an exception will be thrown
     * @param genericXmlProperties - MutableMap<String, String> of properties
     * @param specificXml - instance of user specified type to populate
     * @throws XmlSuperParserInvalidFileException in case of absent setter
     */
    private fun <T> populateProperties(genericXmlProperties: MutableMap<String, String>, specificXml: T) {
        for (property in genericXmlProperties) {
            val key = property.key
            try {
                val setter = specificXml!!::class.java.getDeclaredMethod("set${formatXmlElementName(key)}", String::class.java)
                setter.invoke(specificXml, property.value)
            } catch (exception: NoSuchMethodException) {
                throw XmlSuperParserInvalidFileException("Could not find method \"set${formatXmlElementName(key)}\"")
            }
        }
    }

    /**
     * Populates the final children through reflection
     * It must exist a setter with the child name, otherwise an exception will be thrown
     * @param genericXmlFinalChildren - List of FinalChildren (XmlFinalElement)
     * @param specificXml - instance of user specified type to populate
     * @throws XmlSuperParserInvalidFileException in case of absent setter
     */
    private fun <T> populateFinalChildren(genericXmlFinalChildren: MutableList<XmlFinalElement>, specificXml: T) {
        for (finalChild in genericXmlFinalChildren) {
            try {
                val setter = specificXml!!::class.java.getDeclaredMethod("set${formatXmlElementName(finalChild.name)}", String::class.java)
                setter.invoke(specificXml, finalChild.value)
            } catch (exception: NoSuchMethodException) {
                throw XmlSuperParserInvalidFileException("Could not find method \"set${formatXmlElementName(finalChild.name)}\"")
            }
        }
    }

    /**
     * Populates the children list through reflection
     * It must exist a setter of a list, otherwise an exception will be thrown
     * A new conversion starts recursively to populate final children, children and properties for each child
     * @param genericXmlChildren - List of Children (XmlElement)
     * @param specificXml - instance of user specified type to populate
     * @throws XmlSuperParserInvalidFileException in case of absent setter
     */
    private fun <T> populateChildren(genericXmlChildren: MutableList<XmlElement>, specificXml: T) {
        var setChildrenListMethod: Method? = null
        val methods = specificXml!!::class.java.declaredMethods
        for (method in methods) {
            val receivesListAsParameter = method.parameterTypes.any { it.name.contains("List") }
            if (receivesListAsParameter) {
                setChildrenListMethod = method
                break
            }
        }

        if (setChildrenListMethod == null) {
            throw XmlSuperParserInvalidFileException("Found no method to set the children's list")
        }

        val childrenList = mutableListOf<Any>()

        for (child in genericXmlChildren) {
            val childrenClassName = child.name!!
            val childElement = Class.forName(formatXmlElementName(childrenClassName)).getDeclaredConstructor().newInstance()
            childrenList.add(childElement)
            startConversion(child, childElement)
        }

        setChildrenListMethod.invoke(specificXml, childrenList)
    }

    /**
     * Converts the Tag name to a normalized format (camelCase)
     * @param inputString - String to be formatted
     * @return String - New formatted String
     */
    private fun formatXmlElementName(inputString: String): String {
        val tagStyle = checkXmlTagStyle(inputString)
        return tagStyle.formatTagName(inputString)
    }

    /**
     * Identifies the current Tag name format
     * @param tag - String containing Tag to be analyzed
     * @return XmlTagStyle - The identified XmlTagStyle
     */
    private fun checkXmlTagStyle(tag: String): XmlTagStyle {
        val allUpperCase = !tag.any { it.isLowerCase() }
        val hasUnderscores = tag.any { it == '_' }

        if (allUpperCase && hasUnderscores) return XmlTagStyle.ALL_UPPER_CASE_WITH_UNDERSCORE
        if (allUpperCase && !hasUnderscores) return XmlTagStyle.ALL_UPPER_CASE_WITHOUT_UNDERSCORE

        val allLowerCase = !tag.any { it.isUpperCase() }

        if (allLowerCase && hasUnderscores) return XmlTagStyle.ALL_LOWER_CASE_WITH_UNDERSCORE
        if (allLowerCase && !hasUnderscores) return XmlTagStyle.ALL_LOWER_CASE_WITHOUT_UNDERSCORE
        if (tag.first().isUpperCase() && !hasUnderscores) return XmlTagStyle.CAMEL_CASE

        return XmlTagStyle.UNKNOWN
    }
}