String s="Nobody inspects the spammish repetition";
MessageDigest m=MessageDigest.getInstance("MD5");
m.update(s.getBytes(),0,s.length());
System.out.println(new BigInteger(1,m.digest()).toString(16));
