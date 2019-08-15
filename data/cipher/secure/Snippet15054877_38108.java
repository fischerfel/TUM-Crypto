public String encrypt(String smsbody) {
    try {

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");

        kpg.initialize(512);// initialize key pairs to 512 bits ,you can
                            // also take 1024 or 2048 bits

        KeyPair kp = kpg.genKeyPair();

        PublicKey publi = kp.getPublic();
        System.out.println(publi.serialVersionUID);
        Cipher cipher = Cipher.getInstance("RSA");

        cipher.init(Cipher.ENCRYPT_MODE, publi);

        byte[] src = smsbody.getBytes();// converting source data into byte
                                        // array

        byte[] cipherData = cipher.doFinal(src);// use this method to
                                                // finally encrypt data

        String srco = new String(cipherData);// converting byte array into
                                                // string
        // System.out.println();
        // System.out.println("Encrypted data is:-" + srco);

        return srco;
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    return "";
}

public PrivateKey Privatekey() {

    try {
        kpg = KeyPairGenerator.getInstance("RSA");

        kpg.initialize(512);// initialize key pairs to 512 bits ,you can
                            // also take 1024 or 2048 bits

        KeyPair kp = kpg.genKeyPair();
        privatei = kp.getPrivate();// Generating private key
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return privatei;
}

public String decrypt(String smsbody, PrivateKey privatei) {
    try {

        Cipher cipheri = Cipher.getInstance("RSA");

        cipheri.init(Cipher.DECRYPT_MODE, privatei);// Setting to
                                                    // decrypt_mode


        System.out.println(smsbody);

        byte[] cipherDat = cipheri.doFinal(smsbody.getBytes());// Finally
                                                                // decrypting
        // data
        System.out.println(cipherDat);
        String decryptdata = new String(cipherDat);


        return decryptdata;
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    return "";
} `
