public static class LiberalHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}

public static String post(String requestUrl, Map<String, String> params,
    String username, String password) throws Exception {
    String data = "";
    int paramCount = 1;
    for (Entry<String, String> param : params.entrySet()) {
        if (paramCount == 1) {
            data = URLEncoder.encode(param.getKey(), "UTF-8") + "="
                + URLEncoder.encode(param.getValue(), "UTF-8");
        } else {
            data += "&" + URLEncoder.encode(param.getKey(), "UTF-8") + "="
                + URLEncoder.encode(param.getValue(), "UTF-8");
        }
        paramCount++;
    }
    URL url = new URL(requestUrl);
    HttpsURLConnection conn = (HttpsURLConnection) (url).openConnection();
    conn.setDoOutput(true);
    conn.setRequestMethod("POST");
    conn.setHostnameVerifier(new LiberalHostnameVerifier());
    BASE64Encoder enc = new BASE64Encoder();
    String userAuth = username + ":" + password;
    String encodedAuthorization = enc.encode(userAuth.getBytes());
    conn.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
    wr.write(data);
    wr.flush();
    BufferedReader rd = new BufferedReader(new InputStreamReader(
        conn.getInputStream()));
    String line;
    String response = "";
    while ((line = rd.readLine()) != null) {
        response += line;
    }
    wr.close();
    rd.close();
    return response;
}

public static KeyPair generateKey(String filename) throws Exception {
    JSch jsch = new JSch();
    try {
        KeyPair kpair = KeyPair.genKeyPair(jsch, KeyPair.RSA);
        kpair.setPassphrase("");
        kpair.writePrivateKey(filename + ".pem");
        kpair.writePublicKey(filename + ".pub", "Auto-generated.");
        System.out.println("Finger print: " + kpair.getFingerPrint());
        // kpair.dispose();
        return kpair;
    } catch (Exception e) {
        System.out.println(e);
    }
    return null;
}

public static String getFileContents(File file) throws Exception {
    byte[] buffer = new byte[(int) file.length()];
    FileInputStream f = new FileInputStream(file);
    f.read(buffer);
    return new String(buffer);
}

public static String createKey(String title) throws Exception {
    generateKey(title);
    final String key = getFileContents(new File(
            "/Users/franklovecchio/Desktop/development/" + title
                + ".pub"));
    System.out.println("key: " + key);
    Map<String, String> params = new HashMap<String, String>() {

        {
            put("title", title);
            put("key", key);
        }
    };
    return post("https://api.github.com/user/keys", params, "username",
        "password");
}
