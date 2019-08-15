String hash;
try {
    hash = new BASE64Encoder().encode(MessageDigest.getInstance("SHA-1").digest((keys.get("Sec-WebSocket-Key") + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes()));
} catch (NoSuchAlgorithmException ex) {
    ex.printStackTrace();
    return false;
}
