SSLContext c = SSLContext.getInstance("SSL");
TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
tmf.init(yourKeystore);
TrustManager tm = tmf.getTrustManagers()[0];
tm.
c.init(null, tm, null);
