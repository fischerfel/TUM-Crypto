public void faz() throws NoSuchAlgorithmException, Exception {

    /////////////////////////////////////////////////////////////
    //get the timestamp, nonce and password in SHA-1 and BASE64//       
    /////////////////////////////////////////////////////////////


    String nonce, timestamp, secret;
    nonce = String.valueOf(this.hashCode());
    BASE64Encoder encoder2 = new BASE64Encoder();
    nonce = encoder2.encode(nonce.getBytes());
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());

    timestamp = DatatypeConverter.printDateTime(c);
    timestamp = timestamp.substring(0, 19);
    timestamp = timestamp+"Z";
    secret = "weblogic1";
    MessageDigest SHA1 = null;
    try {
        SHA1 = MessageDigest.getInstance("SHA1");
    } catch (NoSuchAlgorithmException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    ;

    String beforeEncryption = nonce + timestamp + secret;
try {

        SHA1.reset();
        byte[] toEncrypt = beforeEncryption.getBytes("UTF-8");
        SHA1.update(beforeEncryption.getBytes());
    } catch (UnsupportedEncodingException uee) {
        throw new RuntimeException(uee);
    }

    byte[] encryptedRaw = SHA1.digest();
    byte[] encoded = Base64.encodeBase64(encryptedRaw);
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
    digest.update("password".getBytes());
    BASE64Encoder encoder = new BASE64Encoder();
    String senha = encoder.encode(digest.digest());
    System.err.println(senha);
    ////////////////////////////////////
    //////////END //////////////////////        
    ////////////////////////////////////

    CalculaServiceService ss = new CalculaServiceServiceLocator();

    CalculaService service = ss.getCalculaServicePort();

    String uri = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    String uriCrea = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"; 

    SOAPHeaderElement securityE = new SOAPHeaderElement(uri, "Security",
            null);
    SOAPHeaderElement tokenE = new SOAPHeaderElement(uri, "UsernameToken",
            null);
    SOAPHeaderElement userE = new SOAPHeaderElement(uri, "Username", null);
    tokenE.setObjectValue(null);


    securityE.setObjectValue(null);


    userE.setValue("username");
    SOAPHeaderElement pwdE = new SOAPHeaderElement(uri, "Password", null);
    pwdE.addAttribute(uri, "Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest");
    pwdE.setValue(senha);

    SOAPHeaderElement nonceE = new SOAPHeaderElement(uri, "Nonce", null);
    nonceE.addAttribute(uri, "EncodingType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest");
    nonceE.setValue(nonce);

    SOAPHeaderElement createdE = new SOAPHeaderElement(uriCrea, "Created", null);
    createdE.setValue(timestamp);




    tokenE.addChildElement(userE);
    tokenE.addChildElement(pwdE);
    tokenE.addChildElement(nonceE);
    tokenE.addChildElement(createdE);
    securityE.addChildElement(tokenE);
    ((Stub) service).setHeader(securityE);
    service.calcula(13, 10, "somar");
}
