public String doDecryption(String eData) {
    String decryptedText = null;
encodedData = Base64.decode(eData, Base64.DEFAULT);

    try {
        Cipher c;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Log.i("MainActivity","in Decryption version m");

            c = Cipher.getInstance("AES/GCM/NoPadding");
            sKey = (SecretKey) ks.getKey(keyName, null);
            Log.e("MainActivity", "After getting key : " );
            c.init(Cipher.DECRYPT_MODE, sKey);
        } else {

        }

        decodedData = c.doFinal(encodedData);
        decryptedText = new String(decodedData, "UTF-8");
        Log.e("MainActivity", "After decryption : "+ decryptedText);
    } catch (Exception e) {
        Log.e("MainActivity", "RSA Decryption Error.!", e);
    }
    return decryptedText;
}
