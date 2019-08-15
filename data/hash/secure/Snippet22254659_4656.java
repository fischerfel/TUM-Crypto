String createToken() {
    String token = "";

    MessageDigest lclMD = null;
    try {
        lclMD = MessageDigest.getInstance("SHA-256"); }
    catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    lclMD.update((EnvConstants.code + EnvConstants.api_key + Long.toString(getEpochMinutes())).getBytes());
    byte[] lclResult = lclMD.digest();

    sig = new String(Hex.encodeHex(lclResult));
    return token; 
}

long getEpochMinutes () {
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT")); 
    long now = System.currentTimeMillis();

    cal.setTimeInMillis(now);
    return (cal.getTimeInMillis() / 60000L); 
}
