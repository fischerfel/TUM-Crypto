    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
    InputStream is = new FileInputStream(aFile);
    int res;

    while ((res = inputStream.read()) != -1) {
        digester.update((byte) res);
    }

    byte[] digest = messageDigest.digest();
