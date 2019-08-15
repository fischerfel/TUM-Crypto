import java.security.MessageDigest;
String.metaClass.toSHA256 = {
    def messageDigest = MessageDigest.getInstance("SHA-256")
    messageDigest.update(delegate.getBytes("UTF-8"))
    new BigInteger(1, messageDigest.digest()).toString(16).padLeft(40, '0')
}
