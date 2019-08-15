MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashText = bigInt.toString();
