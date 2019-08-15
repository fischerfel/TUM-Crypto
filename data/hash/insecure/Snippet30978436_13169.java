private static int[] Hash(byte[] plainBytes, byte[] saltBytes)
        throws NoSuchAlgorithmException, UnsupportedEncodingException {
    byte[] sourceBytes = new byte[plainBytes.length + saltBytes.length];
    for (int i = 0, il = plainBytes.length; i < il; i++)
        sourceBytes[i] = plainBytes[i];
    for (int i = 0, il = saltBytes.length, pil = plainBytes.length; i < il; i++)
        sourceBytes[pil + i] = saltBytes[i];
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
    digest.reset();
    byte[] resultBytes = digest.digest(sourceBytes);
    int[] result = new int[resultBytes.length];
    for (int i = 0, il = resultBytes.length; i < il; i++)
        result[i] = ((int) resultBytes[i]) & 255;
    return result;
}
