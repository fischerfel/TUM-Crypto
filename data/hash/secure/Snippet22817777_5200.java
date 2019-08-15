MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(user.getPassword().getBytes());

byte byteData[] = md.digest();

StringBuffer sb = new StringBuffer();
for (int i = 0; i < byteData.length; i++) {
    sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
            .substring(1));
}
user.setPassword(sb.toString());
