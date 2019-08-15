private void decodealgo() {

        vers();

        geheim2 = input.getText().toString();

    // BASE64 String zu Byte-Array konvertieren
    data = Base64.decode(geheim2, Base64.NO_WRAP);


    // Entschluesseln

    try {
        cipher3 = Cipher.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    try {
        cipher3.init(Cipher.DECRYPT_MODE, secretKeySpec2);
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        Toast.makeText(getApplicationContext(), "Invalid key",
        Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }

    try {
        cipherData3 = cipher3.doFinal(data);
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        Toast.makeText(getApplicationContext(), "No valid encryption",
        Toast.LENGTH_SHORT).show();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        Toast.makeText(getApplicationContext(), "Key invalid format",
        Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }

    try {
        text3 = new String(cipherData3, "UTF-8");
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }


    // Klartext
    output.setText(text3);

}
