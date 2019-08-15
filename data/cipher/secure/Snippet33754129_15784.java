doDecryption(){
    try {
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(keyName, null);
        PrivateKey privateKey = (PrivateKey) privateKeyEntry.getPrivateKey();

        Cipher c;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            c =  Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        }else{
            c =  Cipher.getInstance("RSA/ECB/PKCS1Padding");
        }
        c.init(Cipher.DECRYPT_MODE, privateKey);
        decodedUser = c.doFinal(encodedUser);
        decodedPassword = c.doFinal(encodedPassword);

    } catch (Exception e) {
        Log.e("MainActivity", "AES Decryption Error.!");
    }

}
