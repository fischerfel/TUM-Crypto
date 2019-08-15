public class CryptoUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public User encrypt(String key, File inputFile, File outputFile)
            throws CryptoException {
        User user = doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
        return user;
    }

    private User doCrypto(int cipherMode, String key, File inputFile,
            File outputFile) throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            //Added
            String eidData = null;
            String randomKey = null;
            User user = new User();
            try{
             DataInputStream dis = new DataInputStream (new FileInputStream (outputFile));

             byte[] datainBytes = new byte[dis.available()];
             dis.readFully(datainBytes);
             dis.close();

             eidData = new String(datainBytes, 0, datainBytes.length);
             randomKey =  UUID.randomUUID().toString();  

             user.setEiddata(eidData);
             user.setRandomkey(randomKey);

            }catch(Exception ex){
                ex.printStackTrace();
            }
            //End

            inputStream.close();
            outputStream.close();
            return user;

        } catch (Exception ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }
}

Exception Trace:
-----------------
Feb 25, 2016 12:40:39 PM org.apache.catalina.core.ApplicationDispatcher invoke
SEVERE: Servlet.service() for servlet mvc-dispatcher threw exception
java.lang.IllegalArgumentException: [?] is not a hexadecimal digit
    at org.apache.catalina.util.RequestUtil.convertHexDigit(RequestUtil.java:316)
    at org.apache.catalina.util.RequestUtil.parseParameters(RequestUtil.java:400)
    at org.apache.catalina.util.RequestUtil.parseParameters(RequestUtil.java:153)
    at org.apache.catalina.core.ApplicationHttpRequest.mergeParameters(ApplicationHttpRequest.java:894)
    at org.apache.catalina.core.ApplicationHttpRequest.parseParameters(ApplicationHttpRequest.java:756)
    at org.apache.catalina.core.ApplicationHttpRequest.getParameterValues(ApplicationHttpRequest.java:416)
    at org.springframework.web.context.request.ServletWebRequest.getParameterValues(ServletWebRequest.java:121)
    at org.springframework.web.method.annotation.RequestParamMethodArgumentResolver.resolveName(RequestParamMethodArgumentResolver.java:171)
    at org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver.resolveArgument(AbstractNamedValueMethodArgumentResolver.java:82)
    at org.springframework.web.method.support.HandlerMethodArgumentResolverComposite.resolveArgument(HandlerMethodArgumentResolverComposite.java:74)
    at org.springframework.web.method.support.InvocableHandlerMethod.getMethodArgumentValues(InvocableHandlerMethod.java:155)
    at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:117)
    at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:96)
    at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:617)
    at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:578)
    at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:80)
    at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:900)
    at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:827)
    at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:882)
    at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:778)
    at javax.servlet.http.HttpServlet.service(HttpServlet.java:621)
    at javax.servlet.http.HttpServlet.service(HttpServlet.java:728)
    at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:305)
    at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210)
    at org.apache.catalina.core.ApplicationDispatcher.invoke(ApplicationDispatcher.java:749)
    at org.apache.catalina.core.ApplicationDispatcher.processRequest(ApplicationDispatcher.java:487)
    at org.apache.catalina.core.ApplicationDispatcher.doForward(ApplicationDispatcher.java:412)
    at org.apache.catalina.core.ApplicationDispatcher.forward(ApplicationDispatcher.java:339)
    at org.springframework.web.servlet.view.InternalResourceView.renderMergedOutputModel(InternalResourceView.java:238)
    at org.springframework.web.servlet.view.AbstractView.render(AbstractView.java:262)
    at org.springframework.web.servlet.DispatcherServlet.render(DispatcherServlet.java:1157)
    at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:927)
    at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:827)
    at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:882)
    at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:778)
    at javax.servlet.http.HttpServlet.service(HttpServlet.java:621)
    at javax.servlet.http.HttpServlet.service(HttpServlet.java:728)
    at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:305)
    at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:210)
    at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:222)
    at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:123)
    at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:472)
    at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:171)
    at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:99)
    at org.apache.catalina.valves.AccessLogValve.invoke(AccessLogValve.java:947)
    at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:118)
    at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:408)
    at org.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1009)
    at org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:589)
    at org.apache.tomcat.util.net.AprEndpoint$SocketProcessor.run(AprEndpoint.java:1852)
    at java.util.concurrent.ThreadPoolExecutor.runWorker(Unknown Source)
    at java.util.concurrent.ThreadPoolExecutor$Worker.run(Unknown Source)
    at java.lang.Thread.run(Unknown Source)
