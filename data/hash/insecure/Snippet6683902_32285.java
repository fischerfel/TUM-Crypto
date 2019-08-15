InputStream is = // input stream of the uploaded file
byte[] buffer = new byte[1024];
byte[] digest;
try {
    MessageDigest md = MessageDigest.getInstance("MD5");

    for (int count = is.read(buffer); count != -1; count = is.read(buffer)) {
        md.update(buffer, 0, count);
    }

    digest = md.digest();
    // store digest as needed, possibly Base64 encode first
}
catch (NoSuchAlgorithmException e) {
    // handle
}
