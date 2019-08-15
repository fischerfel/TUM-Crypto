      String key = "HkJHBKJBvffdbv";
      String IV= "qjfghftrsbdghzir";
      String theMessageToCifer ="your message";

      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");

      IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
      try{

      //specify your mode
      Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec,ivSpec);

      encrypted = cipher.doFinal(theMessageToCifer.getBytes());

      bytesEncoded = Base64.encode(encrypted);
      System.out.println(" base64 code " +bytesEncoded);
      System.out.println("encrypted string: " +encrypted);
      // decryption
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec,ivSpec);
      byte[] original = cipher.doFinal(encrypted);
      String originalString = new String(original);
      System.out.println("Original string: " + originalString );
      }catch (Exception e){
         e.printStackTrace();
      }   
