                private static final String ALGO = "AES";
                // private static final String ALGO = "ARCFOUR";
                private static String keyString;
                private static byte[] keyValue;
                private static Key key;
                private static Cipher c1;
                private static Cipher c2;

                private static void about() {
                                StringBuffer sb = new StringBuffer(128);
                                sb.append("Encryption version 1");
                                System.out.println(sb.toString());
                }

                public static String encrypt(String Data) throws Exception {
                                byte[] encVal = c1.doFinal(Data.getBytes());
                                String encryptedValue = Base64.encodeBase64String(encVal);
                                return encryptedValue;
                }

                public static String decrypt(String encryptedData) throws Exception {
                                byte[] decordedValue = Base64.decodeBase64(encryptedData);
                                byte[] decValue = c2.doFinal(decordedValue);
                                String decryptedValue = new String(decValue);
                                return decryptedValue;
                }

                public static void main(String[] args) throws Exception
                  {

                    String data = null;
                    String dataEnc = null;
                    String dataDec = null;

                    long start = Calendar.getInstance().getTimeInMillis();
                    for (int i=0; i<1000000; i++)
                    {
                      data = "S0me Very Har!!d Pa$$w0rd !!";
                      dataEnc = AES256.encrypt(data);
                      dataDec = AES256.decrypt(dataEnc);
                    }
                    long end = Calendar.getInstance().getTimeInMillis();
                    System.out.println("Time millisecons: " + (end-start));

                    System.out.println("Plain Text : " + data + ", Length=" + data.length());
                    System.out.println("Encrypted Text : " + dataEnc);
                    System.out.println("Decrypted Text : " + dataDec + ", Length=" + dataDec.length());
                  }


                static {
                                try {
                                                about();
                                                File f = new File(".password");
                                                if (!f.exists() || !f.canRead()) {
                                                                f = new File(System.getenv("HOME")    + "/pps/.password");
                                                }

                                                BufferedReader reader = new BufferedReader(new FileReader(f));
                                                keyString = reader.readLine();
                                                reader.close();
                                } catch (IOException ioe) {
                                                keyString = "thesecretekey";
                                }
                                try {
                                                /*
                                                * KeyGenerator generator =
                                                * KeyGenerator.getInstance("AES/CTR/PKCS5PADDING");
                                                * generator.init(128); SecretKey key = generator.generateKey();
                                                * Cipher cipher = Cipher.getInstance("AES");
                                                * cipher.init(Cipher.ENCRYPT_MODE, key);
                                                */
                                                keyValue = DatatypeConverter.parseHexBinary(keyString);
                                                key = new SecretKeySpec(keyValue, ALGO);
                                                c1 = Cipher.getInstance(ALGO);
                                                c1.init(Cipher.ENCRYPT_MODE, key);
                                                c2 = Cipher.getInstance(ALGO);
                                                c2.init(Cipher.DECRYPT_MODE, key);
                                } catch (NoSuchPaddingException nop) {
                                                nop.printStackTrace();
                                } catch (NoSuchAlgorithmException noa) {
                                                noa.printStackTrace();
                                } catch (InvalidKeyException e) {
                                                e.printStackTrace();
                                } catch (Exception othere) {
                                                othere.printStackTrace();
                                }
                }

}
