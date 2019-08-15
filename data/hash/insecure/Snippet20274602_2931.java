private static String getMd5Hash(String input) throws NoSuchAlgorithmException {
    MessageDigest m = MessageDigest.getInstance("MD5");

    byte[] data = m.digest(EncodingUtils.getBytes(input, "UTF8"));

    StringBuilder sBuilder = new StringBuilder();

    for (int i = 0; i < data.length; i++) {

        for (byte b : data) {
            if(b == 0x00){
                sBuilder.append("00");
            } else if ((b & 0x0F) == b) {
                sBuilder.append("0");
                break;
            } else {
                break;
            }
        }

        BigInteger bigInt = new BigInteger(1, data);
        sBuilder.append(bigInt.toString(16));
    }

    // Return the hexadecimal string.
    return sBuilder.toString().substring(0, 32);
}
