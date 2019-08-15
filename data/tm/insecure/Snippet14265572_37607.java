public class LoopSound {
    public static void main(String[] args) throws Exception {
        HostnameVerifier hv = new HostnameVerifier() {

            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                System.out.println("Warning: URL Host: " + urlHostName
                        + " vs. " + session.getPeerHost());
                return true;
            }
        };
        // Now you are telling the JRE to trust any https server.
        // If you know the URL that you are connecting to then this should
        // not be a problem
        try {
            trustAllHttpsCertificates();
        } catch (Exception e) {
            System.out.println("Trustall" + e.getStackTrace());
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        URL url = new URL(
          "https://74.127.51.154/SiPbx/playback.php?access=subscriber&login=501%40mix&domain=mix.nms.mixnetworks.net&user=501&type=vmail&file=vm-20130109213353000125_netsapiens_com.wav&time=20130110170638&auth=de5dda39287604a88fc4b80c467e161d&submit=PLAY");
        Clip clip = AudioSystem.getClip();
        AudioInputStream ais = AudioSystem.
          getAudioInputStream( url );
        clip.open(ais);
        clip.loop(0);
        javax.swing.JOptionPane.
          showMessageDialog(null, "Close to exit!");
      }

     private static void trustAllHttpsCertificates() throws Exception {

            // Create a trust manager that does not validate certificate chains:

            javax.net.ssl.TrustManager[] trustAllCerts =

            new javax.net.ssl.TrustManager[1];

            javax.net.ssl.TrustManager tm = new TempTrustedManager();

            trustAllCerts[0] = tm;

            javax.net.ssl.SSLContext sc =

            javax.net.ssl.SSLContext.getInstance("SSL");

            sc.init(null, trustAllCerts, null);

            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(

            sc.getSocketFactory());

        }
     public static class TempTrustedManager implements
        javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public boolean isServerTrusted(
            java.security.cert.X509Certificate[] certs) {
        return true;
    }

    public boolean isClientTrusted(
            java.security.cert.X509Certificate[] certs) {
        return true;
    }

    public void checkServerTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }

    public void checkClientTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }
}
