        URL dstUrl = new URL("https://myserver:8090")
        URLConnection result = dstUrl.openConnection();


        System.out.println("Protocol: "+dstUrl.getProtocol()+", is HTTPS:"+(result instanceof HttpsURLConnection)); 
// Echo: Protocol HTTPS is HTTPS:false

        if (result instanceof HttpsURLConnection) {//will not reach
            try {
                HttpsURLConnection conn = (HttpsURLConnection) result;
                conn.setSSLSocketFactory(sslContext.getSocketFactory());
                conn.setHostnameVerifier(defaultHostVerifier);
            } catch (Exception e) {
                //sout will not ignate
            }
        }
        return (HttpURLConnection) result;
