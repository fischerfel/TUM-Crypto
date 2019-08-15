val is = this.getClass().getResourceAsStream("/cacert.crt")

val cf: CertificateFactory = CertificateFactory.getInstance("X.509")

val caCert: X509Certificate = cf.generateCertificate(is).asInstanceOf[X509Certificate];

val tmf: TrustManagerFactory  = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
val ks: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(null); 
ks.setCertificateEntry("caCert", caCert);

tmf.init(ks);

implicit val sslContext: SSLContext = SSLContext.getInstance("TLS");
sslContext.init(null, tmf.getTrustManagers(), null);

implicit val timeout: Timeout = Timeout(15.seconds)
import spray.httpx.RequestBuilding._

val respFuture = (IO(Http) ? Post( uri=Uri(url), content="my content")).mapTo[HttpResponse]