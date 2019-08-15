SecretKey signingKey = new SecretKeySpec("123".getBytes(), "HMACSHA256");  
    Mac mac = Mac.getInstance("HMACSHA256");  
    mac.init(signingKey);  
    byte[] digest = mac.doFinal("ABCDEF".getBytes());     
    System.out.println("HMA : "+digest.length);
