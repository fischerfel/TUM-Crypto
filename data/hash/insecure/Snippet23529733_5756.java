byte[] id = plainPwd.getBytes();

MessageDigest md = MessageDigest.getInstance("MD5");
md.update(id);
md.update("99991231".getBytes());           // “99991231” mentioned in XML-API DOC

byte[] buffer = md.digest();
StringBuffer sb = new StringBuffer();
for (int i = 0; i <buffer.length; i++) {
    sb.append(Integer.toHexString((int) buffer[i] & 0xff));
}
String md5Pwd = sb.substring(0, 8);         // only use first 8 characters
