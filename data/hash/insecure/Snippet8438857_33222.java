MessageDigest md = MessageDigest.getInstance("SHA-1");
byte [] ba = cadena.getBytes();
byte [] digest  = md.digest(ba);
