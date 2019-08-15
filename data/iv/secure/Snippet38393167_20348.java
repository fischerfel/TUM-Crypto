Object spi = ReflectionTestUtils.getField(cipher, "spi");
ReflectionTestUtils.setField(spi, "keyLength", 128);
cipher.init(Cipher.DECRYPT_MODE, keyFromPassword, new PBEParameterSpec(
        salt, iterations, new IvParameterSpec(iv)));
