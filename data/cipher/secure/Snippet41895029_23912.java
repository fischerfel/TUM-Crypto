 public byte[] encryptAESKey() throws Exception {
        Cipher cipher;
        byte[] cipherData = null;
        byte[] bkey=secretKey.getEncoded();
        cipher = Cipher.getInstance("RSA/CBC/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey());
        cipherData = cipher.doFinal(bkey);
        return cipherData;
    }

public PublicKey loadPublicKey() throws Exception {

    String storename = "C:\\encryption\\public.jks";
    char[] storepass = "test".toCharArray();

    KeyStore ks = KeyStore.getInstance("JKS");
    FileInputStream fin = null;
    fin = new FileInputStream(storename);
    ks.load(fin, storepass);
    X509Certificate cert = (X509Certificate) ks.getCertificate("cert");
    PublicKey publicKey = cert.getPublicKey();
    return publicKey;
}
