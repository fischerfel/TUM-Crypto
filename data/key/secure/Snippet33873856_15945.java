     public byte[] decryp_decompress(byte[] raw, byte[] encrypted)
                  throws Exception {
           SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
           Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
           cipher.init(Cipher.DECRYPT_MODE, skeySpec);
           byte[] decrypted = cipher.doFinal(encrypted);
           GZip gzip = new GZip();
           byte[] decompressData = gzip.decompresses(decrypted);

           return decompressData;
    }
