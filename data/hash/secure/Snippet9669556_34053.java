String passwordSalt = "Somesalt";
byte[] bsalt=base64ToByte(passwordSalt);
byte[] passwordToDigestAsBytes=("somepassword").getBytes("UTF-8");

MessageDigest digest = MessageDigest.getInstance("SHA-512");
digest.reset();
digest.update(bsalt);
byte[] = input digest.digest(passwordToDigestAsBytes);
