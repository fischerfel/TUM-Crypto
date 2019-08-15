public class DummyTrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] cert, String authType) {
        // everything is trusted
    }

    public void checkServerTrusted(X509Certificate[] cert, String authType) {
        // everything is trusted
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}

the third class AllowAll to set java mail propeties:

public class AllowAll {
         public void setsslproperties()  {

                 //DummySSLSocketFactory easy = new EasySSLProtocolSocketFactory();
                 Properties props = System.getProperties();
                 props.setProperty("mail.imap.ssl.enable", "true");
             props.setProperty("mail.imap.ssl.socketFactory.class","com.mycompany.imapssl.DummySSLSocketFactory");
                 props.setProperty("mail.imap.ssl.socketFactory.fallback", "false");
                 props.setProperty("mail.imap.socketFactory.port", "993");
                 Session session = Session.getInstance(props, null);

        }

}
