import com.sun.org.apache.xml.internal.security.encryption.XMLCipher;
import com.sun.org.apache.xml.internal.security.utils.EncryptionConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class EncryptionDecryption {


    public static void main(String[] args) throws Exception {
        com.sun.org.apache.xml.internal.security.Init.init();
        byte[] key = ("i love stackoverflow").getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-512");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Document document = getDocument("classes.xml");

        Document encryptedDoc = encryptDocument(document, secretKeySpec,
                XMLCipher.AES_256);
        saveDocumentTo(encryptedDoc, "encrypted.xml");

        encryptedDoc = getDocument("encrypted.xml");

        Document decryptedDoc = decryptDocument(encryptedDoc,
                secretKeySpec, XMLCipher.AES_256);
        saveDocumentTo(decryptedDoc, "decrypted.xml");

    }

    public static void saveSecretKey(SecretKey secretKey, String fileName) {
        byte[] keyBytes = secretKey.getEncoded();
        File keyFile = new File(fileName);
        FileOutputStream fOutStream = null;
        try {
            fOutStream = new FileOutputStream(keyFile);
            fOutStream.write(keyBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fOutStream != null) {
                try {
                    fOutStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String keyToString(SecretKey secretKey) {
  /* Get key in encoding format */
        byte encoded[] = secretKey.getEncoded();

  /*
   * Encodes the specified byte array into a String using Base64 encoding
   * scheme
   */
        String encodedKey = Base64.getEncoder().encodeToString(encoded);

        return encodedKey;
    }

    public static SecretKey getSecretKey(String algorithm) {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyGenerator.generateKey();
    }

    public static Document getDocument(String xmlFile) throws Exception {
         /* Get the instance of BuilderFactory class. */
        DocumentBuilderFactory builder = DocumentBuilderFactory.newInstance();

        /* Instantiate DocumentBuilder object. */
        DocumentBuilder docBuilder = builder.newDocumentBuilder();

        /* Get the Document object */
        Document document = docBuilder.parse(xmlFile);
        return document;
    }

    public static void saveDocumentTo(Document document, String fileName)
            throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(fileName));
        transformer.transform(source, result);
    }

    public static Document encryptDocument(Document document, SecretKey secretKey, String algorithm) throws Exception {
        /* Get Document root element */
        Element rootElement = document.getDocumentElement();
        String algorithmURI = algorithm;
        XMLCipher xmlCipher = XMLCipher.getInstance(algorithmURI);

        /* Initialize cipher with given secret key and operational mode */
        xmlCipher.init(XMLCipher.ENCRYPT_MODE, secretKey);

        /* Process the contents of document */
        xmlCipher.doFinal(document, rootElement, true);
        return document;
    }

    public static Document decryptDocument(Document document, SecretKey secretKey, String algorithm) throws Exception {
        Element encryptedDataElement = (Element) document.
                getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS, EncryptionConstants._TAG_ENCRYPTEDDATA).item(0);
        XMLCipher xmlCipher = XMLCipher.getInstance();
        xmlCipher.init(XMLCipher.DECRYPT_MODE, secretKey);
        xmlCipher.doFinal(document, encryptedDataElement);
        return document;
    }

}
