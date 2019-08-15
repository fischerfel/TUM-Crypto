MessageDigest m = MessageDigest.getInstance("MD5");
m.reset();
m.update(password.getBytes());
byte[] digest = m.digest();
BigInteger bigInt = new BigInteger(1,digest);
String hashtext = bigInt.toString(16);

while(hashtext.length() < 32 ){
  hashtext = "0"+hashtext;
}
