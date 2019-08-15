Security.addProvider(new BouncyCastleProvider());

    DuncanCipherSecond cipherSecond = new DuncanCipherSecond();

    RSAKeyXMLReader rsaKeyXMLReader = new RSAKeyXMLReader();
    InputStream inputStream = new ByteArrayInputStream(rsaKeyInXMLString.getBytes());
    RSAData rsaData = rsaKeyXMLReader.parse(inputStream);

    if (rsaData != null) {
        byte[] expBytes = Base64.decodeBase64(rsaData.getExponent().trim());
        byte[] modBytes = Base64.decodeBase64(rsaData.getModulus().trim());
        byte[] dBytes = Base64.decodeBase64(rsaData.getD().trim());


        String nameFromDB = "8B-FE-BE-28-27-94-0B-32-CE-86-24-9E-F1-DA-86-0F-E1-31-B7-2B-7A-58-2C-E2-3A-6F-05-E9-40-70-48-1D-73-6B-30-E5-95-B9-2A-8A-3F-6E-66-94-DF-8F-7C-11-77-C6-A1-E9-09-04-3B-19-C2-AF-19-CE-EE-22-A3-F9-17-D5-C8-C5-9F-12-F2-C5-72-25-D6-C3-D9-4B-E0-E5-46-19-27-80-9A-11-EC-0F-85-4D-13-0B-94-DB-1B-64-21-95-68-7B-A3-C9-1B-AA-94-E7-26-48-49-BA-CD-97-9D-41-23-1E-B6-F9-26-3C-1E-67-84-98-83-A7-3C-06";
        nameFromDB = nameFromDB.replace("-", "");

        byte[] newNameFromDB = hexStringToByteArray(nameFromDB);

        BigInteger modules = new BigInteger(1, modBytes);
        BigInteger exponent = new BigInteger(1, expBytes);
        BigInteger d = new BigInteger(1, dBytes);

        KeyFactory factory = KeyFactory.getInstance("RSA");
        Cipher cipher = Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");

        System.out.println("");
        RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, d);
        PrivateKey privKey = factory.generatePrivate(privSpec);
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        byte[] decrypted = cipher.doFinal(newNameFromDB);
        System.out.println("decrypted: " + new String(decrypted));
