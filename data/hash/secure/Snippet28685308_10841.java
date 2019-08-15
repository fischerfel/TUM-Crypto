SecureRandom random = new SecureRandom();
byte[] salt = new byte[64];
random.nextBytes(salt);

String password="god";
MessageDigest md = MessageDigest.getInstance("SHA-512");
md.update(password.getBytes(Charset.forName("UTF-8")));
md.update(salt);

byte[] digest = md.digest();
