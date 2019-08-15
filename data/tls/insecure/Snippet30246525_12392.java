@Override
public SSLContext getSslContext(String camingoCert, String caminhoCacerts, String senhaCertificado){
    SSLContext sc = null;
    try {
        sc = SSLContext.getInstance("SSL");
        KeyManager[] km = getKeyManager(camingoCert, senhaCertificado);
        TrustManager[] tm = getTrustManager(caminhoCacerts);

        sc.init(km, tm, null);

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }
    return sc;
}
