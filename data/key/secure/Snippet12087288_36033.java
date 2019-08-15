public static String crypt(String input, String key){
            byte[] crypted = null;
            try{
                SecretKeySpec skey = new SecretKeySpec(org.apache.commons.codec.binary.Base64.decodeBase64(key), "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, skey);
                crypted = cipher.doFinal(input.getBytes());
            }catch(Exception e){
            }
           return org.apache.commons.codec.binary.Base64.encodeBase64String(crypted);
        }
