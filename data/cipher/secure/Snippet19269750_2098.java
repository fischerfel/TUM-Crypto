//cipher init
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

    //charger la clef privée
    PrivateKey  privKey =  (PrivateKey) getPrivateKey();
    cipher.init(Cipher.DECRYPT_MODE,  privKey);
    //System.out.println("password entré : '" + password + "'");
    byte[]  cipherData=  cipher.doFinal(toByte(password));
    //System.out.println("password getbyte taille: '" + toByte(password).length + "'");

    return cipherData;
