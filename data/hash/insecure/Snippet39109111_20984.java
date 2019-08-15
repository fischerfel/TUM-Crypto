        // Change value if there is any problem on websphere
        // wsuId = "\"#MsgBody\"";
        wsuId = "\"#id-" + UUIDGenerator.getUUID() + "\"";
        // Create XML data for body SOAP
        String dataXMLSoap = generateBodySoap(doc);
        String startBodyHash = "<soapenv:Body xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" wsu:Id=" + wsuId+ ">";
        String endBodyHash  = "</soapenv:Body>";
        MessageDigest messageDigestFromBody= MessageDigest.getInstance("SHA-1");            
        String xmlHash = startBodyHash + dataXMLSoap + endBodyHash;
        byte[] hashSoapBody = messageDigestFromBody.digest(xmlHash.getBytes());
        String digestValueFromBody = new BASE64Encoder().encode(hashSoapBody);
