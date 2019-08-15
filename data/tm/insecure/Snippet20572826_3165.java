import groovyx.net.http.*
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager
import javax.net.ssl.SSLContext
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManager
import java.security.SecureRandom
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.scheme.SchemeRegistry

import groovy.util.logging.*

@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.6')
@Grab( 'log4j:log4j:1.2.16' )
@Log4j
class WebserviceTest {
  static void main (String[] args) {

    def http = new HTTPBuilder('https://localhost:8443/project/service/')
    log.debug "http is instanceof ${http.getClass()}" //this is printed

    //=== SSL UNSECURE CERTIFICATE ===
   def sslContext = SSLContext.getInstance("SSL")              
   sslContext.init(null, [ new X509TrustManager() {
     public X509Certificate[] getAcceptedIssuers() {null}
     public void checkClientTrusted(X509Certificate[] certs, String authType) { }
     public void checkServerTrusted(X509Certificate[] certs, String authType) { }
   } ] as TrustManager[], new SecureRandom())
   def sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
   def httpsScheme = new Scheme("https", sf, 4483)
   http.client.connectionManager.schemeRegistry.register( httpsScheme )
   //================================

    //the next line throws the exeption...
    http.post(path: '/method', contentType: ContentType.JSON, query: [param: '1000']) { resp, reader ->
        println "resp is instanceof ${resp.getClass()}"
        println "and reader is instanceof ${reder.getClass()}"

        println "response status: ${resp.statusLine}"
        println 'Headers: -----------'
        resp.headers.each { h ->
          println " ${h.name} : ${h.value}"
        }
        println 'Response data: -----'
        System.out << reader
        println '\n--------------------'

    }
  } 
}
