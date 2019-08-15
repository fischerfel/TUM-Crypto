private void codealgo() {

    vers();

    // der zu verschl. Text
    text1 = input.getText().toString();


    // Verschluesseln

    try {
        cipher = Cipher.getInstance("AES");
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    try {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    try {
        encrypted = cipher.doFinal(text1.getBytes());
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    // bytes zu Base64-String konvertieren (dient der Lesbarkeit)
    geheim = Base64.encodeToString(encrypted, Base64.NO_WRAP);


    // Ergebnis
    output.setText(geheim);

}
