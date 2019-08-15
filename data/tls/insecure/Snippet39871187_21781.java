@Component
public class SomeComponent {

    @PostConstruct
    public void sslContextConfiguration() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
           SSLContext.setDefault(sslContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
