public static SOAPHeaderBlock getHeaders(String usernameTxt,
            String passwordTxt) {

        LOGGER.debug("Inside getHeaders");
        String WS_SEC_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
        SOAPFactory soapFact = OMAbstractFactory.getSOAP12Factory();
        OMNamespace ns = soapFact.createOMNamespace(WS_SEC_NS, "wsse");
        OMNamespace nsu = soapFact.createOMNamespace(WS_SEC_NS, "wsu");             //try
        SOAPHeaderBlock wssHeader = soapFact.createSOAPHeaderBlock("Security",
                ns);
        OMElement usernameToken = soapFact.createOMElement("UsernameToken", ns);
        OMElement username = soapFact.createOMElement("Username", ns);
        OMElement password = soapFact.createOMElement("Password", ns);
        OMElement nonce = soapFact.createOMElement("Nonce", ns);
        OMElement created = soapFact.createOMElement("Nonce", nsu);


        LOGGER.info("getHeaders: set the WS-security header values");
        username.setText(usernameTxt);


        UUID randomId = UUID.randomUUID();
        String phrase = String.valueOf(randomId);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        md.update(phrase.getBytes());
        byte[] byteNonce = md.digest();
        //String nonceHash = String.valueOf(BytesToHex.bytesToHex(byteNonce));
        String nonceHash = String.valueOf(Base64.encodeBase64(byteNonce));
        nonce.setText(nonceHash);

        Date currentTimestamp = new Date();
        String currentTs = String.valueOf(currentTimestamp);
        created.setText(currentTs);

        md.update((nonceHash+currentTs+passwordTxt).getBytes());
        byte[] bytePasswordcode = md.digest();
        String passwordHash = String.valueOf(Base64.encodeBase64(bytePasswordcode));
        //String passwordHash = String.valueOf(BytesToHex.bytesToHex(bytePasswordcode));

        password.setText(passwordHash);

        password.addAttribute(WSConstants.PASSWORD_TYPE_ATTR,
                WSConstants.PASSWORD_DIGEST, null);
        nonce.addAttribute(WSConstants.NONCE_LN, WSConstants.BINARY_TOKEN_LN, null);

        usernameToken.addChild(username);
        usernameToken.addChild(password);
        usernameToken.addChild(nonce); //nonce added
        usernameToken.addChild(created);
        wssHeader.addChild(usernameToken);
        System.out.println(String.valueOf(usernameToken)); //remove
        return wssHeader;
    }
