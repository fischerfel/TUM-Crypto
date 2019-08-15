public String getPageContent(String url) throws Exception {
    URL obj = new URL(url);
    conn = (HttpsURLConnection) obj.openConnection();
    conn.setHostnameVerifier(hostnameVerifier);
    conn.setRequestMethod("GET");

    conn.setUseCaches(false);

    conn.setRequestProperty("User-Agent", USER_AGENT);
    conn.setRequestProperty("Accept",
        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
    if (cookies != null) {
        for (String cookie : this.cookies) {
            conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
        }
    }

    james.setResponseCode_GetPageContent(conn.getResponseCode());
    int x = conn.getResponseCode();
    Log.w("App", "ResponseCode: " + x);
    BufferedReader in = 
            new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine + "\r\n");
    }
    in.close();

    setCookies(conn.getHeaderFields().get("Set-Cookie"));
    return response.toString();

  } // end of getPageContent
