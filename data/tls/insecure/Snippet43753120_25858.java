private static OkHttpClient getSSLClient(Context context)
throws
NoSuchAlgorithmException,
KeyStoreException,
KeyManagementException,
CertificateException,
IOException {

OkHttpClient client;
SSLContext sslContext;
SSLSocketFactory sslSocketFactory;
TrustManager[] trustManagers;
TrustManagerFactory trustManagerFactory;
X509TrustManager trustManager;

trustManagerFactory =
  TrustManagerFactory.getInstance(
    TrustManagerFactory.getDefaultAlgorithm());

trustManagerFactory.init(
  readKeyStore(
    context));

trustManagers =
  trustManagerFactory.getTrustManagers();

if (trustManagers.length != 1 ||
    !(trustManagers[0] instanceof X509TrustManager)) {

  throw new IllegalStateException("Unexpected default trust managers:"
                                  + Arrays.toString(trustManagers));
}

trustManager =
  (X509TrustManager) trustManagers[0];

sslContext =
  SSLContext.getInstance("TLS");

sslContext.init(
  null,
  new TrustManager[]{trustManager},
  null);

sslSocketFactory =
  sslContext.getSocketFactory();

client =
  new OkHttpClient.Builder()
    .sslSocketFactory(
      sslSocketFactory,
      trustManager)
    .build();

return client;
}

/**
* Get keys store. Key file should be encrypted with pkcs12 standard. It    can be done with standalone encrypting java applications like "keytool". File password is also required.
*
* @param context Activity or some other context.
* @return Keys store.
* @throws KeyStoreException
* @throws CertificateException
* @throws NoSuchAlgorithmException
* @throws IOException
*/
private static KeyStore readKeyStore(Context context)
throws
KeyStoreException,
CertificateException,
NoSuchAlgorithmException,
IOException {

KeyStore keyStore;
char[] PASSWORD = "12345678".toCharArray();
ArrayList<InputStream> certificates;
int certificateIndex;
InputStream certificate;

certificates = new ArrayList<>();

certificates.add(
  context
    .getResources()
    .openRawResource(
      R.raw.ssl_pkcs12));

keyStore =
  KeyStore.getInstance(
    "pkcs12");

for (
  certificateIndex = 0;
  certificateIndex < certificates.size();
  certificateIndex++) {

  certificate =
    certificates.get(
      certificateIndex);

  try {
    keyStore.load(
      certificate,
      PASSWORD);
  } finally {
    if (certificate != null) {
      certificate.close();
    }
  }
}

keyStore.size();

return keyStore;
}
