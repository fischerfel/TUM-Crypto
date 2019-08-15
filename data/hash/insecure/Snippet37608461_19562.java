try {
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    messageDigest.update("https://services.gradle.org/distributions/gradle-2.10-all.zip".getBytes());
    System.out.println(new BigInteger(1, messageDigest.digest()).toString(36));
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
}
