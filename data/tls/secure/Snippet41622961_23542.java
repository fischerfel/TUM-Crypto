public class HTTPRequest {
    private static SSLContext context;

    static {
        try {
            context = SSLContext.getInstance("TLSv1.2")
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
