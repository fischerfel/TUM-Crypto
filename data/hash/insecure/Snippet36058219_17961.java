private InputStream connect(String urlStr, String username, String password) throws Exception {

    URL url = new URL(urlStr);
    HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
    connection.setDoInput(true);
    connection.setRequestMethod("GET");

    try {

        return connection.getInputStream();

    } catch(Exception e) {

        if (connection.getResponseCode() == 401) {

            String header = connection.getHeaderField("WWW-Authenticate");

            String uri = new URL(urlStr).getFile();

            String nonce = Tools.match(header, "nonce=\"([A-F0-9]+)\"");
            String realm = match(header, "realm=\"(.*?)\"");
            String qop = match(header, "qop=\"(.*?)\"");
            String algorithm = match(header, "algorithm=(.*?),");
            String cnonce = generateCNonce();
            String ha1 = username + ":" + realm + ":" + password;
            String ha1String = md5digestHex(ha1);
            String ha2 = "GET" + ":" + uri;
            String ha2String = md5digestHex(ha2);
            int nc = 1;
            String response = ha1String + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + ha2String;
            String responseString = md5digestHex(response);


            String authorization =
                "Digest username=\"" + username + "\"" + 
                ", realm=\"" + realm + "\"" +
                ", nonce=\"" + nonce + "\"" +
                ", uri=\"" + uri + "\"" +
                ", qop=\"" + qop + "\"" +
                ", nc=\"" + nc + "\"" +
                ", cnonce=\"" + cnonce + "\"" +
                ", response=\"" + responseString + "\"" +
                ", algorithm=\"" + algorithm + "\"";


            HttpURLConnection digestAuthConnection = prepareConnection(urlStr);
            digestAuthConnection.setRequestMethod("GET");
            digestAuthConnection.setRequestProperty("Authorization", authorization);

            return processResponse(digestAuthConnection);

        } else throw e;
    }
}

public static String match(String s, String patternString, boolean strict) {

    if (!isEmpty(s) && !isEmpty(patternString)) {
        Pattern pattern = Pattern.compile(patternString);
        if (pattern != null) {
            Matcher matcher = pattern.matcher(s);
            if (matcher != null && matcher.find() && (matcher.groupCount() == 1 || !strict)) {
                return matcher.group(1);
            }
        }
    }
    return null;
}

public static String match(String s, String patternString) {
    return match(s, patternString, true);
}

public static byte[] md5Digist(String s) {
    try {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        md5.update(s.getBytes());
        return md5.digest();
    } catch (NoSuchAlgorithmException e) {
        return null;
    }
}

public static String digest2HexString(byte[] digest) {

   String digestString="";
   int low, hi;

   for (int i = 0; i < digest.length; i++) {
      low = (digest[i] & 0x0f ) ;
      hi  = ((digest[i] & 0xf0) >> 4);
      digestString += Integer.toHexString(hi);
      digestString += Integer.toHexString(low);
   }
   return digestString;
}

public static String md5digestHex(String s) {
    return digest2HexString(md5Digist(s));
}

public static String generateCNonce() {
    String s = "";
    for (int i = 0; i < 8; i++) {
        s += Integer.toHexString(new Random().nextInt(16));
    }

    return s;

}
