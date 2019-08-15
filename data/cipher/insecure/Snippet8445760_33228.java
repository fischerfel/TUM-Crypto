public class ED {


    private  String Key;

        public ED() {
            Key = "abc12";   // Assigning default key.
        }

        public ED(String key) {
            // TODO Auto-generated constructor stub
            Key = key;

        }



        public String encrypt(String toEncrypt) throws Exception {
            byte[] rawKey = getRawKey(Key.getBytes("UTF-8"));
            byte[] result = encrypt(rawKey, toEncrypt.getBytes("UTF-8"));
            return toHex(result);
        }



        public  byte[] encrypt(byte[] key, byte[] toEncodeString) throws Exception {

            SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);

            byte[] encrypted = cipher.doFinal(toEncodeString);

            return encrypted;
        }

        private  byte[] getRawKey(byte[] key) throws Exception {

            KeyGenerator kGen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(key);
            kGen.init(128, sr);
            SecretKey sKey = kGen.generateKey();
            byte[] raw = sKey.getEncoded();
            return raw;

        } 

    /************************************* Decription *********************************************/

            public String decrypt(String encryptedString) throws Exception {

                byte[] rawKey = getRawKey(Key.getBytes("UTF-8"));
                System.out.println("Decrypted Key in bytes : "+rawKey);


                System.out.println("Key in decryption :"+rawKey);


                SecretKeySpec sKeySpec = new SecretKeySpec(rawKey, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, sKeySpec);
                byte[] decrypted = cipher.doFinal(toByte(encryptedString));
                System.out.println("Decrypted mess in bytes---------->" +decrypted);
                return new String(decrypted);
            }






            public String toHex(byte[] buf) {
                if (buf == null)
                    return "";
                StringBuffer result = new StringBuffer(2*buf.length);
                for (int i = 0; i < buf.length; i++) {
                    appendHex(result, buf[i]);
                }
                return result.toString();
            }
            private final String HEX = "0123456789ABCDEF";
            private void appendHex(StringBuffer sb, byte b) {
                sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
            }

            public byte[] toByte(String hexString) {
                int len = hexString.length()/2;
                byte[] result = new byte[len];
                for (int i = 0; i < len; i++)
                    result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
                return result;
            }

    }
