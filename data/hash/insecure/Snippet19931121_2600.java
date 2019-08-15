String password = "password";

MessageDigest digest = MessageDigest.getInstance("MD5");

ByteArrayInputStream bais = new ByteArrayInputStream(password.getBytes());

int size = 16;
byte[] bytes = new byte[size];
while ((bais.read(bytes, 0, size)) != -1)
{
  digest.update(bytes);
}

byte[] hash = digest.digest();
StringBuilder sb = new StringBuilder(2 * hash.length);
for (byte b : hash)
{
  sb.append(String.format("%02x", b & 0xff));
}

System.out.println("MD5:/ " + sb.toString());
