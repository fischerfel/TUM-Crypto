KeyStore ts = KeyStore.getInstance("BKS");
InputStream trustin = v.getResources().openRawResource(R.raw.trust_store_ca);
ts.load(trustin, "MyKey".toCharArray());
// Create own trustmanager with self-signed cert.
final TrustManagerFactory tmf = TrustManagerFactory.
    getInstance(KeyManagerFactory.getDefaultAlgorithm());
//tmf.init((KeyStore) null);
tmf.init(ts);
trustManagers = tmf.getTrustManagers();

CertificateFactory cf = CertificateFactory.getInstance("X.509");
InputStream cA_certificate = v.getResources().openRawResource(R.raw.ca_certif);
final X509Certificate caCertificate = (X509Certificate)cf
    .generateCertificate(cA_certificate);
// Check server certificate is valid
final X509TrustManager origTrustmanager = (X509TrustManager)trustManagers[0];

wrappedTrustManagers = new TrustManager[]{
                                    new X509TrustManager() {
    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                            return origTrustmanager.getAcceptedIssuers();
                                        }
    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {//Not used}

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) 
throws CertificateException{
    if (certs == null || certs.length == 0) {
        throw new IllegalArgumentException(
"checkServerTrusted: null or zero-length certificate chain");
    }
    if (authType == null || authType.length() == 0) {
        throw new IllegalArgumentException(
"checkServerTrusted: null or zero-length authentication type");
    }

// This does not work in Android, at least from me in JB...
//
// try { 
//      certs[0].verify(caCertificate.getPublicKey(), "BC");
//      Log.i(TAG,"Pubkey for caCertificate: "+ caCertificate.getPublicKey());
//      } catch (CertificateException | NoSuchAlgorithmException | 
//           NoSuchProviderException | InvalidKeyException | SignatureException e)
//            {Log.i(TAG,"Certificate verification exception" + e);}
//

// Do signature verification by decrypting it and comparing to expected
// value (01FFFFFFFFFFFFFFF.....FFFFFF003021300906052B0E03021A05 000414... etc)

byte[] signature = certs[0].getSignature();   // signature in server's certif.
BigInteger exp = new BigInteger("010001",16); // 65537 as usual
BigInteger decrypt_sign = new BigInteger(1, signature).modPow(exp, ca_pubkey.getModulus());
System.out.println("Signature after decryption: " +decrypt_sign);
