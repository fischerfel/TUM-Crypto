public static String encryptAES_Java_Node(String content, String key) {
 
              byte[] input;
              String query = null;
              try {
                     input = content.getBytes("utf-8");
 
                     MessageDigest md = MessageDigest.getInstance("MD5");
                     byte[] thedigest = md.digest(key.getBytes("UTF-8"));
                     SecretKeySpec skc = new SecretKeySpec(thedigest,
                                  "AES/ECB/PKCS5Padding");
                     Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                     cipher.init(Cipher.ENCRYPT_MODE, skc);
 
                     byte[] cipherText = newbyte[cipher.getOutputSize(input.length)];
                     int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
                     ctLength += cipher.doFinal(cipherText, ctLength);
 
                     query = Base64.encodeToString(cipherText, Base64.DEFAULT);
                    
              } catch (UnsupportedEncodingException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              } catch (NoSuchAlgorithmException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              } catch (NoSuchPaddingException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              } catch (InvalidKeyException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              } catch (IllegalBlockSizeException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              } catch (ShortBufferException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              } catch (BadPaddingException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              }
              return query;
 
       }
