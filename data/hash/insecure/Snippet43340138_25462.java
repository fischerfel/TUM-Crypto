public static String MD5(File file) throws NoSuchAlgorithmException, IOException{
    MessageDigest md = MessageDigest.getInstance("MD5");
    BufferedInputStream in = new BufferedInputStream(new FileInputStream(file.getPath()));

    int theByte = 0;
    while ((theByte = in.read()) != -1) {
      md.update((byte) theByte);
    }
    in.close();

    byte[] theDigest = md.digest();

    System.out.println(DatatypeConverter.printHexBinary(theDigest));
    return DatatypeConverter.printHexBinary(theDigest);
}
