private static byte[] combineByteArrays(byte[] a, byte[] b) { // combines two byte[] arrays
    byte[] result = new byte[a.length + b.length];
    System.arraycopy(a, 0, result, 0, a.length);
    for (int i = a.length; i < result.length; i++) {
        result[i] = b[i-a.length];            
    }
    return result;
}

private static String byteArrayToHex(byte[] array) {  // returns hex representation of byte[] array
    String resultStr = "";
    for (int i=0; i < array.length; i++) {
        resultStr += Integer.toString( ( array[i] & 0xff ) + 0x100, 16).substring( 1 );
    }
    return resultStr;
}

private static String computeResponse(String username, String password, String realm, String nonce, String qop, String cnonce, String digest_uri, String nc) throws NoSuchAlgorithmException {  // computes response for challenge query of XMPP server
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    final byte[] part1 = md5.digest(combineByteArrays(md5.digest((username + ":" + realm + ":" + password).getBytes()), (":" + nonce + ":" + cnonce).getBytes()));
    final byte[] part2 = md5.digest(combineByteArrays("AUTHENTICATE:".getBytes(), digest_uri.getBytes()));
    final byte[] temp = combineByteArrays(byteArrayToHex(part1).getBytes(), (":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":").getBytes());
    final byte[] part3  = md5.digest(combineByteArrays(temp, byteArrayToHex(part2).getBytes()));
    return byteArrayToHex(part3);
}

public static void main(String[] args) throws NoSuchAlgorithmException {

    /* example data from http://deusty.blogspot.com/2007/09/example-please.html */
    String username = "test";
    String password = "secret";
    String realm = "osXstream.local";
    String nonce = "392616736";
    String qop = "auth";
    String cnonce = "05E0A6E7-0B7B-4430-9549-0FE1C244ABAB";
    String digest_uri = "xmpp/osXstream.local";
    String nc = "00000001";        

    /* prints out "37991b870e0f6cc757ec74c47877472b" */
    System.out.println(computeResponse(username, password, realm, nonce, qop, cnonce, digest_uri, nc));  
}
