SecretKey key = new SecretKeySpec(new byte[8], "DES");

Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, key);

System.out.println(DatatypeConverter.printHexBinary(cipher
    .doFinal(new byte[8])));
