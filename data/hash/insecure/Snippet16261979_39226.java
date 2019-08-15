import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

public class ComputeXMLStructureHash {

    public static void main(String[] args)
    {
        try {
            FileInputStream in1 = new FileInputStream(new File("file1.xml"));
            FileInputStream in2 = new FileInputStream(new File("file2.xml"));
            FileInputStream in3 = new FileInputStream(new File("file3.xml"));

            System.out.println(digest(in1));
            System.out.println(digest(in2));
            System.out.println(digest(in3));                        

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String digest(InputStream in) {
        MessageDigest messageDigest = null;

        // StAX for XML parsing
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            // Iterate over the XML elements and update hash
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    messageDigest.update(event.asStartElement().getName().toString().getBytes());                   
                } else if (event.isEndElement()) {
                    messageDigest.update(event.asEndElement().getName().toString().getBytes());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuffer result = new StringBuffer();
        byte[] digest = messageDigest.digest();
        for (byte b : digest) 
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

        return result.toString(); 
    }
}
