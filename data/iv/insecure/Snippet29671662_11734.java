public static void main(String[] args) throws UnsupportedEncodingException {
            String KEY = "AB1CD237690AF13B6721AD237A";
            String IV = "por874hyufijdue7w63ysxwet4320o90";
            SecretKeySpec key = generateKey(KEY);
            String message = "password";

            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] ciphedText = cipher.doFinal(message.getBytes());
            String encoded = Base64.encodeBase64String(ciphedText);

            System.out.println("ENCRYPTED text= " + encoded);
}

public  static SecretKeySpec generateKey(final String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();       
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
