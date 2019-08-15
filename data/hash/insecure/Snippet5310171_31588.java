MessageDigest md = MessageDigest.getInstance("MD5");


......


synchronized (md) {

md.reset(); 
byte[] hash = md.digest(plainTextPassword.getBytes("CP1252"));

StringBuffer sb = new StringBuffer();
for (int i = 0; i < hash.length; ++i) {
sb.append(Integer.toHexString((hash[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
}

String password = sb.toString();
}
