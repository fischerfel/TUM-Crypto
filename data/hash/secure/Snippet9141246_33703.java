 private String digest(String algorithm,String password) throws NoSuchAlgorithmException {
        String r = null;
        byte [] b = null;
        MessageDigest md = MessageDigest.getInstance(algorithm);
        BASE64Encoder encoder;

        md.update(password.getBytes());
        b = md.digest();

        encoder = new BASE64Encoder();

        System.out.println(encoder.encode(b));

        r = encoder.encode(b);

        return r;
    }
