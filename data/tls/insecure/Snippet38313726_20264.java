public class AClass {
    public void someMethod() throws Exception {
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, null, null);
    }
}
