private Result postSSL(String urlString, String postParams, Cookie cookie){
    debugLog("postSSL: "+urlString);
    try {
        URL url = new URL( urlString );
        HttpURLConnection http = null; 

        if (url.getProtocol().toLowerCase().equals("https")) { 
            trustAllHosts(); 
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection(); 
            https.setHostnameVerifier(DO_NOT_VERIFY); 
            http = https; 
        } else { 
            http = (HttpURLConnection) url.openConnection(); 
        }

        http.setRequestMethod("POST");
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 

        http.setUseCaches(false);
        http.setDoInput(true);
        http.setDoOutput(true);

        debugLog("http method:"+http.getRequestMethod()+" doOutput:"+String.valueOf(http.getDoOutput()));

        //Set cookie

        if (cookie.hasCrumbs()){
            //debugLog("postSSL sending cookie :"+cookie.toString());
        http.setRequestProperty("Cookie", cookie.toString());                   
    } 

        //Send request
        if (postParams.length()>0){
            DataOutputStream ostream = new DataOutputStream( http.getOutputStream() );
            ostream.writeBytes(postParams);
            ostream.flush();
            ostream.close();
        }


        //Read response
        Object contents = http.getContent();
        InputStream is = (InputStream) contents;

        StringBuffer responseBuffer = new StringBuffer();
        debugLog("dies here. Null:"+String.valueOf(is==null));
        int c;
        while( ( c = is.read() ) != -1 ) {
            responseBuffer.append( (char) c );
        }
        is.close();        

        //Get the cookie 
    /*String cookie = http.getHeaderField("set-cookie"); 
    if(cookie!=null && cookie.length()>0){ 
        sCookie = cookie;               
    }*/

    // Multi cookie handling:                   
    String responseHeaderName = null;
    for (int i=1; (responseHeaderName = http.getHeaderFieldKey(i))!=null; i++) { 
        if (responseHeaderName.toLowerCase().equals("set-cookie")) {
            cookie.update(http.getHeaderField(i));
            //debugLog("postSSL cookie["+String.valueOf(i)+"] received :"+ http.getHeaderField(i));
        } 
    }

        http.disconnect();
        //debugLog("postSSL cookie received :"+cookie.toString());
        Result res = new Result(responseBuffer.toString(),cookie);
        return res;
    } catch (Exception e){
        e.printStackTrace();
        return new Result("postSSL Exception:"+e.getMessage());
    }
}
