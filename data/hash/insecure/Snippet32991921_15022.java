public class HttpClient {

    protected OkHttpClient mClient = new OkHttpClient();

    public String get(final URL url, final String[] responseHash)
        throws IOException {
        HttpURLConnection connection = new OkUrlFactory(mClient).open(url);
        InputStream inputStream = null;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert messageDigest != null;
        try {
            // Read the response.
            inputStream = connection.getInputStream();
            byte[] response = readFully(inputStream);
            final byte[] digest = messageDigest.digest(response);
            responseHash[0] = Base64.encodeToString(digest, Base64.DEFAULT);
            return new String(response, Util.UTF_8);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }

}
