    SSLSocketFactory ssl =  (SSLSocketFactory)http.getConnectionManager().getSchemeRegistry().getScheme( "https" ).getSocketFactory(); 
    ssl.setHostnameVerifier( SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
