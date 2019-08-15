        byte[] encoded = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03,
                (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03};
        SecretKeySpec secretKeySpec = new SecretKeySpec(encoded, "AES");

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");//AES/CBC/PKCS5Padding
        System.out.println("swapnil:"+c.getAlgorithm()+" BlockSize:"+c.getBlockSize());
            c.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] input = "Hello".getBytes();
            byte[] output = c.doFinal(input);
            System.out.println("Swapnil: " + new String(output));
