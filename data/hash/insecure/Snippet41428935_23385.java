try {
    String password = "qwkld67U";
    MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
    sha1.update(password.getBytes("UTF-16LE"));
    String result = "2-" + Base64.encodeToString(sha1.digest(), Base64.DEFAULT);
    Log.i("SHA1", result);
} catch (Exception e) {
    throw new RuntimeException(e);
}
