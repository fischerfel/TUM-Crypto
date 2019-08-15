public static SecretKey loadKey(String  keyString)  {


            byte[] encoded = keyString.getBytes();

            SecretKey key = new SecretKeySpec(encoded, "AES");
            return key;
        }
