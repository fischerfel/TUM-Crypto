     spec = new PBEKeySpec(
            // password.toCharArray(),
            saltBytes, // byte[] cannot be converted into char[] I am getting error here 
            pswdIterations,
            keySize
    );
