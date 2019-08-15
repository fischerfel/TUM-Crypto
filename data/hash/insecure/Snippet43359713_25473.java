try {
  MessageDigest md = MessageDigest.getInstance("MD5");
  byte[] byteArray = md.digest(myString.getBytes("UTF-8"));
  StringBuffer sb = new StringBuffer();
  for (int i = 0; i < byteArray.length; i++) {
    sb.append(Integer.toString((byteArray[i] & 0xff) + 0x100, 16).substring(1));
  }

  int n = (int) Long.parseLong(sb.toString());
  System.out.println(n);
} catch (Exception e) {
  System.out.println(e.getMessage());
}
