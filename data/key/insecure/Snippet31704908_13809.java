public static void main(String[] args) throws Exception {

    // Create a DOM XMLSignatureFactory that will be used to generate the
    // enveloped signature
    XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
    SecretKey signingKey = new SecretKeySpec("welcome1".getBytes(), "HMAC");
    // Create a Reference to the enveloped document (in this case we are
    // signing the whole document, so a URI of "" signifies that) and
    // also specify the SHA1 digest algorithm and the ENVELOPED Transform.

    Reference ref = fac.newReference
        ("#_0", fac.newDigestMethod(DigestMethod.SHA1, null),
         Collections.singletonList
          (fac.newTransform
            ("http://www.w3.org/2001/10/xml-exc-c14n#", (TransformParameterSpec) null)),
         null, null);

    // Create the SignedInfo
    SignedInfo si = fac.newSignedInfo
        (fac.newCanonicalizationMethod
         (CanonicalizationMethod.EXCLUSIVE,
          (C14NMethodParameterSpec) null),
         fac.newSignatureMethod(SignatureMethod.HMAC_SHA1, null),
         Collections.singletonList(ref));


    // Instantiate the document to be signed
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    Document doc =
        dbf.newDocumentBuilder().parse(new FileInputStream("C:/Users/Signed_Encryp/timestamp.txt"));

    // Create a DOMSignContext and specify the DSA PrivateKey and
    // location of the resulting XMLSignature's parent element
    DOMSignContext dsc = new DOMSignContext
        (signingKey, doc.getDocumentElement());

    // Create the XMLSignature (but don't sign it yet)
    XMLSignature signature = fac.newXMLSignature(si, null);

    // Marshal, generate (and sign) the enveloped signature
    signature.sign(dsc);

    // output the resulting document
    OutputStream os;
    if (args.length > 1) {
       os = new FileOutputStream(args[1]);
    } else {
       os = System.out;
    }

    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer trans = tf.newTransformer();
    trans.transform(new DOMSource(doc), new StreamResult(os));
}
