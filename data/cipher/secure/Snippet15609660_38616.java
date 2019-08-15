    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGO);
    c.init(Cipher.ENCRYPT_MODE, key);
    String salt = "some salt";
    String valueToEnc = null;
    String eValue = Data;
    for (int i = 0; i < 2; i++) {
        valueToEnc = salt + eValue;
        byte[] encValue = c.doFinal(valueToEnc.getBytes(UNICODE_FORMAT));
        eValue =new String(Base64.encodeBase64(encValue));
    }
