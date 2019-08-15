private static byte[] hash(String paramString)
{
    MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-256");
    localMessageDigest.update(paramString.getBytes("UTF-8"));
    byte[] paramByte = localMessageDigest.digest();
    String base64Binary = DatatypeConverter.printBase64Binary(paramByte);
    System.out.println("Hash: " + base64Binary);
    return base64Binary.getBytes();
}
