String hashedId = "";

String deviceId = urlEncode(Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));

try {
    MessageDigest digest = MessageDigest.getInstance("SHA-1");

    byte bytes[] = digest.digest(deviceId.getBytes());

    BigInteger b = new BigInteger(1, bytes);
    hashedId = String.format("%0" + (bytes.length << 1) + "x", b);

} catch (NoSuchAlgorithmException e) {
    //ignored
}

return hashedId;
