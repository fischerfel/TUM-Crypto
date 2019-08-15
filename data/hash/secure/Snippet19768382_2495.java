/**
 * @param args
 */
public static void main(String[] args) {
    // TODO Auto-generated method stub
    String message = "test";

    MessageDigest messageDigest = null;
    try {
        messageDigest = MessageDigest.getInstance("SHA-512");
    } catch (NoSuchAlgorithmException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }

    try {
        messageDigest.update(message.getBytes("UTF-16BE"));
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    byte[] digest = messageDigest.digest();

    StringBuffer digestInHex = new StringBuffer();

    for (int i = 0, l = digest.length; i < l; i++) {
        // Preserve the bit representation when casting to integer.
        int intRep = digest[i] & 0xFF;
        // Add leading zero if value is less than 0x10.
        if (intRep < 0x10)  digestInHex.append('\u0030');
        // Convert value to hex.
        digestInHex.append(Integer.toHexString(intRep));
    }

    System.out.println(digestInHex.toString());

}
