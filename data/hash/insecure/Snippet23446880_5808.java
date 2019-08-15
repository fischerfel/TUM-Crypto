String message = "This is a test Message";
MessageDigest sha1 = MessageDigest.getInstance("SHA1")
System.out.println(calculateHash(sha1, message));

public static String calculateHash(MessageDigest algorithm,
        String message) throws Exception{
    algorithm.update(message.getBytes());
    byte[] hash = algorithm.digest();
    return byteArray2Hex(hash);
}
