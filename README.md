# XmlSuperParser

This project was born because I could not find a XML parser library that was easy to use.
Just upload the file and get the result free from hassle.
It started with the single purpose of read a XML file and parse it to a generic object.
If several people show interest, I'm considering an expansion to further add some more functionality.<br><br>

This is what I have in mind:
- Reads a XML file and returns a generic XmlElement - Done
- Reads a XML file and returns an instance of an object specified by the user - Done
- Converts a generic XmlElement to a XML file - Done
- Converts an instance of an object specified by the user to a XML file
- Use parallel parsing to increase performance in large files (still wondering if I want to fight such battle....)<br><br>

If something is not working right, send me the file that caused the problem.<br>
If you have any suggestions or detected any kind of problems, feel free to report.<br><br><br>

## Generic Parsing<br>

### XmlElement and XmlFinalElement<br>

![image](https://user-images.githubusercontent.com/55634585/136702427-1d661b11-0f8d-44a4-9589-3f91dc267f74.png)<br>

In this example, "Catalog" is an XmlElement with the following characteristics:
- name is "Catalog"
- no parent
- has 2 children, both named "book"
- no final children
- no properties<br><br>

Picking one of the children "book", which is also an XmlElement, with the following characteristics:
- name is "book"
- parent is "Catalog"
- has no children
- has 6 final children (author, title, genre, price, publish_date, description>)
- has 1 property, with key "id" and value "bk101"<br><br>

As an example of a Final Children, we can use an "Author", with the following characteristics:
- name is "Author"
- value is "Gambardella, Matthew"
- parent is the book with "id = bk101"<br><br><br>

## Specific Parsing<br>

Now let's pay close attention how to use the specific parsing.<br>
Bear in mind that this feature heavily relies on reflection!<br>
So, the name of classes, variables and methods must be carefully chosen!<br>
This library will always be looking for the setter of a property / field.<br>
So, the name of the setter must match the XML tag.<br><br>

Examples:<br>

For the tag \<genre\>, this library will be looking for a setter named "setGenre"<br>
For the tag \<GENRE\>, this library will also be looking for a setter named "setGenre"<br>
  
For the tag \<publish_date\>, this library will be looking for a setter named "setPublishDate"<br>
For the tag \<publishDate\>, this library will also be looking for a setter named "setPublishDate"<br>
For the tag \<PUBLISH_DATE\>, this library will also be looking for a setter named "setPublishDate"<br><br>


Let's consider the same XML as before:

![image](https://user-images.githubusercontent.com/55634585/139146657-05673fbc-8ec5-4613-bc90-85d5b29306d5.png)<br><br>

A class settled in Kotlin should look like this, considering that the setters are implied:<br>

![image](https://user-images.githubusercontent.com/55634585/139148176-26cd65f0-7f19-46b1-aa6d-99405f62e267.png)<br><br>

Using Java, the setters must be defined by the programmer. In this situation, the variable name is irrelevant, only the setter is important:<br>

![image](https://user-images.githubusercontent.com/55634585/139148855-958f5013-6653-492c-90ab-5f5c2e75a46e.png)<br><br>

In case of a setter was not properly named, an exception will be thrown specifying which setter was not found, for easier debugging.<br><br><br>

## Generic Serialization<br>

Generic serialization is pretty self explanatory. It converts a XmlElement into a XML file.<br>
User must specify the path and file name, as well as the charset intended for the XML file.<br>
It is also possible to choose if the XML file will have indentation or not.<br>


