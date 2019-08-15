import javax.net.ssl.X509TrustManager
import javax.net.ssl.TrustManager
import java.security.cert.X509Certificate
import java.security.cert.CertificateException

def trustAllCerts = [
    new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return null
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }
    }
] as TrustManager[] 
