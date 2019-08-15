    String transform = "my transform here"

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(new ByteArrayInputStream(transform.getBytes()));


    Transforms transforms = new Transforms(doc.getDocumentElement(), null);

    XMLSignatureInput input = new XMLSignatureInput(xml.getBytes());
    XMLSignatureInput result = transforms.performTransforms(input);
    MessageDigest md = MessageDigest.getInstance("SHA");
    md.update(result.getBytes());
    return md.digest();
