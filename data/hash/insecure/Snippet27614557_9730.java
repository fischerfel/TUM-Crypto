String str1 = "unoshriram";
String outHash = "95e5f0b6988ec703e832172f70ce7dc7";
try {
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] array = md.digest(str1.getBytes("UTF-8"));
    StringBuilder sb = new StringBuilder();
    for (byte b : array) {
        sb.append(String.format("%02X", b));
    }
    System.out.println(sb.toString());
    System.out.println(sb.toString().equalsIgnoreCase(outHash));
} catch (Exception e) {
    e.printStackTrace();
}
