    MessageDigest messageDigest = null;

    try{
        messageDigest = MessageDigest.getInstance("MD5");
    }catch(NoSuchAlgorithmException e){
        System.out.println("Error: " + e);
    }

    messageDigest.reset();
    messageDigest.update(inPassword.getBytes());
    byte[] digest = messageDigest.digest();
    BigInteger bigInt = new BigInteger(1, digest);
    String encodedPass = bigInt.toString(16);

    while (encodedPass.length() < 32) {
        encodedPass = "0" + encodedPass;
    }


    inPassword = encodedPass;
