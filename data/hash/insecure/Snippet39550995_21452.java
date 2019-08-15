byte[] bytes;
java.security.MessageDigest d = null;
d = java.security.MessageDigest.getInstance("SHA-1");
d.reset();
d.update(str.getBytes());
bytes = d.digest();
StringBuilder s = new StringBuilder();
for(byte b : bytes){
    s.append(Util.binToHex(Util.unsign(b)));
}
return s.toString();
