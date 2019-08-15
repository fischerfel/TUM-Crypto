final byte[] iv = new byte[16];
Arrays.fill(iv, (byte) 0x00);
IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
.. // the rest of preparations
ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
