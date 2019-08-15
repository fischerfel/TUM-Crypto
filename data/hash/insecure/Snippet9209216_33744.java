MessageDigest md = MessageDigest.getInstance("MD5");
byte[] digest = md.digest(pr.getEncoded());
System.out.println(Arrays.toString(digest));
