KeyGenerator kgen = KeyGenerator.getInstance("AES");kgen.init(128); 
SecretKey skey = kgen.generateKey();
byte[] bytes = skey.getEncoded();
SecretKeySpec skeySpec = new SecretKeySpec(bytes, "AES");
