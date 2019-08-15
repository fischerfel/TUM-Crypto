public void encryptfile(String encpathname , String destencfile){

        try {

            byte enc[] = null;

            File file = new File(encpathname);
            byte[] data = new byte[(int) file.length()];

            FileInputStream fis;

                fis = new FileInputStream(file);
                fis.read(data);
                fis.close();

                enc = encrypt(passphrase, data);

                FileOutputStream stream = new FileOutputStream(destencfile);
                stream.write(enc);
                stream.close();

                file.delete();

                System.gc();

                }catch (Exception e) {

                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
    }

private static byte [] encrypt(String passphrase, byte [] inbytes) throws Exception {
                SecretKey key = generateKey(passphrase);

                Cipher cipher = Cipher.getInstance("AES/CTR/NOPADDING");
               // Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

                cipher.init(Cipher.ENCRYPT_MODE, key, generateIV(cipher), random);
                return cipher.doFinal(inbytes);
            }

            private static SecretKey generateKey(String passphrase) throws Exception {
                PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(), iterations, keyLength);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWITHSHA256AND256BITAES-CBC-BC");
                return keyFactory.generateSecret(keySpec);
            }

            private static IvParameterSpec generateIV(Cipher cipher) throws Exception {
                byte [] ivBytes = new byte[cipher.getBlockSize()];
                random.nextBytes(ivBytes);
                return new IvParameterSpec(ivBytes);
            }
