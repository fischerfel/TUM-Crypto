public String byteToHexString(byte[] input) {
    String output = "";
    for (int i=0; i<input.length; ++i) {
        output += String.format("%02X", input[i]);
    }
    return output;
}

public String hash(String value) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return byteToHexString(md.digest(value.getBytes("UTF-8")));
    } catch (NoSuchAlgorithmException e) {
        return null;
    } catch (UnsupportedEncodingException e) {
        return null;
    }
}
