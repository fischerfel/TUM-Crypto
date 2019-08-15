        private static final String ALGORITHM = "AES";

        private static final String TRANSFORMATION = "AES";

        public static  void encrypt(String key, File inputFile, File outputFile)
                throws ExtendedException {
            doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
        }

        public static  void decrypt(String key, File inputFile, File outputFile)
                throws ExtendedException {
            doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
        }

        private static void doCrypto(int cipherMode, String key, File inputFile,
                File outputFile) throws ExtendedException {
            try {
                Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
                Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                cipher.init(cipherMode, secretKey);

                FileInputStream inputStream = new FileInputStream(inputFile);


                CipherOutputStream out = new CipherOutputStream(new FileOutputStream(outputFile), cipher);
                byte[] buffer = new byte[8192];
                byte[] outputBytes = null;
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                int count;
                while ((count = inputStream.read(buffer)) > 0)
                {
                    out.write(buffer, 0, count);
                    outputBytes = cipher.doFinal(buffer);

                }


                inputStream.close();
                outputStream.close();

            } catch (NoSuchPaddingException | NoSuchAlgorithmException
                    | InvalidKeyException | BadPaddingException
                    | IllegalBlockSizeException | IOException ex) {
                throw new ExtendedException("Error encrypting/decrypting file", ex);
            }
        }
