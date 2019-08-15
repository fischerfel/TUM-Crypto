        // creation of SHA-1 instance:
        MessageDigest message = MessageDigest.getInstance("SHA-1");
        // construction of the string to hash:
        String secretMessage = "0" + myPassWord + mySalt;
        // updating the instance:
        message.update(secretMessage.getBytes());
        // cloning the instance:
        MessageDigest messageClone = (MessageDigest) message.clone();
        // digesting the clone: the result is of type byte[]:
        byteResult = messageClone.digest();
        // construction of the previousHash: this will be used in the 
        // next run of SHA-1 hashing. Python runs everything in lowercase.
        // the hash is rendered as HEX characters String:
        prevHash = (DatatypeConverter.printHexBinary(byteResult)).toLowerCase();
        secretMessage = prevHash + "1" + myPassWord + mySalt;
        message.update(secretMessage.getBytes());
        // compute the final digest:
        byteResult = message.digest();
        // print it:
        System.out.println(DatatypeConverter.printHexBinary(byteResult));
