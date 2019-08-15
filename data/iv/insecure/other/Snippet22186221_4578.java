byte[] iv = DatatypeConverter.parseHexBinary("0000000000000000");
IvParameterSpec ips = new IvParameterSpec(iv);
desCipher.init(Cipher.ENCRYPT_MODE, key, iv);
