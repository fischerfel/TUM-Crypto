import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;


/**
 * Code sample for Jetty {@link HttpClient} with HTTPS, in a completely standalone fashion.
 * Use create-chains.sh and create-empty.sh to generate completely standalone certificates.
 */
public class JettyHttpsForStackOverflow {

  public static void main( final String... arguments ) throws Exception {
    System.setProperty( "javax.net.debug", "all" ) ;

    try {
      if( arguments.length == 0 || "server".equals( arguments[ 0 ] ) ) {
        runServer() ;
      }
      if( arguments.length == 0 || "client".equals( arguments[ 0 ] ) ) {
        runClient() ;
      }
    } catch( Exception e ) {
      e.printStackTrace() ;
      System.exit( 1 ) ; // Avoids keeping the port open.
    }

  }

  private static void runServer() throws Exception {
    final KeyStore keyStore = loadKeystore() ;
    final SSLContext sslContext = createSslContext(
        keyStore,
        KEYPASS,
        newTrustManagers( keyStore, CERTIFICATE_ALIAS )
    ) ;

    final SslContextFactory sslContextFactory = new SslContextFactory() {
      @Override
      public SSLEngine newSslEngine() {
        return sslContext.createSSLEngine() ;
      }
      @Override
      public SSLEngine newSslEngine( final String host, final int port ) {
        return sslContext.createSSLEngine( host, port ) ;
      }
    } ;
    sslContextFactory.setAllowRenegotiate( true ) ;
    sslContextFactory.setNeedClientAuth( false ) ;
    sslContextFactory.setWantClientAuth( false ) ;
    sslContextFactory.setKeyStorePath( keyStore.toString() ) ; // Better logging.
    sslContextFactory.setKeyStore( keyStore ) ;
    sslContextFactory.setCertAlias( CERTIFICATE_ALIAS ) ;
    sslContextFactory.setKeyManagerPassword( KEYPASS ) ;

    final SslSelectChannelConnector sslConnector =
        new SslSelectChannelConnector( sslContextFactory ) ;
    sslConnector.setPort( PORT ) ;
    sslConnector.open() ;

    final Server jettyServer = new Server() ;
    jettyServer.addConnector( sslConnector ) ;

    jettyServer.start() ;
  }

  public static void runClient() throws Exception {
    final KeyStore keyStore = loadTruststore() ;

    final HttpClient httpClient = new HttpClient() ;
    httpClient.getSslContextFactory().setKeyStore( keyStore ) ; // Better logging.
    httpClient.getSslContextFactory().setKeyStorePassword( "storepwd" ) ;
    httpClient.getSslContextFactory().setKeyManagerPassword( KEYPASS ) ;
    httpClient.setConnectorType( HttpClient.CONNECTOR_SELECT_CHANNEL ) ;
    httpClient.setConnectorType(HttpClient.CONNECTOR_SOCKET);


    // Don't need that because shipping our own certificate in the truststore.
    // Anyways, it blows when set to true.
//    httpClient.getSslContextFactory().setValidateCerts( false ) ;

    httpClient.start() ;

    final ContentExchange contentExchange = new ContentExchange() ;
    contentExchange.setURI( new URL( "https://localhost:" + PORT ).toURI() ) ;
    contentExchange.setTimeout( 36_000_000 ) ; // Leave time for debugging.
    httpClient.send( contentExchange ) ;
    contentExchange.waitForDone() ;
    assert( contentExchange.getStatus() == ContentExchange.STATUS_COMPLETED ) ;
  }

  private static SSLContext createSslContext(
      final KeyStore keyStore,
      final String keypass,
      final TrustManager[] trustManagers
  ) {
    try {
      final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance( "SunX509" ) ;
      keyManagerFactory.init( keyStore, keypass == null ? null : keypass.toCharArray() ) ;
      final KeyManager[] keyManagers = keyManagerFactory.getKeyManagers() ;
      final SecureRandom secureRandom = new SecureRandom() ;

      final SSLContext sslContext = SSLContext.getInstance( "TLS" ) ;
      sslContext.init(
          keyManagers,
          trustManagers,
          secureRandom
      ) ;
      return sslContext ;
    } catch( NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException
        | KeyManagementException e
    ) {
      throw new RuntimeException( e ) ;
    }
  }



  private static TrustManager[] newTrustManagers(
      final KeyStore keyStore,
      final String certificateAlias
  ) {
    try {
      final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance( "SunX509" ) ;
      trustManagerFactory.init( keyStore ) ;
      final TrustManager[] trustManagers ;
      if( certificateAlias == null ) {
        trustManagers = trustManagerFactory.getTrustManagers() ;
      } else {
        final Certificate certificate = keyStore.getCertificate( certificateAlias ) ;
        final X509Certificate[] x509Certificates ;
        if( certificate == null ) {
          x509Certificates = new X509Certificate[ 0 ] ;
        } else {
          x509Certificates = new X509Certificate[] { ( X509Certificate ) certificate } ;
        }
        trustManagers = new TrustManager[] { newX509TrustManager( x509Certificates ) } ;

      }
      return trustManagers ;
    } catch( KeyStoreException | NoSuchAlgorithmException e ) {
      throw new RuntimeException( e );
    }

  }

  private static final TrustManager newX509TrustManager( final X509Certificate[] certificates ) {
    return new X509TrustManager() {

      public X509Certificate[] getAcceptedIssuers() {
        return certificates ;
      }

      public void checkClientTrusted(
          final X509Certificate[] certs,
          final String authType
      ) { ; }

      public void checkServerTrusted(
          final X509Certificate[] certs,
          final String authType
      ) { ; }
    } ;
  }


  public static KeyStore loadKeystore()
      throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException
  {
    return loadKeystore( KEYSTORE_RESOURCE_URL ) ;
  }

  public static KeyStore loadTruststore()
      throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException
  {
    return loadKeystore( TRUSTSTORE_RESOURCE_URL ) ;
  }

  public static KeyStore loadKeystore( final URL keystoreResourceUrl )
      throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException
  {
    try( final InputStream inputStream = keystoreResourceUrl.openStream() ) {
      final KeyStore keyStore = KeyStore.getInstance( "JKS" ) ;
      // We don't need the storepass for just reading one password-protected certificate
      // of our own, or a trusted entry.
      keyStore.load( inputStream, null ) ;
      return keyStore ;
    }
  }


  private static final int PORT = 8443 ;

  private static final String CERTIFICATE_ALIAS = "e1";

  private static final String KEYPASS = "Keypass";

  private static final URL KEYSTORE_RESOURCE_URL
      = JettyHttpsForStackOverflow.class.getResource( "my-keystore.jks" ) ;

  private static final URL TRUSTSTORE_RESOURCE_URL
      = JettyHttpsForStackOverflow.class.getResource( "my-truststore.jks" ) ;


}
