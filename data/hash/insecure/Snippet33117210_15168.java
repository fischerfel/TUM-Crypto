public class HashingInterceptor implements Interceptor {
    public static final String HASH_HEADER = "content-hash";

    final protected static char[] hexArray = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String expectedHash = request.header(HASH_HEADER);
        if (expectedHash != null) {
            com.squareup.okhttp.Response response = chain.proceed(request);
            byte[] bytes = response.body().bytes();
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                final byte[] digest = messageDigest.digest(bytes);
                String responseHash = bytesToHex(digest);
                if (responseHash.equals(expectedHash)) {
                    return response.newBuilder()
                            .code(204).build();
                } else {
                    return response.newBuilder()
                        .body(ResponseBody.create(
                            response.body().contentType(), bytes))
                        .addHeader(HASH_HEADER, responseHash)
                        .build();
                }
            } catch (NoSuchAlgorithmException e) {
                throw new IOException(e);
            }

        } else {
            // Header was not set, just proceed as usual
            return chain.proceed(request);
        }
    }
}
