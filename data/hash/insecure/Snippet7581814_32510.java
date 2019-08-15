final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
final byte[] data = stringToConvert.getBytes(); 
messageDigest.update(data,0,data.length);
final BigInteger hash = new BigInteger(1,messageDigest.digest());
return String.format("%1$032X", hash);
