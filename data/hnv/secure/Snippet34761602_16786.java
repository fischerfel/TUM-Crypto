    URL url;
    // get URL content
    url = new URL(apiURL);
    conn = (HttpsURLConnection) url.openConnection();

    System.setProperty("http.keepAlive", "false");
    conn.setReadTimeout(5000);
    conn.setConnectTimeout(5000);
    conn.setUseCaches(false);
    conn.setDoInput(true);
    conn.setDoOutput(false);
    conn.setHostnameVerifier(AsyncConnect.DO_NOT_VERIFY);
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Content-length", "0");
    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
    conn.setRequestProperty(MainActivity.API_TOKEN, MainActivity.ENCRYPTED_TOKEN);

    in=conn.getInputStream(); // slowest part so far, several seconds spent there

    // open the stream and put it into BufferedReader
    BufferedReader br = new BufferedReader(new InputStreamReader(in));

    while ((line=br.readLine())!= null) {
        builder.append(line);
    }

    result=builder.toString();
    //System.out.print(result);
    br.close();



} catch (MalformedURLException e) {
    result=null;
} catch (IOException e) {
    result=null;
} catch (Exception e) {
    result=null;
}
finally {
    try {
        in.close();
    }catch(Exception e){}
    try {
        conn.disconnect();
    }catch(Exception e){}

    return result;
}
