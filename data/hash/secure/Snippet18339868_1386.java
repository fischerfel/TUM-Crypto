MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(stringAsbytes);
stringAsBytes = md.digest();
