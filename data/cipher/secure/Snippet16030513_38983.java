public static String decode(byte[] encryptedData) {
    Key key = readPEMKey(new File("CA_key.pkcs8.pem"));
    //Key key = readPEMKey(new File("CA_key.pem"));
    try {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decValue = c.doFinal(encryptedData);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

private static Key readPEMKey(File key) {
    DataInputStream dis = null;
    BufferedReader reader = null;
    try {
        /*
        reader = new BufferedReader(new FileReader(key));
        String privKeyPEM = "";
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.equals("-----BEGIN RSA PRIVATE KEY-----") && !line.equals("-----END RSA PRIVATE KEY-----"))
            privKeyPEM += line + "\n";
        }
        byte[] encoded = new BASE64Decoder().decodeBuffer(privKeyPEM);
        */

        dis = new DataInputStream(new BufferedInputStream(new FileInputStream(key)));
        byte[] encoded = new byte[(int) key.length()];
        dis.readFully(encoded);

        // PKCS8 decode the encoded RSA private key
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);
        return privKey;
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    finally {
        if (reader != null) {try {reader.close();} catch (Exception e) {e.printStackTrace();}}
        if (dis != null) {try {dis.close();} catch (Exception e) {e.printStackTrace();}}
    }
    return null;
}
