public class MySocketFactory extends JSSESocketFactory implements SecureSocketFactory {
...
    protected void initFactory() throws IOException {
...
            SSLContext context = getContext();
            this.sslFactory = context.getSocketFactory();
...
    }

    protected SSLContext getContext() throws Exception {
        MyKeyManager myKeyManager = new MyKeyManager();
        KeyManager[] km = new X509KeyManager[] { myKeyManager };
        SSLContext context = SSLContext.getInstance("SSLv3");
        context.init(km, null, null);
        return context;
    }
}
