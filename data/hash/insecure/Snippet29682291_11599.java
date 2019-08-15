  MessageDigest md5 = MessageDigest.getInstance("md5");

  for (int i = 0; i < 10; i++) {
     String s = "" + i;
     byte[] bs = s.getBytes("UTF-8");
     md5.update(bs);

     MessageDigest md5c = (MessageDigest) md5.clone(); 

     byte[] bytes = md5c.digest();
     String currentMd5 = javax.xml.bind.DatatypeConverter.printHexBinary(bytes);
     System.out.println("md5: " + currentMd5);
  }
