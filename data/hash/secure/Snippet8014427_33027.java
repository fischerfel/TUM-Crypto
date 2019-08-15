String all = varbyte1+varstr1+varbyte2;
MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] digest = md.digest(all.getBytes("UTF-8"));
String hash = String.format("%0" + (digest.length*2) + "X", new BigInteger(1, digest));
