doEncription(){
 try {
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(keyName, null);
        PublicKey publicKey = (PublicKey) privateKeyEntry.getCertificate().getPublicKey();
        Cipher c;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            c =  Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        }else{
            c =  Cipher.getInstance("RSA/ECB/PKCS1Padding");
        }

        c.init(Cipher.ENCRYPT_MODE, publicKey);
        encodedUser = c.doFinal(userName.getBytes());
        encodedPassword = c.doFinal(userPassword.getBytes());

        userName = Base64.encodeToString(encodedUser, Base64.DEFAULT);
        userPassword = Base64.encodeToString(encodedPassword, Base64.DEFAULT);
        // Log.e("MainActivity","AES Encription Error.!");
    } catch (Exception e) {
        Log.e("MainActivity", "AES Encription Error.!");
    }
}
