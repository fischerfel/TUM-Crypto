public KeyPair wrapPrivateKeyWithSymmetricKey(KeyPair keyPair) {

    try {
        PrivateKey priv = keyPair.getPrivate();
        SecretKey symmetricKey = "bjksabfkasdbgvkasbvkkj";//symmetricKey from jks file

        //wrapping Private Key
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.WRAP_MODE, symmetricKey);
        byte[] wrappedKey = cipher.wrap(priv);

        //wrappedKey bytes to PrivateKey Object
        KeyFactory keyFactory = KeyFactory.getInstance(priv.getAlgorithm());
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(wrappedKey);
        PrivateKey privateKey2 = keyFactory.generatePrivate(privateKeySpec); //above Error Throwing in this line

        return new KeyPair(keyPair.getPublic(), privateKey2);;
}
