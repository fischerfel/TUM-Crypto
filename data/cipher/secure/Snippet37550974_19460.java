    if (jsonToken.has("data"))
    {
        // Converte la Stringa in XML
        String publicToken = jsonToken.getString("data");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document publicTokenXML = builder.parse(new InputSource(new StringReader(publicToken)));


        Log.i("out", token.toString());
        Log.i("outXML", publicTokenXML.getFirstChild().getFirstChild().getNextSibling().getTextContent());

        // <RSAKeyValue><Modulus>*Modulus*</Modulus><Exponent>*Exponent*</Exponent></RSAKeyValue>
        byte[] expBytes = Base64.decode(publicTokenXML.getFirstChild().getFirstChild().getNextSibling().getTextContent(), Base64.DEFAULT);
        byte[] modBytes = Base64.decode(publicTokenXML.getFirstChild().getFirstChild().getTextContent(), Base64.DEFAULT);
        byte[] dBytes = Base64.decode(d, Base64.DEFAULT);


        BigInteger modules = new BigInteger(1, modBytes);
        BigInteger exponent = new BigInteger(1, expBytes);
        BigInteger d = new BigInteger(1, dBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // *****
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // *****

        RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modules, exponent);
        PublicKey pubKey = keyFactory.generatePublic(pubSpec);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] encrypted = cipher.doFinal(bin2hex(getHash(pwd)).getBytes("UTF-8"));

        // *****
        Log.i("encrypted: ", Base64.encodeToString(encrypted, Base64.DEFAULT).replace("\n", ""));
        // *****

        // Decrypt
        RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, d);
        PrivateKey privKey = keyFactory.generatePrivate(privSpec);
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        byte[] decrypted = cipher.doFinal(encrypted);
        System.out.println("decrypted: " + new String(decrypted));
        Log.i("hash", bin2hex(getHash(pwd)));
    }
