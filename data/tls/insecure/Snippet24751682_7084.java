public SSLContext getSSLContext(String tspath) 
        throws Exception {

      TrustManager[] trustManagers = new TrustManager[] { 

        new ReloadableX509TrustManager(tspath) 
      };
      SSLContext sslContext = SSLContext.getInstance("TLS");

      sslContext.init(null, trustManagers, null);

      return sslContext;

    }

SSLContext sslContext=getSSLContext("C:\\Java\\jdk1.6.0_38\\jre\\bin\\quid.jks");
SSLSocketFactory socketFactory = sslContext.getSocketFactory();
URL pickUrl = new URL(pickupLocation);
URLConnection urlConn = pickUrl.openConnection();
HttpsURLConnection httpsURLConn = (HttpsURLConnection)urlConn;
httpsURLConn.setSSLSocketFactory(socketFactory);
String encoding = urlConn.getContentEncoding();   
InputStream is = urlConn.getInputStream();    
InputStreamReader streamReader = new InputStreamReader(is, encoding != null
? encoding : "UTF-8");
