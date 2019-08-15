String input = "11252411445171911438526";
MessageDigest md = MessageDigest.getInstance("SHA-1");
md.reset();
md.update(input.getBytes("utf8"));
String ouput = new BigInteger(1, md.digest()).toString(16);
