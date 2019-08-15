   private static final String UNICODE_FORMAT = "UTF8";
            public static final String DESEDE_ENCRYPTION_SCHEME = "DESede"; //"DESede/ECB/NoPadding";
            private KeySpec ks;
            private SecretKeyFactory skf;
            private Cipher cipher;
            byte[] arrayBytes;
            private String myEncryptionKey;
            private String myEncryptionScheme;
            SecretKey key;

            public PasswordEncryption_TrippleDES() throws Exception {
                myEncryptionKey =  "ThisIsSpartaThisIsanilku";
                myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
                arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
                ks = new DESedeKeySpec(arrayBytes);
                skf = SecretKeyFactory.getInstance(myEncryptionScheme);
                cipher = Cipher.getInstance(myEncryptionScheme);
                key = skf.generateSecret(ks);
            }


            public String encrypt(String unencryptedString) {
                String encryptedString = null;
                try {
                    cipher.init(Cipher.ENCRYPT_MODE, key);
                    byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
                    byte[] encryptedText = cipher.doFinal(plainText);
                    encryptedString = new String(Base64.encodeBase64(encryptedText));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return encryptedString;
            }


            public String decrypt(String encryptedString) {
                String decryptedText=null;
                try {
                    cipher.init(Cipher.DECRYPT_MODE, key);
                    byte[] encryptedText = Base64.decodeBase64(encryptedString);
                    byte[] plainText = cipher.doFinal(encryptedText);
                    decryptedText= new String(plainText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return decryptedText;
            }
 public static void main(String args []) throws Exception
        {
            PasswordEncryption_TrippleDES td= new PasswordEncryption_TrippleDES();

            String target="data for encyption";
            String encrypted=td.encrypt(target);
            String decrypted=td.decrypt(encrypted);

            System.out.println("String To Encrypt: "+ target);
            System.out.println("Encrypted String:" + encrypted);
            System.out.println("Decrypted String:" + decrypted);

        }
