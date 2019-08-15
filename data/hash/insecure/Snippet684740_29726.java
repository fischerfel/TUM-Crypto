MessageDigest m=MessageDigest.getInstance("MD5");
m.update(image.getBytes(),0,image.length());
System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
