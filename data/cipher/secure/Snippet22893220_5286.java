public class TestSecurityDiscussions {

        public static byte[] encryptData(KeyPair keys){

            String rawData = "Hi how are you>?";
            Cipher cipher = Cipher.getInstance("RSA");
            try {
                cipher.init(Cipher.ENCRYPT_MODE, keys.getPublic());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            byte[] encrypted = cipher.doFinal(rawData.getBytes());

            return encrypted;
        }


        public static String decryptData(byte[] encrypted,KeyPair keys) {
            Cipher cipher = Cipher.getInstance("RSA");
            try {
                cipher.init(Cipher.DECRYPT_MODE, keys.getPrivate());
            } catch (Exception e) {

                e.printStackTrace();
            }
            byte[] deycrypted = cipher.doFinal(encrypted);

            return deycrypted.toString();
        }


        public static void main(String[] args)   {
            KeyPair keys = KeyPairGenerator.getInstance("RSA").generateKeyPair();
            byte[] keydata = encryptData(keys);
            System.out.println("======>"+decryptData(keydata,keys));

        }

    }
