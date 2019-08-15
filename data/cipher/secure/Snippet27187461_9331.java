//command for retrieving exponent
resp = channel.transmit(new CommandAPDU(cmdExp)); 
BigInteger modulus = new BigInteger(resp.getData());
byte[] input = { (byte) 0x92, (byte) 0x84, (byte) 0x3B,
        (byte) 0xD3, (byte) 0x5D, (byte) 0x8A, (byte) 0x6B,
        (byte) 0x56, (byte) 0xDA, (byte) 0xEA, (byte) 0xE0,
        (byte) 0x2F, (byte) 0x6D, (byte) 0xAA, (byte) 0x62,
        (byte) 0x4B, (byte) 0x38, (byte) 0xCE, (byte) 0xD4,
        (byte) 0x70, (byte) 0xA2, (byte) 0x16, (byte) 0x35,
        (byte) 0xCC, (byte) 0xEE, (byte) 0xB8, (byte) 0x31,
        (byte) 0x13, (byte) 0x37, (byte) 0x40, (byte) 0xBE,
        (byte) 0xA1, (byte) 0xCD, (byte) 0x84, (byte) 0xD9,
        (byte) 0xF3, (byte) 0xE6, (byte) 0xCE, (byte) 0x26,
        (byte) 0x0A, (byte) 0xC1, (byte) 0x40, (byte) 0xED,
        (byte) 0x20, (byte) 0x8F, (byte) 0x3D, (byte) 0x9F,
        (byte) 0x0D, (byte) 0xE7, (byte) 0x19, (byte) 0xC8,
        (byte) 0x87, (byte) 0x96, (byte) 0x29, (byte) 0xF2,
        (byte) 0x63, (byte) 0x34, (byte) 0x6D, (byte) 0x10,
        (byte) 0xB9, (byte) 0xFB, (byte) 0xB4, (byte) 0x75,
        (byte) 0xE9 };

RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
        .generatePublic(new RSAPublicKeySpec(modulus, exponent));

Cipher cipher = null;

cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.DECRYPT_MODE, pubKey);

byte[] cipherText = cipher.doFinal(input);
