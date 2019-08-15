BufferedImage buffImg = ImageIO.read(imageEntry);
ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
ImageIO.write(buffImg, Utils.getExtensionNoDot(imageEntry.getName()), outputStream);
MessageDigest md = MessageDigest.getInstance("MD5");
md.update(outputStream.toByteArray());
BigInteger bigInt = new BigInteger(1,md.digest());
String md5 = bigInt.toString(16);
