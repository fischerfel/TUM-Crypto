public class DownloadHandler{

public String getMd5(String url){
    HttpClient client = new DefaultHttpClient();

    HttpGet request = new HttpGet(url);

    HttpResponse response = null;

    try {
        response = client.execute(request);
    } catch (ClientProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    InputStream in = null;
    try {
        in = response.getEntity().getContent();
    } catch (IllegalStateException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

    StringBuilder str = new StringBuilder();
    String line = null;
    try {
        while ((line = reader.readLine()) != null) {
            str.append(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        in.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

    String HTML = str.toString();

    try {
        String md5 = stringToMd5(html);
        return md5;
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return null;
    }

}

public String stringToMd5(String s) throws NoSuchAlgorithmException {
    MessageDigest md5 = MessageDigest.getInstance("MD5");stringa
    md5.update(s.getBytes(), 0, s.length());
    String md5String = new BigInteger(1, md5.digest()).toString(16);
    return md5String;
}
