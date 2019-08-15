    public class Sign {

    static String myToken = "";
    static SOAPMessage signedRequest = null;
    static SOAPEnvelope soapEnvelope = null;

    static String url = "";
    static String fileToSend = "";
    static String receiptDir = "";
    static String jksKey = "";
    static String keyPwd = "";
    static String privKeyAlias = "";
    static TrustManagerFactory tmf = null;

    public static void main(String[] args) throws Exception {

        try {
            url = args[0];
            System.out.println(url);
            fileToSend = args[1];
            System.out.println(fileToSend);
            receiptDir = args[2];
            System.out.println(receiptDir);
            jksKey = args[3];
            System.out.println(jksKey);
            keyPwd = args[4];
            System.out.println(keyPwd);
            privKeyAlias = args[5];
            System.out.println(privKeyAlias);
        } catch (Exception e) {
            System.out.println("Paramètres incorrects !");
            e.printStackTrace();
        }

        // Timestamp to use
        SimpleDateFormat formater = null;
        Date aujourdhui = new Date();
        formater = new SimpleDateFormat("yyyyMMddHHmmss");
        myToken = formater.format(aujourdhui);
        // myToken = "20150112202835";


        ////////////////////////////
        // Constructing the message
        ////////////////////////////
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        soapEnvelope = soapPart.getEnvelope();
        soapEnvelope.removeNamespaceDeclaration(soapEnvelope.getPrefix());
        soapEnvelope.setPrefix("soapenv");
        soapEnvelope.addNamespaceDeclaration("tem", "http://tempuri.org/");
        soapEnvelope.addNamespaceDeclaration("elc",
                "http://schemas.datacontract.org/2004/07/ZIRE.Match.Presentation.WebService");

        SOAPHeader soapHeader = soapEnvelope.getHeader();
        soapHeader.setPrefix("soapenv");

        Name nameSecurity = soapEnvelope.createName("Security", "wsse",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        SOAPElement soapSecurity = soapHeader.addChildElement(nameSecurity);
        soapSecurity.addNamespaceDeclaration("wsu",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");

        Name nameBinSec = soapEnvelope.createName("BinarySecurityToken", "wsse",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        SOAPElement soapBinSec = soapSecurity.addChildElement(nameBinSec);
        soapBinSec.setAttribute("EncodingType",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
        soapBinSec.setAttribute("ValueType",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
        soapBinSec.setAttribute("wsu:Id", "X509-" + myToken);

        SOAPBody soapBody = soapEnvelope.getBody();
        soapBody.setPrefix("soapenv");
        soapBody.addAttribute(
                soapEnvelope.createName("id", "wsu",
                        "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"),
                "id-" + myToken);
        soapBody.setIdAttribute("wsu:id", true);


        ////////////////////////////
        // Adding the real content
        ////////////////////////////
//       The real payload new String(readAllBytes(get("test.txt")))
        String xmlValue = "<![CDATA[" + readFile(fileToSend,  StandardCharsets.UTF_8) + "]]>";      
//      String xmlValue = "<![CDATA[" + new String(readAllBytes(get(fileToSend))) + "]]>";
//      System.out.println(xmlValue);

        QName sendMessage = new QName("tem:SendMessage");
        QName request = new QName("tem:request");
        QName xmldata = new QName("elc:XmlData");

        SOAPElement sendMessageNode = soapBody.addChildElement(sendMessage);
        SOAPElement requestNode = soapBody.addChildElement(request);
        SOAPElement xmldataNode = soapBody.addChildElement(xmldata);

        xmldataNode.addTextNode(xmlValue);
        requestNode.addChildElement(xmldataNode);
        sendMessageNode.addChildElement(requestNode);
        Source source = soapPart.getContent();


        ////////////////////////////////////////////////////////
        // Sending the request to the signing function
        ////////////////////////////////////////////////////////
        Node root = ((DOMSource) source).getNode();     
        signedRequest = addSignature(root.getFirstChild().getOwnerDocument());


        ////////////////////////////////////////////////////////
        // SSL mode configuration
        ////////////////////////////////////////////////////////
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            // Using null here initialises the TMF with the default trust store.
            tmf.init((KeyStore) null);

            // Get hold of the default trust manager
            X509TrustManager defaultTm = null;
            for (TrustManager tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    defaultTm = (X509TrustManager) tm;
                    break;
                }
            }

            FileInputStream myKeys = new FileInputStream("D:\\ZIRE\\MesCertificats\\myTrustStore");

            // Do the same with your trust store this time
            // Adapt how you load the keystore to your needs
            KeyStore myTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            myTrustStore.load(myKeys, "zire2016".toCharArray());

            myKeys.close();

            tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(myTrustStore);

            // Get hold of the default trust manager
            X509TrustManager myTm = null;
            for (TrustManager tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    myTm = (X509TrustManager) tm;
                    break;
                }
            }

            // Wrap it in your own class.
            final X509TrustManager finalDefaultTm = defaultTm;
            final X509TrustManager finalMyTm = myTm;
            X509TrustManager customTm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // If you're planning to use client-cert auth,
                    // merge results from "defaultTm" and "myTm".
                    return finalDefaultTm.getAcceptedIssuers();
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                    try {
                        finalMyTm.checkServerTrusted(chain, authType);
                    } catch (CertificateException e) {
                        // This will throw another CertificateException if this fails too.
                        finalDefaultTm.checkServerTrusted(chain, authType);
                    }
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                    // If you're planning to use client-cert auth,
                    // do the same as checking the server.
                    finalDefaultTm.checkClientTrusted(chain, authType);
                }
            };


            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { customTm }, null);

            // You don't have to set this as the default context,
            // it depends on the library you're using.
            SSLContext.setDefault(sslContext);



            ////////////////////////////////////////////////////////
            // Sending the request to the receiver
            ////////////////////////////////////////////////////////
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();       
            // Send SOAP Message to SOAP Server     
//          System.setProperty("javax.net.ssl.trustStore", "D:\\ZIRE\\MesCertificats\\myTrustStore" );
//          System.setProperty("javax.net.ssl.trustStorePassword", "zire2016");     
//          System.setProperty("java.net.useSystemProxies", "true");
//           SOAPMessage soapResponse = soapConnection.call(signedRequest, url);
//          doTrustToCertificates();
            SOAPMessage soapResponse = sendMessage(signedRequest, url);


        ////////////////////////////////////////////////////////
        // Processing the response
        ////////////////////////////////////////////////////////
        // Cleaning of the response
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapResponse.writeTo(out);
        String strMsg = new String(out.toByteArray());
        int d = strMsg.indexOf("<s:Envelope");
        int f = strMsg.indexOf("</s:Envelope>") + 13;
        String strMsg2 = strMsg.substring(d, f);
        // System.out.println(strMsg2);
        // Writting of the response on the server
//      PrintWriter printWriter = new PrintWriter("D:\\ZIRE\\Result.xml");
//      fileToSend
//      String receiptFileName = (new File(fileToSend)).getParent() + "Receipt_" +  (new File(fileToSend)).getName();
//      System.out.println(receiptFileName);

        // Writing the response
        String receiptFileName = receiptDir + "Receipt_" +  (new File(fileToSend)).getName();
        System.out.println(receiptFileName);
        PrintWriter printWriter = new PrintWriter( receiptFileName );
        printWriter.println(strMsg2);
        printWriter.close();
        soapConnection.close();

    }

    public static SOAPMessage addSignature(Document contenu)
            throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException,
            IOException, InvalidAlgorithmParameterException, InstantiationException, IllegalAccessException,
            ClassNotFoundException, KeyException, SAXException, ParserConfigurationException, MarshalException,
            XMLSignatureException, TransformerException, InvalidKeySpecException, XMLSecurityException, SOAPException {

        // Chargement des clés
//      FileInputStream is = new FileInputStream("D:\\ZIRE\\Step 1\\mock\\NewJKS.jks");
        FileInputStream is = new FileInputStream(jksKey);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, null);

//      tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()); // PKIX
//      tmf.init(keystore);

//       Enumeration<String> myEnum = keystore.aliases();
//      for (Enumeration<String> e = keystore.aliases(); e.hasMoreElements();) {
//          String newAlias = e.nextElement();
//          System.out.println(newAlias);
//          if (newAlias != "wsgbit" ) {
//              System.out.println(keystore.getCertificate(newAlias));
//          }   
//      }

        // Recup clé privée
//      String alias = "wsgbit";
        String alias = privKeyAlias;
//      Key key = keystore.getKey(alias, "password".toCharArray());
        Key key = keystore.getKey(alias, keyPwd.toCharArray());
        KeyPair kp = null;
        Certificate cert = null;
        if (key instanceof PrivateKey) {
            // Get certificate of public key
            cert = keystore.getCertificate(alias);
            // Get public key
            PublicKey publicKey = cert.getPublicKey();

            // Return a key pair
            kp = new KeyPair(publicKey, (PrivateKey) key);
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = contenu;
//       dumpDocument(doc);


        // MAJ du champ wsse:BinarySecurityToken
        Element elt = doc.getDocumentElement();
        Node myBinSecElt = (Node) (elt.getElementsByTagName("wsse:BinarySecurityToken")).item(0);
        myBinSecElt.setTextContent(Utf8.decode(Base64.encode(cert.getEncoded())));

        // Debut
        org.apache.xml.security.Init.init();

        Node mySecElt = (Node) (elt.getElementsByTagName("wsse:Security")).item(0);
        org.apache.xml.security.signature.XMLSignature xmlSignature = new org.apache.xml.security.signature.XMLSignature(
                doc, "", org.apache.xml.security.signature.XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1,
                "http://www.w3.org/2001/10/xml-exc-c14n#");
        Element eltSign = xmlSignature.getElement();
        eltSign.setAttribute("Id", "SIG-" + myToken);
        mySecElt.appendChild(eltSign);

        Transforms transforms = new Transforms(doc);
        transforms.addTransform("http://www.w3.org/2001/10/xml-exc-c14n#");
        xmlSignature.addDocument("#id-" + myToken, transforms, Constants.ALGO_ID_DIGEST_SHA1);

        SOAPElement myCanonElt = (SOAPElement) (elt.getElementsByTagName("ds:CanonicalizationMethod")).item(0);
        InclusiveNamespaces inclusiveNamespaces = new InclusiveNamespaces(doc, "elc soapenv tem");
        myCanonElt.appendChild(inclusiveNamespaces.getElement());

        SOAPElement mySoapTransform = (SOAPElement) (elt.getElementsByTagName("ds:Transform")).item(0);
        InclusiveNamespaces inclusiveNamespaces2 = new InclusiveNamespaces(doc, "elc tem");
        mySoapTransform.appendChild(inclusiveNamespaces2.getElement());

        // Ajustements
        // SOAPElement myCanonElt = (SOAPElement)
        // (elt.getElementsByTagName("ds:CanonicalizationMethod")).item(0);
        // myCanonElt.setAttribute("Algorithm",
        // "http://www.w3.org/2001/10/xml-exc-c14n#");
        // Name nameInclusiveNamespaces =
        // soapEnvelope.createName("InclusiveNamespaces", "ec",
        // "http://www.w3.org/2001/10/xml-exc-c14n#");
        // SOAPElement soapInclusiveNamespaces =
        // myCanonElt.addChildElement(nameInclusiveNamespaces);
        // soapInclusiveNamespaces.setAttribute("PrefixList", "elc soapenv
        // tem");
        //
        // SOAPElement mySoapTransform = (SOAPElement)
        // (elt.getElementsByTagName("ds:Transform")).item(0);
        // Name nameInclusiveNamespaces2 =
        // soapEnvelope.createName("InclusiveNamespaces", "ec",
        // "http://www.w3.org/2001/10/xml-exc-c14n#");
        // SOAPElement soapInclusiveNamespaces2 =
        // mySoapTransform.addChildElement(nameInclusiveNamespaces2);
        // soapInclusiveNamespaces2.setAttribute("PrefixList", "elc tem");
        //
        Name nameKeyInfo = soapEnvelope.createName("KeyInfo", "ds", "http://www.w3.org/2000/09/xmldsig#");
        SOAPElement mySoapSign = (SOAPElement) (elt.getElementsByTagName("ds:Signature")).item(0);
        SOAPElement soapKeyInfo = mySoapSign.addChildElement(nameKeyInfo);
        soapKeyInfo.setAttribute("Id", "KI-" + myToken);

        Name nameSecToken = soapEnvelope.createName("SecurityTokenReference", "wsse",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        SOAPElement soapSecToken = soapKeyInfo.addChildElement(nameSecToken);
        soapSecToken.setAttribute("wsu:Id", "STR-" + myToken);

        Name nameRef = soapEnvelope.createName("Reference", "wsse",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        SOAPElement soapNameRef = soapSecToken.addChildElement(nameRef);
        soapNameRef.setAttribute("URI", "#X509-" + myToken);
        soapNameRef.setAttribute("ValueType",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");

        xmlSignature.sign(key);
//      xmlSignature.sign(key);
//      dumpDocument(doc);

        // String xml = new XMLOutputter().outputString(doc);

        // Ecriture du String represantant la requete signée
        StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
//      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
//      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//      transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(new DOMSource(doc), new StreamResult(sw));
//      String xml = sw.toString(); System.out.println(">>>" + xml + "<<<");

        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));// new
                                                                                                    // InputStream(xml);
        MessageFactory messageFactory = MessageFactory.newInstance();
        return messageFactory.createMessage(null, inputStream);

        // Ecriture dans un fichier
        // Transformer transformer =
        // TransformerFactory.newInstance().newTransformer();
        // Result output = new StreamResult(new
        // FileOutputStream("D:\\ZIRE\\mySignedFile2.xml"));
        // Source input = new DOMSource(doc);
        // transformer.transform(input, output);

    }

    public static String convertToString(Document document) throws TransformerException {

        DOMSource domSource = new DOMSource(document);
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        // create an instance of TransformerFactory
        TransformerFactory transFact = TransformerFactory.newInstance();
        Transformer trans = transFact.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        trans.transform(domSource, result);
        return sw.toString();
    }

    private static void dumpDocument(Node root) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
//      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(root), new StreamResult(System.out));
    }

    private static Element getFirstChildElement(Node node) {
        Node child = node.getFirstChild();
        while ((child != null) && (child.getNodeType() != Node.ELEMENT_NODE)) {
            child = child.getNextSibling();
        }
        return (Element) child;
    }

    public static Element getNextSiblingElement(Node node) {
        Node sibling = node.getNextSibling();
        while ((sibling != null) && (sibling.getNodeType() != Node.ELEMENT_NODE)) {
            sibling = sibling.getNextSibling();
        }
        return (Element) sibling;
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    static public SOAPMessage sendMessage(SOAPMessage message, String endPoint) throws SOAPException, IOException {
        SOAPMessage result = null;
        if (endPoint != null && message != null) {
            URL url;
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection connection = null;
            try {
                connection = scf.createConnection(); //point-to-point connection
                url = new URL(endPoint);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                con.connect();
                result = connection.call(message, url);
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SOAPException soape) {
                        System.out.print("Can't close SOAPConnection:" + soape);
                    }
                }
            }
        }
        return result;
    }

    static public void doTrustToCertificates() throws Exception {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                        return;
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }
}
