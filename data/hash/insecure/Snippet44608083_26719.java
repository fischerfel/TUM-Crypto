try {
         MessageDigest hashDigester = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
return (new BigInteger(1, hashDigester.digest(pin.getBytes())).toString(16)).equals(object.getPin());
