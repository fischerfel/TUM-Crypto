     RandomAccessFile raf = new RandomAccessFile(fileName, "r");
     final byte[] stored = new byte[(int) raf.length()]; // in File : "9F3F4E45C0B58B410333D5CB45EB94B708285E80E77F85E1545B9CED25044EAF9158EADB";
     String saltedPw="2013527f8d155ecf0";

     byte[] saltedPassword=saltedPw.getBytes();
     byte[] sha1 = MessageDigest.getInstance("SHA-1").digest(saltedPassword);
     byte[] md5 = MessageDigest.getInstance("MD5").digest(saltedPassword);
     byte[] hashed=(toHex(sha1)+toHex(md5)).getBytes();
     Arrays.equals(hashed, stored);    // its mismatching here
