package assignment_2

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import javax.xml.parsers.DocumentBuilderFactory

/**
 * I referred to code from this webpage https://turreta.com/2017/07/07/how-to-read-xml-in-kotlin-using-dom-parser/
 */

class XmlParser {
    fun downloadFile(){
        val website: URL = URL("https://www.w3schools.com/xml/books.xml")
        val copyPath = Paths.get("books.xml")
        website.openStream().use { `in` -> Files.copy(`in`, copyPath, StandardCopyOption.REPLACE_EXISTING) }
    }

    fun parseFile(){
        var price = 0.0
        val xmlFile = File("books.xml")
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val xmlInput = InputSource(StringReader(xmlFile.readText()))
        val doc = dBuilder.parse(xmlInput)

        doc.documentElement.normalize()

        val bookList: NodeList = doc.getElementsByTagName("book")


        for (i in 0 until bookList.length){
            var bookNode: Node = bookList.item(i)
            if (bookNode.getNodeType() === Node.ELEMENT_NODE) {
                val elem = bookNode as Element
                price += elem.getElementsByTagName("price").item(0).textContent.toDouble()
            }
        }


        print("Thare are "+bookList.length+" books in the bookstore with total price of "+price)

    }
}

fun main(){
    val xmlParser = XmlParser()
    xmlParser.downloadFile()
    xmlParser.parseFile()
}