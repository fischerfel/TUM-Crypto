    MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
    String password = "secret";
    messageDigest.update(password.getBytes());
    byte[] bytes = messageDigest.digest();
    StringBuilder stringBuilder = new StringBuilder();
    for (byte aByte : bytes) {
        stringBuilder.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
    }
    System.out.println(stringBuilder.toString());
