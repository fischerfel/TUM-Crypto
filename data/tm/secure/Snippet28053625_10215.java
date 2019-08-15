/**
 * Created by Nevuroth on 1/19/15.
 */
public class CustomX509TrustManager implements X509TrustManager{

private X509TrustManager defaultManager = null;

/**
 * Custom constructor for the x509 trust manager. This workaround won't take away from security, but it will drop and accept the self signed cert for our test server at the
 * end of the cert chain, as well as allowing
 *
 * @param keyStore
 * @throws NoSuchAlgorithmException
 * @throws KeyStoreException
 */
public CustomX509TrustManager(KeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException{
    super();
    TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    factory.init(keyStore);
    TrustManager[] trustManagers = factory.getTrustManagers();
    if(trustManagers.length ==0){
        throw new NoSuchAlgorithmException("Failed to find Default trust managers");
    }

    this.defaultManager = (X509TrustManager) trustManagers[0];

}


//we just want the standard functionality for x509
@Override
public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
    defaultManager.checkClientTrusted(x509Certificates, s);
}

/**
 *  Here's where the magic happens, we're going to be compensating for out of order certificates in the X509 header
 *  as well as compensating for self-signed certificates (kind of), by passing the certificate before it in the chain
 *  to the chekc servertrusted method
 *  This won't compensate for purely self-signed certs.... but you can do so by adding it to the accepted issuers method.
 * @param x509Certificates
 * @param s
 * @throws CertificateException
 */

@Override
public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    //Clean the certificates and make sure they are in the proper order.
    int chainln = x509Certificates.length;
    if(x509Certificates.length > 1){
        //Clean the chains by matching issuer and subject fields until we can't continue
        int index;
        boolean foundNext;
        for(index=0; index < x509Certificates.length; ++index){
            foundNext = false;
            for(int nextIndex = index + 1; nextIndex < x509Certificates.length; ++nextIndex){
                //look for the next certificate in the chain.
                if(x509Certificates[index].getIssuerDN().equals(x509Certificates[nextIndex].getSubjectDN())){
                    foundNext = true;
                    //exchange certificates so that 0 through index+1 are in proper order.
                    if(nextIndex != index+1){
                        X509Certificate tempCert = x509Certificates[nextIndex];
                        x509Certificates[nextIndex] = x509Certificates[index+1];
                        x509Certificates[index+1] = tempCert;
                    }
                    break;
                }

            }

            if(!foundNext){
                break;  
            }

        }

        //if the cert is self signed and if it is expired, if so we drop it and pass the rest to checkServerTrusted, hoping we may have a similar bu unexpired trusted root.
        chainln = index +1;
        X509Certificate lastCert = x509Certificates[chainln - 1];
        Date now = new Date();
        if(lastCert.getSubjectDN().equals(lastCert.getIssuerDN()) && now.after(lastCert.getNotAfter())){
            --chainln;
        }
    }

    defaultManager.checkServerTrusted(x509Certificates, s);

}

//you can add an accepted issuer
@Override
public X509Certificate[] getAcceptedIssuers() {
    return this.defaultManager.getAcceptedIssuers();
}
}
