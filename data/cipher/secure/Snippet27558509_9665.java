    Security.addProvider(new BouncyCastleProvider());
    PEMParser reader = new PEMParser(new StringReader(key));
    PemObject obj = reader.readPemObject();
    org.bouncycastle.asn1.pkcs.RSAPublicKey rsaPublicKey = org.bouncycastle.asn1.pkcs.RSAPublicKey.getInstance(obj.getContent());
    BigInteger modulus = rsaPublicKey.getModulus();
    BigInteger publicExponent = rsaPublicKey.getPublicExponent();

    KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
    RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
    PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");//This line should use right padding.For PKCS#1 format RSA key , it should be this.
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    return cipher.doFinal(data);
