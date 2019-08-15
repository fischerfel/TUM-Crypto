/*
** Generates the HMRC IRMARK as required.
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

import org.apache.xml.security.Init;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.bouncycastle.util.encoders.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class IRMarkDOS
{
    /**
     * @param args
     */
    public static void main(String[] args) 
    {

        // Initialise Apache XML tools
        Init.init();

        // Start tracking execution time
        long start = System.currentTimeMillis();

        try
        {

            // Validate/parse the command line
            if (args.length != 3)
            {
                System.out.println("INCORRECT PARAMETERS SPECIFIED" + System.getProperty("line.separator") + "  Specify IRMark.exe <InputFile> <OutputFile> " + "<TaxNamespace>");
                return;
            }

            // Set input/output variables
            String sInput = args[0];
            String sOutput = args[1];
            String sTaxNamespace = args[2];


            // Read the XML Document
            //Document xmlDoc = IRMarkDOS.processXML(sInput, sTaxNamespace);
            String xml = IRMarkDOS.processXML(sInput, sTaxNamespace);
            System.gc();


            // Generate the IRMark
            String strIRMark = IRMarkDOS.generateIRMark(xml);

            // Write to file
            PrintWriter out = new PrintWriter(new FileOutputStream(sOutput));
            out.println(strIRMark);
            out.close();
            System.out.println("IRmark64: " + strIRMark);

            // Output execution time
            long end = System.currentTimeMillis();          
            System.out.println("Execution Time " + ((end-start) / 1000) + " seconds, " + (end-start) + " milliseconds");


        }
        catch (RuntimeException ex)
        {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        catch (OutOfMemoryError ex) 
        {
            System.out.println(ex.getMessage());
            System.exit(1);         
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 
     * Processes the specified XML document
     * 
     * @param sInput             - XML Document
     * @param sTaxNamespace      - TaxNamespace
     * @param sEnvelopeNamespace - EnvelopeNamespace
     * @return
     */
    private static String processXML (String sInput, String sTaxNamespace)
    {

        try
        {
            // Read XML
            File xmlDocument=new File(sInput);
            DocumentBuilderFactory xmlDomFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = xmlDomFactory.newDocumentBuilder();
            Document xmlDoc = xmlBuilder.parse(xmlDocument);                    

            // Setup XPath          
            XPathFactory factory=XPathFactory.newInstance();
            XPath xPath=factory.newXPath();     

            Node body = (Node) xPath.evaluate("/GovTalkMessage/Body", xmlDoc, XPathConstants.NODE);

            //Get IRMark and remove if exists
            Node irmark = (Node) xPath.evaluate("/GovTalkMessage/Body/IRenvelope/IRheader/IRmark", xmlDoc, XPathConstants.NODE);

            if (irmark != null)
            {
                System.out.println("Original IRMark: " + irmark.getTextContent());
                irmark.getParentNode().removeChild(irmark);             
            }
            irmark = null;


            // Create new doc for body and add envelope namespace to body as required
            xmlDoc = null;
            xmlDoc = xmlBuilder.newDocument();

            Node tmp = xmlDoc.importNode(body, true);
            xmlDoc.appendChild(tmp);
            tmp = null;

            // Add namespace to body element        
            xmlDoc.getDocumentElement().setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.govtalk.gov.uk/CM/envelope");

            return IRMarkDOS.getOuterXml(xmlDoc);

        }
        catch (RuntimeException ex)
        {
            System.out.println(ex.getMessage());
            System.exit(1);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Generates the IRMark for the specified XML Document
     * 
     * @param xmlDoc - XML Document to generate the IRMark for
     * @return - The generated IRMark
     */
    private static String generateIRMark(String bodyText1)
    {
        // Get XML string
        //String bodyText1 = IRMarkDOS.getOuterXml(xmlDoc);

        // Final Data Tweaks
        bodyText1 = bodyText1.toString();
        bodyText1 = bodyText1.replace("&#xD;", "");
        bodyText1 = bodyText1.replace("\r\n", "\n");
        bodyText1 = bodyText1.replace("\r", "\n");

        try 
        {
            // Convert the final document back into a byte array encoded as UTF8
            byte[] bodyBytes = bodyText1.getBytes("UTF8");

            // Canonicalisation to C14n         
            Canonicalizer c14n = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
            byte[] bodyCanonical = c14n.canonicalize(bodyBytes);

            // Generate SHA 1 and convert to Base64
            MessageDigest md1 = MessageDigest.getInstance("SHA");           
            md1.update(bodyCanonical); //bodyBytes
            byte[] digest1 = md1.digest();         

            String strIRmark = new String(Base64.encode(digest1));
            return strIRmark;
        }
        catch (RuntimeException ex)
        {
            System.out.println(ex.getMessage());
            System.exit(1);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidCanonicalizerException e) {
            e.printStackTrace();
        } catch (CanonicalizationException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }       
        return "";
    }


    /**
     * Converts a XML Node to a string representation
     * 
     * @param node - XML Node to convert to String
     * @return - A string representation of the XML Node
     */
    private static String getOuterXml(Node node)
    {
        try
        {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            return writer.toString();  
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return "";

    }

}
