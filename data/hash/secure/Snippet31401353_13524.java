byte[] password = "firstpassword".getBytes();
byte[] salt = "FooBarBaz".getBytes();

MessageDigest digest = MessageDigest.getInstance("SHA-512");
digest.reset();
digest.update(salt);
byte[] hashed = digest.digest(password);

String encodedHash = Base64.getEncoder().encodeToString(hashed);

System.out.printf("{SHA512-CRYPT}$6$%s$%s", "FooBarBaz",encodedHash);
