public class HTTPRequest {
    private static SSLContext context = setContext();
    private static SSLContext setContext() throws NoSuchAlgorithmException    {
        return SSLContext.getInstance("TLSv1.2");
    }
}
