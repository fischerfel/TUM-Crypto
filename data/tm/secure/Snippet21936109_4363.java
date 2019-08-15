public class EasyX509TrustManager implements X509TrustManager 
{  
    private X509TrustManager standardTrustManager = null;  

    /** 
     * Constructor for EasyX509TrustManager. 
     */  
    public EasyX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException 
    {  
        super();  
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());  
        factory.init(keystore);  
        TrustManager[] trustmanagers = factory.getTrustManagers();  
        if (trustmanagers.length == 0) 
        {  
            throw new NoSuchAlgorithmException("no trust manager found");  
        }  
        this.standardTrustManager = (X509TrustManager) trustmanagers[0];  
    }  

    /** 
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[],String authType) 
     */  
    public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException 
    {
        standardTrustManager.checkClientTrusted(certificates, authType);  
    }  

    /** 
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[],String authType) 
     */  
    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException 
    {  
        X509Certificate c = certificates[0];
        String name = c.getIssuerDN().getName();
        if(!"bla bla".equals(name))
            throw new CertificateException("OMG! it is not bla bla!");
        standardTrustManager.checkServerTrusted(certificates, authType);    
    }  

    /** 
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers() 
     */  
    public X509Certificate[] getAcceptedIssuers() 
    {  
        return this.standardTrustManager.getAcceptedIssuers();  
    }    
}  
