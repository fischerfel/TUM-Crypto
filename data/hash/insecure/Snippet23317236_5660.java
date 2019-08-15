public static void GenarationKEY(byte[] data) {

        try {
            File fileEndSignatuer = new File("EndSignatuer.txt");
            FileOutputStream fopEndSignatuer = new FileOutputStream(
                    fileEndSignatuer);
            // /Read private key from file
            FileInputStream keyfis = new FileInputStream("PiveteKey.txt");
            byte[] PrivateKeyB = new byte[keyfis.available()];
            keyfis.read(PrivateKeyB);
            keyfis.close();
            byte[] decodePrivetekey = Base64.decodeBase64(PrivateKeyB);
            // /get private key
            PKCS8EncodedKeySpec pubKeySpec = new PKCS8EncodedKeySpec(
                    decodePrivetekey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = keyFactory.generatePrivate(pubKeySpec);
            // / make hash
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privKey);
            // /make encoding
            MessageDigest md5_digest = MessageDigest.getInstance("MD5");
            byte[] digest = md5_digest.digest(data);
            byte[] cipherText = cipher.doFinal(digest);
            byte[] degnatureencode = Base64.encodeBase64(cipherText);
            fopEndSignatuer.write(degnatureencode);
            fopEndSignatuer.flush();
            fopEndSignatuer.close();
} 
