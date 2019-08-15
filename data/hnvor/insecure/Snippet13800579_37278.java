SSLContext sc = SSLContext.getInstance("SSLv3");
sc.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
SSLContext.setDefault(sc);

URL url = new URL(URLStr);
HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
/**
* Create our own hostname verifier which always returns true
* to guarantee that our certificates are accepted
*/
conn.setHostnameVerifier(new HostnameVerifier(){
@Override
public boolean verify(String string, SSLSession ssls) {
    return true;
}
});
