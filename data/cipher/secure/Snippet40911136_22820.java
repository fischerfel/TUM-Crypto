Cipher pbeCipher = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
AlgorithmParameters params = pbeCipher.getParameters();
