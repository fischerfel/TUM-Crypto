    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    SSLContext cpmContext = SSLContext.getInstance("TLSv1.2");
    cpmContext.init(null, null, null);
    SSLSocket socket = (SSLSocket) cpmContext.getSocketFactory().createSocket(ip, port);
    socket.setEnabledProtocols(socket.getEnabledProtocols());
    socket.setEnabledCipherSuites(socket.getEnabledCipherSuites());
    Class c = socket.getClass();
    try {
        Method m = c.getMethod("setUseSessionTickets",boolean.class);
        m.invoke(socket,true);
    } catch (NoSuchMethodException e) {
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    } catch (IllegalArgumentException e) {
        e.printStackTrace();
    } catch (InvocationTargetException e) {
        e.printStackTrace();
    }
    SSLSession session = socket.getSession();
