  `String psw="hello";  
   String tobehashed="";
   tobehashed=salt+psw;
   MessageDigest md = MessageDigest.getInstance("SHA-256");
   byte[] digest = md.digest(tobehashed.getBytes());
   System.out.println("Digest:"+digest);` 
