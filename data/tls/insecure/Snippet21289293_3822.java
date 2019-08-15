// Create an SSLContext that uses our TrustManager
SSLContext context = SSLContext.getInstance("TLS");
context.init(null, trustAllCerts, new SecureRandom());

SSLParameters params = context.getSupportedSSLParameters();
String[] suites = params.getCipherSuites();
System.out.println("Java version : " + System.getProperty("java.runtime.version"));
System.out.println("Connecting with " + suites.length + " cipher suites supported:");

for (int i = 0; i < suites.length; i++) {
    System.out.println();
    System.out.print(" ********* ");
    System.out.print(suites[i]);
    System.out.print(" ********* ");
}

Java version: 1.7.0_51-b13
Connecting with 63 cipher suites supported:

 ********* TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256 ********* 
 ********* TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256 ********* 
 ********* TLS_RSA_WITH_AES_128_CBC_SHA256 ********* 
 ********* TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256 ********* 
 ********* TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256 ********* 
 ********* TLS_DHE_RSA_WITH_AES_128_CBC_SHA256 ********* 
 ********* TLS_DHE_DSS_WITH_AES_128_CBC_SHA256 ********* 
 ********* TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA ********* 
 ********* TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA ********* 
 ********* TLS_RSA_WITH_AES_128_CBC_SHA ********* 
 ********* TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA ********* 
 ********* TLS_ECDH_RSA_WITH_AES_128_CBC_SHA ********* 
 ********* TLS_DHE_RSA_WITH_AES_128_CBC_SHA ********* 
 ********* TLS_DHE_DSS_WITH_AES_128_CBC_SHA ********* 
 ********* TLS_ECDHE_ECDSA_WITH_RC4_128_SHA ********* 
 ********* TLS_ECDHE_RSA_WITH_RC4_128_SHA ********* 
 ********* SSL_RSA_WITH_RC4_128_SHA ********* 
 ********* TLS_ECDH_ECDSA_WITH_RC4_128_SHA ********* 
 ********* TLS_ECDH_RSA_WITH_RC4_128_SHA ********* 
 ********* TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA ********* 
 ********* TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA ********* 
 ********* SSL_RSA_WITH_3DES_EDE_CBC_SHA ********* 
 ********* TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA ********* 
 ********* TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA ********* 
 ********* SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA ********* 
 ********* SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA ********* 
 ********* SSL_RSA_WITH_RC4_128_MD5 ********* 
 ********* TLS_EMPTY_RENEGOTIATION_INFO_SCSV ********* 
 ********* TLS_DH_anon_WITH_AES_128_CBC_SHA256 ********* 
 ********* TLS_ECDH_anon_WITH_AES_128_CBC_SHA ********* 
 ********* TLS_DH_anon_WITH_AES_128_CBC_SHA ********* 
 ********* TLS_ECDH_anon_WITH_RC4_128_SHA ********* 
 ********* SSL_DH_anon_WITH_RC4_128_MD5 ********* 
 ********* TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA ********* 
 ********* SSL_DH_anon_WITH_3DES_EDE_CBC_SHA ********* 
 ********* TLS_RSA_WITH_NULL_SHA256 ********* 
 ********* TLS_ECDHE_ECDSA_WITH_NULL_SHA ********* 
 ********* TLS_ECDHE_RSA_WITH_NULL_SHA ********* 
 ********* SSL_RSA_WITH_NULL_SHA ********* 
 ********* TLS_ECDH_ECDSA_WITH_NULL_SHA ********* 
 ********* TLS_ECDH_RSA_WITH_NULL_SHA ********* 
 ********* TLS_ECDH_anon_WITH_NULL_SHA ********* 
 ********* SSL_RSA_WITH_NULL_MD5 ********* 
 ********* SSL_RSA_WITH_DES_CBC_SHA ********* 
 ********* SSL_DHE_RSA_WITH_DES_CBC_SHA ********* 
 ********* SSL_DHE_DSS_WITH_DES_CBC_SHA ********* 
 ********* SSL_DH_anon_WITH_DES_CBC_SHA ********* 
 ********* SSL_RSA_EXPORT_WITH_RC4_40_MD5 ********* 
 ********* SSL_DH_anon_EXPORT_WITH_RC4_40_MD5 ********* 
 ********* SSL_RSA_EXPORT_WITH_DES40_CBC_SHA ********* 
 ********* SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA ********* 
 ********* SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA ********* 
 ********* SSL_DH_anon_EXPORT_WITH_DES40_CBC_SHA ********* 
 ********* TLS_KRB5_WITH_RC4_128_SHA ********* 
 ********* TLS_KRB5_WITH_RC4_128_MD5 ********* 
 ********* TLS_KRB5_WITH_3DES_EDE_CBC_SHA ********* 
 ********* TLS_KRB5_WITH_3DES_EDE_CBC_MD5 ********* 
 ********* TLS_KRB5_WITH_DES_CBC_SHA ********* 
 ********* TLS_KRB5_WITH_DES_CBC_MD5 ********* 
 ********* TLS_KRB5_EXPORT_WITH_RC4_40_SHA ********* 
 ********* TLS_KRB5_EXPORT_WITH_RC4_40_MD5 ********* 
 ********* TLS_KRB5_EXPORT_WITH_DES_CBC_40_SHA ********* 
 ********* TLS_KRB5_EXPORT_WITH_DES_CBC_40_MD5 ********* INFO - Received response from post device of : 
