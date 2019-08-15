    private byte[] rsaDecrypt(RSAPrivateCrtKey rsaprivateCrtKey, byte[] ciphertext) throws IOException {
            System.out.println("\n----------------DECRYPTION STARTED------------");
            byte[] descryptedData = null;
            int cipher1= ciphertext.length;
            System.out.println(cipher1);
            try {
                RSAPrivateCrtKey privateKey = readPrivateKeyFromFile(PRIVATE_KEY_FILE);
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                descryptedData = cipher.doFinal(ciphertext);
                System.out.println("Decrypted Data: " + new String(descryptedData));

            } catch (Exception e) {
                e.printStackTrace();
            }   

            System.out.println("----------------DECRYPTION COMPLETED------------");
            return descryptedData;      
        }



            public RSAPrivateCrtKey readPrivateKeyFromFile(String fileName) throws IOException{
                FileInputStream fis = null;
                ObjectInputStream ois = null;
                try {
                    fis = new FileInputStream(new File(fileName));
                    ois = new ObjectInputStream(fis);

                    BigInteger modulus = (BigInteger) ois.readObject();
                    BigInteger exponent = (BigInteger) ois.readObject();

                    //Get Private Key
                    RSAPrivateCrtKeySpec rsaPrivateCrtKeySpec = new RSAPrivateCrtKeySpec(modulus, exponent, exponent, exponent, exponent, exponent, exponent, exponent);
                    KeyFactory fact = KeyFactory.getInstance("RSA");
                    RSAPrivateKey privateKey = (RSAPrivateKey) fact.generatePrivate(rsaPrivateCrtKeySpec);

                    return (RSAPrivateCrtKey) privateKey;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally{
                    if(ois != null){
                        ois.close();
                        if(fis != null){
                            fis.close();
                        }
                    }
                }
                return null;
            }
        }
