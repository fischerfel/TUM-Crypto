MessageDigest md= MessageDigest.getInstance("SHA-1");         
md.update(hashString.getBytes("UTF-8"),0,0);
byte[] digest = null;
md.digest(digest,0,digest.length);
System.out.println("digest of str is "+new String(digest) );
