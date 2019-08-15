String token = "H?OIgSJ35~LKJ:9~~7&sUtHDeKAv*O@is?cEwV[}!i@u%}";
MessageDigest cript = null;
try {
    cript = MessageDigest.getInstance("SHA1");
} catch (NoSuchAlgorithmException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
cript.reset();
cript.update(token.getBytes());
String password = new String(Hex.encodeHex(cript.digest()));
System.out.println(password);
