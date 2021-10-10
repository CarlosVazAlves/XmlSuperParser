# XmlSuperParser

This project was born because I could not find a XML parser library that was easy to use.
Just upload the file and get the result free from hassle.
It started with the single purpose of read a XML file and parse it to a generic object.
If several people show interest, I'm considering an expansion to further add some more functionality.<br><br>

This is what I have in mind:
- Reads a XML file and returns a generic XmlElement - Done
- Reads a XML file and returns an instance of an object specified by the user
- Converts a generic XmlElement to a XML file
- Converts an instance of an object specified by the user to a XML file
- Use parallel parsing to increase performance in large files (still wondering if I want to fight such battle....)<br><br>

If something is not working right, send me the file that caused the problem.<br>
If you have any suggestions or detected any kind of problems, feel free to report.<br><br>

## XmlElement and XmlFinalElement<br>

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
- parent is the book with "id = bk101"
