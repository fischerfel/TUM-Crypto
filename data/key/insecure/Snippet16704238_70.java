         public static final String constant = "1234";

         String key = constant + (machine ID);


         SecretKeySpec sks = new SecretKeySpec(key.getBytes(), "DES");

         String result = sks.toString();
