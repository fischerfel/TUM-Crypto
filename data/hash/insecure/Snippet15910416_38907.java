private static void buildPlaylist() {

    String mood = "Happy";
    System.out.println("\nMood is " + mood + "\n\n");

    String title = mood + " " + new Date().getTime();
    String description = "For when you are " + mood + ". Created by MoodicPlayer.";

    MessageDigest md = MessageDigest.getInstance("MD5");

    String apiSig = "api_key" + key + "description" + description + "methodplaylist.createsk" + sessionKey + "title" + title + secret;
    md.update(apiSig.getBytes("UTF-8"));
    byte byteData[] = md.digest();
    //convert the byte to hex format
    StringBuffer sb = new StringBuffer(byteData.length*2);
    for (int i = 0; i < byteData.length; i++) {
        sb.append(String.format("%02X", byteData[i]));    
    }
    String hashedSig = sb.toString();

    // FOR DEBUGGING
    System.out.println("api_key = " + key);
    System.out.println("api_sig = " + hashedSig);
    System.out.println("session key = " + sessionKey);
    // FOR DEBUGGING

    String urlParameters = "method=playlist.create&api_key="+ key + "&api_sig=" + hashedSig + "&description=" + URLEncoder.encode(description, "UTF-8") + "&sk=" + sessionKey + "&title=" + URLEncoder.encode(title, "UTF-8");
    String request = "http://ws.audioscrobbler.com/2.0/";

    URL url = new URL(request);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();     
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.setInstanceFollowRedirects(false); 
    connection.setRequestMethod("POST"); 
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
    connection.setRequestProperty("charset", "utf-8");
    connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
    connection.setUseCaches(false);

    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
    wr.writeBytes(urlParameters);
    wr.flush();
    wr.close();

    ...
}
