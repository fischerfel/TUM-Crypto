public class GenerateXmlSignature2 {
    private static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PurchaseOrder><foo>bar</foo></PurchaseOrder>";
    private static final String xpath  = "PurchaseOrder/foo/text()";

    public static void main(String[] args) throws Exception {
        Base64 base64 = new Base64();
        // Create a DOM XMLSignatureFactory that will be used to
        // generate the enveloped signature.
        final XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

        // Create a Reference to the enveloped document (in this case,
        // you are signing the whole document, so a URI of "" signifies
        // that, and also specify the SHA1 digest algorithm and
        // the ENVELOPED Transform.
        final List<XPathType> xpaths = new ArrayList<XPathType>() {
            {
                add(new XPathType(xpath, XPathType.Filter.UNION));
            }
        };
        List<Transform> transforms = new ArrayList<Transform>() {{
             add(fac.newTransform(
                Transform.XPATH2,
                new XPathFilter2ParameterSpec(xpaths)
            )
            );
        }};
        Reference ref = fac.newReference
                ("", fac.newDigestMethod(DigestMethod.SHA1, null),
                        transforms,
                        null, null);


        // Create the SignedInfo.
        SignedInfo si = fac.newSignedInfo
                (fac.newCanonicalizationMethod
                        (CanonicalizationMethod.INCLUSIVE,
                                (C14NMethodParameterSpec) null),
                        fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                        Collections.singletonList(ref));


        // Load the KeyStore and get the signing key and certificate.
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("mykeystore.jks"), "changeit".toCharArray());
        KeyStore.PrivateKeyEntry keyEntry =
                (KeyStore.PrivateKeyEntry) ks.getEntry
                        ("mykey", new KeyStore.PasswordProtection("changeit".toCharArray()));
        X509Certificate cert = (X509Certificate) keyEntry.getCertificate();

        // Create the KeyInfo containing the X509Data.
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        List x509Content = new ArrayList();
        x509Content.add(cert.getSubjectX500Principal().getName());
        x509Content.add(cert);
        X509Data xd = kif.newX509Data(x509Content);
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

        // Instantiate the document to be signed.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().parse
                (new ByteArrayInputStream(xml.getBytes()));

        // Create a DOMSignContext and specify the RSA PrivateKey and
        // location of the resulting XMLSignature's parent element.
        DOMSignContext dsc = new DOMSignContext
                (keyEntry.getPrivateKey(), doc.getDocumentElement());

        // Create the XMLSignature, but don't sign it yet.
        XMLSignature signature = fac.newXMLSignature(si, ki);



        // Marshal, generate, and sign the enveloped signature.
        signature.sign(dsc);

        // Output the resulting document.
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(doc), new StreamResult(System.out));



        System.out.println("\n\n*** SHA-1 Digest ***");
        XPathExpression xpathExpression = XPathFactory.newInstance().newXPath().compile(xpath);
        String data = xpathExpression.evaluate(new InputSource(new StringReader(xml)));
        System.out.println("Xpath: " + data);
        MessageDigest md;
        md = MessageDigest.getInstance("SHA");
        byte[] sha1hash;
        md.update(data.getBytes(), 0, data.length());
        sha1hash = md.digest();
        String base64Sha1OfCanonicalXml = new String(base64.encode(sha1hash));
        System.out.println("Digest:   " + base64Sha1OfCanonicalXml);
    }
}
