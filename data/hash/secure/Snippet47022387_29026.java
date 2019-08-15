public class CustomObjectRule {

    object = DatatypeConverter.printHexBinary(MessageDigest.getInstance(SECURITY_ALGORITHM)
             .digest(message.getBytes(ENCODING)));

}
