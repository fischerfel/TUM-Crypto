@WebService
public class Test {

// Keystore containing multiple certificates I am using to sign xml which I send to target web service
private static final String KEY_STORE_TYPE = "JKS";
private static final String KEY_STORE_FILE = "C:\\Certificates\\mycert.jks";
private static final String KEY_STORE_PASS = "mypass";

// Keystore which I use to validate the signed message I receive from target web service
private static final String TARGET_KEY_STORE_TYPE = "JKS";
private static final String TARGET_KEY_STORE_FILE = "C:\\Certificates\\othercert.jks";
private static final String TARGET_KEY_STORE_PASS = "otherpass";
private static final String TARGET_KEY_ALIAS = "othercert";

// Keystore which I use for one way ssl handshake when connecting to target web service
private static final String TRUSTED_STORE_TYPE = "JKS";
private static final String TRUSTED_STORE_FILE = "C:\\Certificates\\truststore.jks";
private static final String TRUSTED_STORE_PASS = "trustpass";

// Target web service which I'm connecting to
private static final String SERVICE_URL = "https://service.i-am.using:1111/AnotherWebService";

// Attribute used in signing xml
private static final String ID = "testId";

// Method which user calls to sign and send his message to target web service which returns signed reply with data. Since there are multiple certificates (for multiple users) in keystore user must supply certificate alias and password
@WebMethod
public String getData(String user_message, String alias, String passwd) {
    String reply = "Unexpected error";
    SOAPMessage message = null;
    try {
        message = createSoapMessage(user_message);
        message = signMessage(message, alias, passwd);
        message = sendSoapMessage(message);
        Boolean value = false;
        value = verifyMessage(message);
        if (value == false) throw new Exception ("Message received from target web service is not correctly signed!");
        reply = stringFromSoap(message);
    } catch (Exception e) {
            reply = "Error! "+e.toString();
    }
    return reply;
}

// Method for testing whether target web service is available
@WebMethod
public String testResponse(){
    String reply = "Unexpected error";
    SOAPMessage response = null;
    try {
        response = createEchoMessage();
        // THIS CALL DOESN'T WORK WHEN EVEN WHEN I ADD KEYSTORE TO WEBLOGIC KEYSTORE CONFIGURATION
        response = sendSoapMessage(response);
        reply = stringFromSoap(response);
    } catch (Exception e) {
        reply = "Error! "+e.toString();
    }
    return reply;
}

// Method for creating Soap message used in testResponse method. Implementation is not important, here it is only as a signature.
private SOAPMessage createEchoMessage();

// Method for creating Soap message from string. Implementation is not important, here it is only as a signature.
private SOAPMessage createSoapMessage(String input_msg);

// Method for creating string from Soap message. Implementation is not important, here it is only as a signature.
private String stringFromSoap(SOAPMessage message);

// Method that sends Soap message to target server and receives reply
private SOAPMessage sendSoapMessage(SOAPMessage message) throws Exception {

    // Key used for one way ssl
    KeyStore keyStore = KeyStore.getInstance(TRUSTED_STORE_TYPE);
    keyStore.load(
        new FileInputStream(TRUSTED_STORE_FILE),  
        TRUSTED_STORE_PASS.toCharArray()
    );

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
    tmf.init(keyStore);

    SSLContext sslctx = SSLContext.getInstance("SSL");
    sslctx.init(null, tmf.getTrustManagers(), null);

    HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());

    SOAPConnectionFactory sfc = SOAPConnectionFactory.newInstance();
    SOAPConnection connection = sfc.createConnection();

    SOAPMessage response = null;

    try{
        URL endpoint =
                  new URL(new URL(SERVICE_URL),
                          "",
                          new URLStreamHandler() {
                            @Override
                            protected URLConnection openConnection(URL url) {
                                URLConnection dummy = null;
                                try{
                                    URL target = new URL(url.toString());
                                    URLConnection connection = target.openConnection();
                                    // Timeout settings
                                    connection.setConnectTimeout(1000); // 1 sec
                                    connection.setReadTimeout(5000); // 5 sec
                                    return(connection);
                                }
                                catch (Exception e){
                                    return dummy;
                                }
                            }
                          });
        response = connection.call(message, endpoint);
    }
    catch (Exception e){
        throw e;
    }
    connection.close();
    return response;
}

// Method for signing xml
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
private static SOAPMessage signMessage(SOAPMessage message, String alias, String passwd) throws Exception {

    // Read SOAP message
    SOAPMessage doc = message;
    SOAPPart soapPart = doc.getSOAPPart();

    // Find id for signing
    Node nodeToSign = null;
    Node sigParent = null;
    String referenceURI = null;
    XPathExpression expr = null; 
    NodeList nodes;
    List transforms = null;

    XPathFactory factory = XPathFactory.newInstance();
    XPath xpath = factory.newXPath();

    expr = xpath.compile(
            String.format("//*[@Id='%s']", ID)
        );
        nodes = (NodeList) expr.evaluate(soapPart, XPathConstants.NODESET);
        if (nodes.getLength() == 0) {
            throw new Exception("No node with id: " + ID);
        }

    nodeToSign = nodes.item(0);
    sigParent = nodeToSign;
    referenceURI = "#" + ID;


    // Prepare signature
    String providerName = System.getProperty(
            "jsr105Provider", 
            "org.jcp.xml.dsig.internal.dom.XMLDSigRI"
            );

    final XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance(
                        "DOM",
                        (Provider) Class.forName(providerName).newInstance()
                     );

    // Transformations used for signing
    transforms = new ArrayList<Transform>(){{
        add(sigFactory.newTransform(
                Transform.ENVELOPED, 
                (TransformParameterSpec) null )

        );
        add(sigFactory.newTransform(CanonicalizationMethod.EXCLUSIVE, (TransformParameterSpec) null
        )
        );
    }};

    // Get key for signing
    KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
    keyStore.load(
        new FileInputStream(KEY_STORE_FILE),  
        KEY_STORE_PASS.toCharArray()
    );

    PrivateKey privateKey = (PrivateKey) keyStore.getKey(
                    alias,
                    passwd.toCharArray()
                );

    X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);

    // Create a reference to enveloped document
    Reference ref = sigFactory.newReference(
            referenceURI,
            sigFactory.newDigestMethod(DigestMethod.SHA1, null),
            transforms,
            null, 
            null
            );

    // Create SignedInfo
    SignedInfo signedInfo = sigFactory.newSignedInfo(
                    sigFactory.newCanonicalizationMethod(
                    CanonicalizationMethod.EXCLUSIVE, 
                    (C14NMethodParameterSpec) null
                    ), 
                    sigFactory.newSignatureMethod(
                    SignatureMethod.RSA_SHA1, 
                    null
                    ),
                    Collections.singletonList(ref)
                );

    // Create KeyInfo
    KeyInfoFactory keyinfoFactory = sigFactory.getKeyInfoFactory(); 

    X509IssuerSerial issuer= keyinfoFactory.newX509IssuerSerial(cert.getIssuerX500Principal().getName(), cert.getSerialNumber());

    List x509Content = new ArrayList();
    x509Content.add(cert);
    x509Content.add(cert.getSubjectX500Principal().getName());
    x509Content.add(issuer);
    X509Data xd = keyinfoFactory.newX509Data(x509Content);
    KeyInfo keyInfo = keyinfoFactory.newKeyInfo(Collections.singletonList(xd));

    // Create DOMSignContext
    DOMSignContext dsc = new DOMSignContext(
                 privateKey, 
                 sigParent
                 );
            System.out.println(dsc.toString());

    // Create XMLSignature
    XMLSignature signature = sigFactory.newXMLSignature(signedInfo, keyInfo);

    // Generate and sign - THIS IS WHERE THE PROGRAM FAILS WHEN I CONVERT IT TO WEBLOGIC WEBSERVICE
    signature.sign(dsc);

    // Save changes on Soap message
    doc.saveChanges();

    return doc;
}

// Method used to verify message received from targer server
private Boolean verifyMessage(SOAPMessage message) throws Exception{

    Boolean verified = false;

    // Create xml from Soap message
    SOAPMessage msg = message;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    msg.writeTo(out);

    XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(out.toByteArray()));

    // Check whether Signature element exists in XML message
    NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS,"Signature");
    if (nl.getLength() == 0) {
        throw new Exception("Signature element doesn't exist!");
    }

    // Get key which will be used for verification
    KeyStore keyStore = KeyStore.getInstance(TARGET_KEY_STORE_TYPE);
    keyStore.load(
        new FileInputStream(TARGET_KEY_STORE_FILE),  
        TARGET_KEY_STORE_PASS.toCharArray()
    );

    X509Certificate cert = (X509Certificate) keyStore.getCertificate(TARGET_KEY_ALIAS);
    PublicKey publicKey = cert.getPublicKey();

    // Create validation context and signature
    DOMValidateContext valContext = new DOMValidateContext(publicKey, nl.item(0));
    XMLSignature signature = fac.unmarshalXMLSignature(valContext);

    // Check whether the document is properly signed
    verified = signature.validate(valContext);

    return verified;
}
}
