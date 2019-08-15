MessageDigest md = MessageDigest.getInstance("SHA-256");

byte[] hash = md.digest("password".getBytes());

BigInteger bI = new BigInteger(1, hash);

System.out.println(bI.toString(16));
