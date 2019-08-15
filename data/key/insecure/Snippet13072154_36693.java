    Cipher f = Cipher.getInstance("AES/CBC/PKCS5Padding");
     byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
                0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    f.init(Cipher.ENCRYPT_MODE, key);
    byte[] iv = f.getIV();
    System.out.println(Arrays.toString(f.doFinal("hello".getBytes())));
    System.out.println(Arrays.toString(f.getIV()));
    System.out.println(Arrays.toString(f.doFinal("hello".getBytes())));
    System.out.println(Arrays.toString(f.getIV()));
    System.out.println( Arrays.equals(f.getIV(), iv)); // true
