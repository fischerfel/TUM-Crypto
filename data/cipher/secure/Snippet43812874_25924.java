static final int GCM_TAG_LENGTH = 16;
...
String decryptedText = null;
    byte[] encodedData = Base64.decode(data, Base64.DEFAULT);
    byte[] decodediv = Base64.decode(iv, Base64.DEFAULT);
    byte[] decodedKey = Base64.decode(key, Base64.DEFAULT);

    Log.e("data", new String(encodedData, 0));
    Log.e("iv", new String(decodediv, 0));
    Log.e("key", new String(decodedKey, 0));
    Log.d("test", "test");

    SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

    try {
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");

        c.init(Cipher.DECRYPT_MODE, originalKey , new GCMParameterSpec(GCM_TAG_LENGTH * 8,decodediv));
        byte[] decodedData = c.doFinal(encodedData);
        decryptedText = new String(decodedData, "UTF-8");
        Log.e("MainActivity", "After decryption : "+ decryptedText);
    } catch (Exception e) {
        Log.e("MainActivity", "Decryption Error.!", e);
    }
    return decryptedText;
