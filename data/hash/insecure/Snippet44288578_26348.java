public static String getHashOfImage(URL imageUrl) throws IOException, NoSuchAlgorithmException
{
    BufferedImage buffImg = ImageIO.read(imageUrl);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(buffImg, "png", outputStream);
    byte[] data = outputStream.toByteArray();

    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(data);
    byte[] digest = md5.digest();

    return new BigInteger(1, digest).toString(16);
}
