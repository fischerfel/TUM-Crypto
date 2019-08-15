public class TrustManagerNoCritical implements X509TrustManager  {

protected X509TrustManager trustManager;

public TrustManagerNoCritical(X509TrustManager realTrustManager) {
    trustManager = realTrustManager;
}

@Override
public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    trustManager.checkClientTrusted(chain, authType);
}

@Override
public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    List<X509Certificate> certifs = new ArrayList<X509Certificate>();       

    for (X509Certificate certif : chain) {
        certifs.add(new CertificateNoCritical(certif));
    }

    X509Certificate[] newChain = new X509Certificate[certifs.size()];  
    newChain = certifs.toArray(newChain);

    trustManager.checkServerTrusted(newChain, authType);
}

@Override
public X509Certificate[] getAcceptedIssuers() {
    return trustManager.getAcceptedIssuers();
}

}
