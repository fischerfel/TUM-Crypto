public static void main(String[] args) throws Exception {
        // byte [] key = "Bar12345Bar12345".getBytes("UTF-8"); // 128 bit key
        byte[] key = Hex.decode("8ec8f262e96e3d80ef52b530a5bc7b7baaf6e4357a363119b0a636b2034e298e");
        byte[] iv = Hex.decode("a5e8d2e9c1721ae0e84ad660c472c1f3");
        System.out.print(Arrays.toString(key));
        System.out.println(key.length);
        System.out.print(Arrays.toString(iv));
        System.out.println(iv.length);

        System.out.println(decrypt(key, iv, encrypt(key, iv, "Hello World")));

    }

    public static String encrypt(byte[] key, byte[] initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");/// CBC/PKCS5PADDING
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: " + Base64.encodeBase64String(encrypted));

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(byte[] key, byte[] initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
