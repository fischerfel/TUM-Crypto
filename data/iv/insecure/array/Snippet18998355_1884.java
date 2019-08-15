byte[] iv = new byte[] { '3', 'd', '0', 'c', 'd', '7', 'A', '9', '7', 'e', '2', '0', 'b', 'x', 'g', 'y' };
IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
