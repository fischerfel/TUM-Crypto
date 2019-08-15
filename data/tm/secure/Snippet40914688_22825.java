    public class CustomTrustManager implements X509TrustManager {

   private X509TrustManager trustManager;
   // If a connection was previously attempted and failed the certificate check, that certificate chain will be saved here.
   private Certificate[] rejectedCertificates = null;
   private Certificate[] encounteredCertificates = null;
   private KeyStore keyStore = null;
   private Logger logger;

   /**
    * Constructor
    *
    * @param loggerFactory
    *           see {@link InstanceLoggerFactory}
    */
   public CustomTrustManager(InstanceLoggerFactory loggerFactory) {
      try {
         this.logger = loggerFactory.getLogger(CustomTrustManager.class);
         keyStore = KeyStore.getInstance("JKS");
         // a keyStore must be initialized with load, even if certificate trust is not file based.
         keyStore.load(null, null);

         System.setProperty("com.sun.net.ssl.checkRevocation", "true");
         Security.setProperty("ocsp.enable", "true");
      } catch (Exception ex) {
         logger.error("Problem initializing keyStore", ex);
      }
   }

   /**
    * Returns the rejected certificate based on the last usage
    */
   public Certificate[] getRejectedCertificateChain() {
      return rejectedCertificates;
   }

   /**
    * Returns the encountered certificates based on the last usage
    */
   public Certificate[] getEncounteredCertificates() {
      return encounteredCertificates;
   }

   @Override
   public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      if (trustManager != null) {
         trustManager.checkClientTrusted(chain, authType);
      }
   }

   /**
    * Checks if a server is trusted, based on the wrapped keyStore's trust
    * anchors. This will also capture the encountered certificate chain and, if
    * trust fails, the rejected certificate chain.
    */
   @Override
   public void checkServerTrusted(X509Certificate[] chain, String authType) throws CustomCertificateException {
      // Capture the certificate if it fails
      try {
         encounteredCertificates = chain;
         if (trustManager != null) {
            trustManager.checkServerTrusted(chain, authType);
         } else {
            throw new RuntimeException("Trust manager is null");
         }
      } catch (CertificateException ex) {
         rejectedCertificates = chain;
         throw new CustomCertificateException(ex, rejectedCertificates);
      } catch (Exception ex) {
         rejectedCertificates = chain;
         throw new CustomCertificateException(new CertificateException(ex), rejectedCertificates);
      }
   }

   @Override
   public X509Certificate[] getAcceptedIssuers() {
      return trustManager == null ? new X509Certificate[0] : trustManager.getAcceptedIssuers();
   }

   /**
    * initializes the internal trust manager with all known certificates
    * certificates are stored in the keyStore object
    */
   private void initTrustManager() {
      try {
         // initialize a new TMF with our keyStore
         TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX", "SunJSSE");

         // keyStore must not be empty
         CertPathParameters pkixParams = new PKIXBuilderParameters(keyStore, new X509CertSelector());
         ((PKIXBuilderParameters) pkixParams).setRevocationEnabled(true);

         tmf.init(new CertPathTrustManagerParameters(pkixParams));

         // acquire X509 trust manager from factory
         TrustManager tms[] = tmf.getTrustManagers();
         for (TrustManager tm : tms) {
            if (tm instanceof X509TrustManager) {
               trustManager = (X509TrustManager) tm;
               break;
            }
         }
      } catch (Exception ex) {
         logger.error("Problem initializing trust manager", ex);
      }
   }

 ...
}
