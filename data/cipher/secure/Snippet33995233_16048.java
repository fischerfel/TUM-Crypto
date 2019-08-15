public String doEncryption(String data) {
    try {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ce = Cipher.getInstance("AES/GCM/NoPadding");
            sKey = (SecretKey) ks.getKey(keyName, null);
            ce.init(Cipher.ENCRYPT_MODE, sKey);
        } else {

        }

        encodedData = ce.doFinal(data.getBytes());
        mEncodedData = Base64.encodeToString(encodedData, Base64.DEFAULT);
    } catch (Exception e) {
        Log.e("Main", "RSA Encription Error.!");
        Log.e("MainActivity", "RSA Decryption Error.!", e);
    }
    Log.e("Main", "encripted DATA =" + mEncodedData);
    return mEncodedData;
}
