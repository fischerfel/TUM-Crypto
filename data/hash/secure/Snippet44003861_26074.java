    import java.security.MessageDigest;

    String password = "Høstname1";
    String salt = "6";

    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    byte[] hash = new byte[40];
    messageDigest.update(salt.getBytes("utf-8"), 0, salt.length());
    messageDigest.update(password.getBytes("utf-8"), 0, password.length());
    hash = messageDigest.digest();
