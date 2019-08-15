public static String Key(String thekey) throws NoSuchAlgorithmException {
    String base64;

    thekey = thekey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    byte[] digest = md.digest(thekey.getBytes()); // Missing charset
    base64 = Base64.encodeBase64URLSafeString(digest);
    base64=base64.replace("_", "/");
    base64=base64.replace("-", "+");
    return new String(base64);
}
 Send("HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: " + Key(key) +"=\r\n\r\n");
