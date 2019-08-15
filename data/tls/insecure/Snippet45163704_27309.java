  public static final String CERTIFICATO=("-----BEGIN CERTIFICATE-----\n" +
             "MIIFuDCCBKCgA....kqhkiG9w0BAQsFADBm\n" +
             "MQswCQYDVQQGEwJ...SW5jLjEdMBsGA1UECxMU\n" +
             "RG9tYWluIFZhbGlk....ERWIFNTTCBD\n" +
             "QSAtIEczMB4XDTE....k1OVowGzEZMBcGA1UE\n" +
             "AwwQdGVzdDIuYmNzb2....ADggEPADCCAQoC\n" +
             "ggEBAMEIbF7hHdy2...d6nWJE0zRSG1IzL6qTe\n" +
             "tan8UGyIUdHTx0Cy...VRhchXab628VxP\n" +
             "1Ngd2ffFUKBO9...N0/Fphr\n" +
             "9yKJCwgbcb2PAsH....knT5q\n" +
             "T6qkfug0jBVdKmaG5...Vg694vGZYVkFi\n" +
             "NbDFAaF7f1oS...BKCEHRl\n" +
             "c3QyLmJjc29mdC....hpodHRw\n" +
             "Oi8vZ3Quc3ltY2I...gZngQwBAgEw\n" +
             "gYQwPwYIKwY...N0LmNvbS9yZXNvdXJj\n" +
             "ZXMvcmVw...RwczovL3d3dy5n\n" +
             "ZW90cnVzdC5jb...WwwHwYDVR0jBBgw\n" +
             "FoAUrWUihZ...B0GA1UdJQQW\n" +
             "MBQGCCsGAQU....wHwYIKwYBBQUH\n" +
             "MAGGE2h0d...0dHA6Ly9ndC5z\n" +
             "eW1jYi5jb20vZ3Q...wDxAHYA3esdK3oN\n" +
             "T6Ygi4GtgWhwf...RzBFAiBp54lk\n" +
             "UV/yv5lgSW0w...K4uzyiBfJQMMe1\n" +
             "OVzA+x/INw9...5G9+443fNDsgN\n" +
             "3BAAAAFc9Ao..WRNuoF/GR\n" +
             "ckf1umsC...NBgkqhkiG\n" +
             "9w0BAQsFAA...lcKFn1fk\n" +
             "N6tnsHI...JKA4fjAgV\n" +
             "k5VMllg...pkVGqing\n" +
             "h+pkAJg19u...suEdhrpK8c\n" +
             "6ZU6kjpyNuIiVX9nAEA2..LkKM3Yi6LE\n" +
             "N9TlYfz4B...nQd3bZAg==\n" +
             "-----END CERTIFICATE-----\n");

public static SSLSocketFactory socket=null;

 static {
     // Load CAs from an InputStream
     // (could be from a resource or ByteArrayInputStream or ...)
     InputStream in=null;
     try {
         CertificateFactory cf = CertificateFactory.getInstance("X.509");

         Certificate ca;

         in = new ByteArrayInputStream(Costanti.CERTIFICATO.getBytes());

         ca = cf.generateCertificate(in);

         // Create a KeyStore containing our trusted CAs
         String keyStoreType = KeyStore.getDefaultType();
         KeyStore keyStore = KeyStore.getInstance(keyStoreType);
         keyStore.load(null, null);
         keyStore.setCertificateEntry("ca", ca);

         // Create a TrustManager that trusts the CAs in our KeyStore
         String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
         TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
         tmf.init(keyStore);

         // Create an SSLContext that uses our TrustManager
         SSLContext context = SSLContext.getInstance("TLS");
         context.init(null, tmf.getTrustManagers(), null);
         socket=context.getSocketFactory();
     }catch (Exception e){
         e.printStackTrace();
         LogManage.logError(LogManage.Type.Connection, Costanti.class, null, "Problema con SSL conf");
         socket=null;
     }
     finally {
         if (in!=null)
             try {
                 in.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
     }
 }
