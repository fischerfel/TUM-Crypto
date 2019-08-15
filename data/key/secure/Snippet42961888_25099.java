private static final String REQUEST_API_SIGN = "musing";
private static final String REQUEST_API_KEY = "mike";
private static final String REQUEST_USER_AGENT = "User-Agent";
private static final String REQUEST_POST = "POST";

private static final String PUBLIC_URL = "https://api.kraken.com/0/public/";
private static final String PRIVATE_URL = "https://api.kraken.com/0/private/Balance";

main() {
    HttpsURLConnection connection = null;

    connection = (HttpsURLConnection) url.openConnection();
    connection.setRequestMethod(REQUEST_POST);
    if (!isPublic) {
        sign = Base64.getEncoder().encodeToString(createSignature(nonce, "/0/private/Balance", postData)); //path is "/version/private/method";

        connection.addRequestProperty("API-Key", URLEncoder.encode(REQUEST_API_KEY, "UTF-8"));
        connection.addRequestProperty("API-Sign", URLEncoder.encode(sign, "UTF-8"));

        connection.setRequestProperty("Content-Length", String.valueOf(postData.length()));
        connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8;application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        // connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
        connection.setRequestProperty("Accept", "application/x-www-form-urlencoded; charset=utf-8");

        connection.setUseCaches(false);
        connection.addRequestProperty(REQUEST_USER_AGENT, "Mozilla/4.0");

        connection.setDoOutput(true);
        connection.setDoInput(true);

        try {
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(postData);
            out.flush();
        }
        finally {
        }
    }
    try {
        BufferedReader in =new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }

        return Response.status(200).entity(response.toString()).build();
    }
}

public static byte[] concatArrays(byte[] a, byte[] b) {
    if (a == null || b == null) {
        throw new IllegalArgumentException(ERROR_NULL_ARRAYS);
    }

    byte[] concat = new byte[a.length + b.length];
    for (int i = 0; i < concat.length; i++) {
        concat[i] = i < a.length ? a[i] : b[i - a.length];
    }

    return concat;
}
private static byte[] createSignature(long nonce, String path, String postdata) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
    return hmac3(concatArrays(stringToBytes(path), sha256(nonce + post Data)), Base64.getDecoder().decode(REQUEST_API_SIGN));
}

public static byte[] stringToBytes(String input) {
    if (input == null) {
        throw new IllegalArgumentException(ERROR_NULL_INPUT);
    }
    return input.getBytes(Charset.forName(UTF8));
}

public static String sha256Hex(String text) throws NoSuchAlgorithmException, IOException {
    return org.apache.commons.codec.digest.DigestUtils.sha256Hex(text);
}

public static byte[] sha256(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");

    md.update(text.getBytes("UTF-8"));
    // md.update(text.getBytes());
    byte[] digest = md.digest();

    return digest;
}

public static byte[] hmacSha512(byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
    Mac mac = Mac.getInstance(HMAC_SHA512);
    mac.init(new SecretKeySpec(key, HMAC_SHA512));
    return mac.doFinal(message);
}

public static byte[] hmac3(byte[] text, byte[] secret) {

    Mac mac = null;
    SecretKeySpec key = null;

    // Create a new secret key
    try {
        key = new SecretKeySpec(secret, "HmacSHA512");
    }
    finally {
    }
    // Create a new mac
    try {
        mac = Mac.getInstance("HmacSHA512");
    } catch(NoSuchAlgorithmException nsae) {
        System.err.println("No such algorithm exception: " + nsae.toString());
        return null;
    }

    // Init mac with key.
    try {
        mac.init(key);
    } catch(InvalidKeyException ike) {
        System.err.println("Invalid key exception: " + ike.toString());
        return null;
    }

    // Encode the text with the secret
    try {
        //String s;

        byte[] s = mac.doFinal(text);
        return s;
    }
    finally {
    }
}
