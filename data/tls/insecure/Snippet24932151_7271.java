HostnameVerifier hostnameVerifier = new HostnameVerifier() {

    @Override
    public boolean verify(String hostname, SSLSession session)
    {
        if (hostname.equals(SSL_HOSTVERIFIER_TAG))
            return true;
        else
            return false;
    }
};

CertificateFactory cf = CertificateFactory.getInstance("X.509");
InputStream caInput = mContext.getResources().openRawResource(R.raw.firm_gmbh);

Certificate ca;
try
{
    ca = cf.generateCertificate(caInput);
} finally{
    caInput.close();
}

String keyStoreType = KeyStore.getDefaultType();
KeyStore keyStore = KeyStore.getInstance(keyStoreType);
keyStore.load(null, null);
keyStore.setCertificateEntry("ca", ca);

String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
tmf.init(keyStore);

SSLContext context = SSLContext.getInstance("TLS");
context.init(null, tmf.getTrustManagers(), null);

String authString = mUserName + ":" + mPassword;
byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
String authStringEnc = new String(authEncBytes);

URL url = new URL(SSL_URL);
HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
urlConnection.setSSLSocketFactory(context.getSocketFactory());
urlConnection.setHostnameVerifier(hostnameVerifier);
urlConnection.setRequestMethod("GET");
urlConnection.setRequestProperty("soapaction", SOAP_ACTION);

int res = urlConnection.getResponseCode();
String message = urlConnection.getResponseMessage();
InputStream in = urlConnection.getInputStream();
String msg = convertStreamToString(in);
