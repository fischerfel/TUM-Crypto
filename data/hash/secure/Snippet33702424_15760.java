private String calculateHash(BufferedImage img) throws NoSuchAlgorithmException {
    final int width = img.getWidth();
    final int height = img.getHeight();
    final ByteBuffer byteBuffer = ByteBuffer.allocate(4 * width * height);
    for (int y = 0; y < height; y++)
    {
        for (int x = 0; x < width; x++)
        {
            // grab color information
            int argb = img.getRGB(x, y);

            // a,r,g,b ordered bytes per this pixel. the values are always 0-255 so the byte cast is safe
            byteBuffer.put((byte) ((argb >> 24) & 0xff));
            byteBuffer.put((byte) ((argb >> 16) & 0xff));
            byteBuffer.put((byte) ((argb >> 8) & 0xff));
            byteBuffer.put((byte) (argb & 0xff));
        }
    }


    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.reset();

    byte[] hashBytes = digest.digest(byteBuffer.array());
    return Base64Utils.encodeToString(hashBytes);
}
