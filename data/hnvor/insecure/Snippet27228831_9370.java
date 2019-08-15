     url = new URL(urlStr);

     HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();

            conn.setHostnameVerifier(new HostnameVerifier()  
            {        
                public boolean verify(String hostname, SSLSession session)  
                {  
                    return true;  
                }  
            });  
