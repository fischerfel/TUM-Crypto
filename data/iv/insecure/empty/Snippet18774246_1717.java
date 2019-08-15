public class MessageEncrypt {

        public String encryptString(String message, String seckey) throws Exception{
            byte[] encData = encrypt(message, seckey);

            return Base64.encodeToString(encData, Base64.DEFAULT);
        }

        public String decryptString(String message, String seckey) throws Exception{
            byte[] encData= Base64.decode(message, Base64.DEFAULT);

            return decrypt(encData, seckey);
        }

        private byte[] encrypt(String message, String seckey) throws Exception {
            final MessageDigest md = MessageDigest.getInstance("md5");
            final byte[] digestOfPassword = md.digest(seckey.getBytes("utf-8"));
            final byte[] keyBytes = acopyof(digestOfPassword, 24);
            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }

            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            final byte[] plainTextBytes = message.getBytes("utf-8");
            final byte[] cipherText = cipher.doFinal(plainTextBytes);

            return cipherText;
        }

        private String decrypt(byte[] message, String seckey) throws Exception {
            final MessageDigest md = MessageDigest.getInstance("md5");
            final byte[] digestOfPassword = md.digest(seckey.getBytes("utf-8"));
            final byte[] keyBytes = acopyof(digestOfPassword, 24);
            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }

            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            decipher.init(Cipher.DECRYPT_MODE, key, iv);

            final byte[] plainText = decipher.doFinal(message);

            return new String(plainText, "UTF-8");
        }

        public byte[] acopyof(byte[] orig, int newlength){
            byte[] copya = new byte[newlength];
            for(int i=0;i< orig.length;i++){
                copya[i]=orig[i];
            }
            for(int i=orig.length;i<newlength;i++){
                copya[i]=0x0;
            }
            return copya;
        }
    }
