    public byte[] encryptedAes(char[] input) throws Exception {
//declare as static so initVector can be reused when decrypting
         IvParamterSpec initVector = new IvParameterSpec(aesSecretKey);       
            aesCipher.init(Cipher.ENCRYPT_MODE, aesSecretKey, initVector);
            CharBuffer cBuf = CharBuffer.wrap(input);
            byte[] normalised = Charset.forName("UTF-8").encode(cBuf).array();
            byte[] ciphertext = aesCipher.doFinal(normalised);
            return ciphertext;
        }
