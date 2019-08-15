  MessageDigest md5 = MessageDigest.getInstance("md5");
  for (int i = 0; i < 10; i++) {
     String s = "" + i;
     byte[] bs = s.getBytes("UTF-8");
     md5.update(bs);

     String currMd5 = ...;
     System.out.println(currMd5);
  }
