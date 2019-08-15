MessageDigest mDigest = null;
try {
    mDigest = MessageDigest.getInstance("SHA1");
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
}

String input = value1 + value1 + server_key;
byte[] result = mDigest.digest(input.getBytes());
String secret = Base64.encodeToString(result, Base64.NO_WRAP);
...
//comparison logic goes here
