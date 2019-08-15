public static void main(String[] args) {
    try {
        byte[] keyBytes = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        byte[] ivBytes =  {0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00, 0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77};

        SecretKeySpec sks = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, sks, iv);

        // read file to byte[]
        InputStream is = new FileInputStream("test.enc");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while ((b = is.read()) != -1) {
            baos.write(b);
        }
        byte[] fileBytes = baos.toByteArray();

        byte[] decrypted = cipher.doFinal(fileBytes);
        System.out.println(new String(decrypted));

    } catch (Exception e) {
        e.printStackTrace();
    }
}
