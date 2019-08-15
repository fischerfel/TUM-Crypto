def beforeInsert() {
    hashpass = encodePassword(hashpass);
}

def encodePassword(cleartextpwd) {
    // create a key generator based upon the Blowfish cipher
    KeyGenerator keygenerator = KeyGenerator.getInstance("Blowfish");

    // create a key
    SecretKey secretkey = keygenerator.generateKey();

    // create a cipher based upon Blowfish
    Cipher cipher = Cipher.getInstance(ALGORITHM);

    // initialise cipher to with secret key
    cipher.init(Cipher.ENCRYPT_MODE, secretkey);

    // get the text to encrypt
    String inputText = cleartextpwd;

    // encrypt message
    byte[] encrypted = cipher.doFinal(inputText.getBytes("UTF-8"));
    return Base64.encodeBase64String(encrypted);
}
