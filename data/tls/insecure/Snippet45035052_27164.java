//Configure trustManager if needed
TrustManager[] trustManagers = null;

//Configure keyManager to select the private key and the certificate chain from KeyChain
KeyManager keyManager = KeyChainKeyManager.fromAlias(
            context, mClientCertAlias);

//Configure SSLContext
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(new KeyManager[] {keyManager}, trustManagers, null);


//Perform the connection
URL url = new URL( versionUrl );
HttpsURLConnection urlConnection = ( HttpsURLConnection ) url.openConnection();
urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
//urlConnection.setHostnameVerifier(hostnameVerifier);  //Configure hostnameVerifier if needed
urlConnection.setConnectTimeout( 10000 );
InputStream in = urlConnection.getInputStream();
