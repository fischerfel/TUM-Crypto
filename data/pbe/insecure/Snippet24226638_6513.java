                        try {
                            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                            KeySpec spec = new PBEKeySpec("i15646dont6321wanna".toCharArray(),"ahhalkdjfslk3205jlk3m4ljdfa85l".getBytes("UTF8"),65536,256);
                            SecretKey tmp = factory.generateSecret(spec);
                            SecretKey key = new SecretKeySpec(tmp.getEncoded(),"AES");

                            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                            IvParameterSpec ivspec = new IvParameterSpec(iv);
                            cipher.init(Cipher.DECRYPT_MODE, key, ivspec);

                            byte[] b = new byte[is.available()];
                            is.read(b);
/* Line 88 */               byte[] dec = cipher.doFinal(b);
                            SecretKey ck = new SecretKeySpec(dec,"AES");
                            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
                            c.init(Cipher.ENCRYPT_MODE, ck, ivspec);
                            eciphers.put(client, c);
                            c.init(Cipher.DECRYPT_MODE, ck, ivspec);
                            dciphers.put(client, c);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (InvalidAlgorithmParameterException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
