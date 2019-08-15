public static String getChecksum(Serializable object) throws IOException, NoSuchAlgorithmException {
    ByteArrayOutputStream baos = null;
    ObjectOutputStream oos = null;
    try {
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(baos.toByteArray());
        return DatatypeConverter.printHexBinary(thedigest);
    } finally {
        oos.close();
        baos.close();
    }
}
