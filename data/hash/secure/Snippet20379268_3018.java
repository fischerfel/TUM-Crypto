String password = ((TextView)findViewById(R.id.passwordr1)).getText().toString();
MessageDigest messageDigest = MessageDigest.getInstance("SHA-256"); //$NON-NLS-1$
messageDigest.update(password.getBytes());
String encryptedPassword = new String(messageDigest.digest());
