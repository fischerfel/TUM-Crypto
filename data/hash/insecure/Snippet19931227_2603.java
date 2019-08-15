byte[] array = MessageDigest.getInstance("MD5").digest("password".getBytes("UTF-8"));              
StringBuffer sb = new StringBuffer();
for (int i = 0; i < array.length; ++i) {
    sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));            
}
System.out.println(sb.toString());
