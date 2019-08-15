public String encrypt(String auth) {
           try {
               String cipherKey = "Key as a HEX string";
               byte[] rawKey = hexToBytes(cipherKey);
               SecretKeySpec keySpec = new SecretKeySpec(rawKey, "AES");
               Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

               String cipherIV = "xYzF5AqA2cKLbvbfGzsMwg==";
               byte[] btCipherIV = Base64.decodeBase64(cipherIV.getBytes());

               cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec (btCipherIV));
               byte[] unencrypted = StringUtils.getBytesUtf16(auth);
               byte[] encryptedData = cipher.doFinal(unencrypted);
               String encryptedText = null;

               byte[] entlib = new byte[btCipherIV2.length + encryptedData.length];
               System.arraycopy(btCipherIV, 0, entlib, 0, btCipherIV.length);
               System.arraycopy(encryptedData, 0, entlib, btCipherIV.length, encryptedData.length);

               encryptedText = new String(encryptedData);
               encryptedText = Base64.encodeBase64String(encryptedData);               
               return encryptedText;

           } catch (Exception e) {
           }

           return "";
       }

    public static byte[] hexToBytes(String str) {
          if (str==null) {
             return null;
          } else if (str.length() < 2) {
             return null;
          } else {
             int len = str.length() / 2;
             byte[] buffer = new byte[len];
             for (int i=0; i<len; i++) {
                 buffer[i] = (byte) Integer.parseInt(
                    str.substring(i*2,i*2+2),16);
             }
             return buffer;
          }

       }
