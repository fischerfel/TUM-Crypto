        // create the transformer in order to transform the document from
        // DOM Source as a JAVA document class, into a character stream (StreamResult) of
        // type String writer, in order to be converted to a string later on
        TransformerFactory tf = new net.sf.saxon.TransformerFactoryImpl();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        // create the string writer and transform the document to a character stream
        StringWriter sw = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(sw));

        String documentAsString = sw.toString();

        // initialize the XML security object, which is necessary to run the apache canonicalization
        com.sun.org.apache.xml.internal.security.Init.init();

        // canonicalize the document to a byte array and convert it to string
        Canonicalizer canon = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        byte canonXmlBytes[] = canon.canonicalize(documentAsString.getBytes());
        String canonXmlString = new String(canonXmlBytes);

        // get instance of the message digest based on the SHA-256 hashing algorithm
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // call the digest method passing the byte stream on the text, this directly updates the message
        // being digested and perform the hashing
        byte[] hash = digest.digest(canonXmlString.getBytes(StandardCharsets.UTF_8));

        // encode the endresult byte hash
        byte[] encodedBytes = Base64.encodeBase64(hash);

        return new String(encodedBytes);
