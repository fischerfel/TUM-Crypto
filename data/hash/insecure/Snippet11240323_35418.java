MessageDigest md = MessageDigest.getInstance("SHA-1"); 
return byteArray2Hex(md.digest(convertme));
