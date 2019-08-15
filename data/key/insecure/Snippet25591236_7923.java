public void secretKeyGeneration(View view) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    byte[]  sek = "eru9tyighw34ilty348934i34uiq34q34ri".getBytes();
    SecretKey sk = new SecretKeySpec(sek, 0, sek.length, "AES");    
    char[] password = "keystorepassword".toCharArray();
    KeyStore.ProtectionParameter protParam = 
    new KeyStore.PasswordProtection(password);

    KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(sk);
    ks.setEntry("secretKeyAlias", skEntry, protParam);

    }   
