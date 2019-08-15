public class HTTPRequest {
    private static SSLContext context;

    static {
        try {
            context = setContext();
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
            // or maybe just eat it, but that would leave context null
        }
    }

    private static SSLContext setContext() throws NoSuchAlgorithmException    {
        return SSLContext.getInstance("TLSv1.2");
    }
}
