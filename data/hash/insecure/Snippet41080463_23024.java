private static String encodePassword(String password) {
    MessageDigest algorithm;
    try {
        algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(password.getBytes());
        byte[] encrypted =  algorithm.digest();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStream encoder = MimeUtility.encode(out, "base64");

        encoder.write(encrypted);
        encoder.flush();
        return new String(out.toByteArray());
    } catch (NoSuchAlgorithmException e) {
        return "Bad Encryption";
    } catch (MessagingException e) {
        return "Bad Encryption";
    } catch (IOException e) {
        return "Bad Encryption";
    }
