 package apps

 import grails.converters.*
 import org.codehaus.groovy.grails.web.json.*; // package containing JSONObject,  JSONArray,...
 import groovyx.net.http.*
 import static groovyx.net.http.ContentType.*
 import static groovyx.net.http.Method.*

 import javax.net.ssl.X509TrustManager
 import javax.net.ssl.SSLContext
 import java.security.cert.X509Certificate
 import javax.net.ssl.TrustManager
 import java.security.SecureRandom
 import org.apache.http.conn.ssl.SSLSocketFactory
 import org.apache.http.conn.scheme.Scheme
 import org.apache.http.conn.scheme.SchemeRegistry
 import org.apache.http.conn.ssl.X509HostnameVerifier


class PaypalController {

   def index() {
    def paypalUrl = "https://pilot-payflowpro.paypal.com?PARTNER=PayPal&VENDOR=ROAdvanced&USER=ROAdvanced&PWD=joespizza1&TRXTYPE=S&MODE=TEST&AMT=40&CREATESECURETOKEN=Y&SECURETOKENID=12528208de1413abc3d60c86cdr87"
    //def paypalUrl = "https://pilot-payflowpro.paypal.com"
    println "Making new http Builder with paypalURL ..."
    def http = new HTTPBuilder(paypalUrl)

    println "Now setting timeouts ..."
    http.getClient().getParams().setParameter("http.connection.timeout", new Integer(12000))
    http.getClient().getParams().setParameter("http.socket.timeout", new Integer(30000))

    //=== SSL UNSECURE CERTIFICATE ===

    println "Don't know if needed, but since timeouts don't work to get a response, setting up SSL bypass"

    def sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, [new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {null }

        public void checkClientTrusted(X509Certificate[] certs, String authType) { }

        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
    }] as TrustManager[], new SecureRandom())

    //SSLSocketFactory sf = new org.apache.http.conn.ssl.SSLSocketFactory(sslContext, org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)

    SSLSocketFactory sf = new org.apache.http.conn.ssl.SSLSocketFactory(sslContext)
    sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
    def httpsScheme = new Scheme("https", sf, 443)
    http.client.connectionManager.schemeRegistry.register(httpsScheme)


    println "Now doing the get"
    // perform a GET request, expecting JSON response data
    try {
        http.request(GET, ContentType.ANY) {
            println "Issued the get waiting for the response"
            // Sleeping doesn't help
            //Thread.sleep(2000)
            // println "Done sleeping, looking to process success"

            response.success = { resp, any ->
                println "in success code"
                println "My response handler got response: ${resp.statusLine}"
                println "Response length: ${resp.headers.'Content-Length'}"
                assert resp.status == 200

                def result = any.results;

                render(view: "index", model: [message: "Request sent", result: result]);
            }
            println "past the success code"
        }//end of request
    } catch (groovyx.net.http.HttpResponseException ex) {
        println "Had response exception ...."
        ex.printStackTrace()
        return null
    } catch (java.net.ConnectException ex) {
        println "Had connection exception ...."
        ex.printStackTrace()
        return null
    }

    finally {
        http.shutdown()
    }
}//end of method


def fail() {}

def success() {}
