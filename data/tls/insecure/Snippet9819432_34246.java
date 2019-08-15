TrustManager[] myTrustManager = { new MyTrustManager() };
SSLContext ctx = SSLContext.getInstance("TLS");

if (sendClientCerts) {   //send my certs for authentication to the server

    X509KeyManager kmsd = ...

    ctx.init(kmsd, myTrustManager, null);
} 
else {

    ctx.init(null, myTrustManager, null);
}

// Finally get a SSL Socket Factory and return it
SSLScketFactory ssf = ctx.getSocketFactory();

SSLSocket socket = ssf.createSocket(...);
