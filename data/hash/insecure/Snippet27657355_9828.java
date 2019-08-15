import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;

import java.security.*;
import java.security.cert.CertificateException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main
{   
    public static void main(String[] args) throws IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, CertificateException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SignatureException{
        //open the file containing keys
        File file = new File("keys/ks_file.jks");
        //cipher object that will hold the information
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //create keystore object from stored keys inside the file
        KeyStore keystore = loadKeyStore(file, "sergey", "JKS");
        //messageDigest instance
        MessageDigest md = MessageDigest.getInstance("SHA1");
        //singanture instance
        Signature dsa = Signature.getInstance("SHA1withDSA"); 

        //params for getting keys
        String allias = "enc_key", password = "sergey";
        SecureRandom s_random = SecureRandom.getInstance("SHA1PRNG");
        //create random bytes for semtric key
        byte key_bytes[] = new byte[16];
        s_random.setSeed(711);
        s_random.nextBytes(key_bytes);

        Key key = new SecretKeySpec(key_bytes, "AES");

        Key key_enc = keystore.getKey(allias, password.toCharArray());
        KeyPair enc_key = null;

        if (key_enc instanceof PrivateKey) {
            // Get certificate of public key
            java.security.cert.Certificate cert = keystore.getCertificate(allias);
            // Get public key
            PublicKey publicKey = cert.getPublicKey();
            enc_key = new KeyPair(publicKey, (PrivateKey) key_enc);
        }
        //cipher the file
        aes.init(Cipher.ENCRYPT_MODE, key);
        FileInputStream fis; 
        FileOutputStream fos; 
        CipherInputStream cis; 
        fis = new FileInputStream("tmp/a.txt"); 
        cis = new CipherInputStream(fis, aes);
        fos = new FileOutputStream("tmp/b.txt"); 
        byte[] b = new byte[8]; 
        int i = cis.read(b); 
        byte[] bytes = ByteBuffer.allocate(4).putInt(i).array();
        //update message digest for signature
        md.update(bytes);
        while (i != -1) { 
            fos.write(b, 0, i); 
            i = cis.read(b);
            bytes = ByteBuffer.allocate(4).putInt(i).array();
            md.update(bytes);
        } 
        fis.close();
        cis.close();
        fos.close();

        //encode the secret key
        aes.init(Cipher.ENCRYPT_MODE, (Key)enc_key.getPublic());
        byte[] cipherKey = aes.doFinal(key.toString().getBytes());

        //we save the final digest
        byte[] hash = md.digest();
        //init singature with private key
        dsa.initSign(enc_key.getPrivate());
        //update the signature with the hash aster digest
        dsa.update(hash); 
        //final signature
        byte[] sig = dsa.sign();

        //creating config xml
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("config");
            doc.appendChild(rootElement);

            // signature elements
            Element sig_xml = doc.createElement("sig");
            rootElement.appendChild(sig_xml);
            sig_xml.setAttribute("value", sig.toString());

            // key element
            Element key_xml = doc.createElement("key");
            rootElement.appendChild(key_xml);
            key_xml.setAttribute("value", cipherKey.toString());

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("./config.xml"));

            transformer.transform(source, result);

            System.out.println("File saved!");

          } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
          } catch (TransformerException tfe) {
            tfe.printStackTrace();
          }
        }

    /**
     * Reads a Java keystore from a file.
     * 
     * @param keystoreFile
     *          keystore file to read
     * @param password
     *          password for the keystore file
     * @param keyStoreType
     *          type of keystore, e.g., JKS or PKCS12
     * @return the keystore object
     * @throws KeyStoreException
     *           if the type of KeyStore could not be created
     * @throws IOException
     *           if the keystore could not be loaded
     * @throws NoSuchAlgorithmException
     *           if the algorithm used to check the integrity of the keystore
     *           cannot be found
     * @throws CertificateException
     *           if any of the certificates in the keystore could not be loaded
     */
    public static KeyStore loadKeyStore(final File keystoreFile,
        final String password, final String keyStoreType)
        throws KeyStoreException, IOException, NoSuchAlgorithmException,
        CertificateException {
      if (null == keystoreFile) {
        throw new IllegalArgumentException("Keystore url may not be null");
      }
      final URI keystoreUri = keystoreFile.toURI();
      final URL keystoreUrl = keystoreUri.toURL();
      final KeyStore keystore = KeyStore.getInstance(keyStoreType);
      InputStream is = null;
      try {
        is = keystoreUrl.openStream();
        keystore.load(is, null == password ? null : password.toCharArray());
      } finally {
        if (null != is) {
          is.close();
        }
      }
      return keystore;
    }

}
