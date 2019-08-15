KeySpec ks = new PBEKeySpec(password.toCharArray(), "somepredefinedsalt".getBytes(), numIters, keySizeInBits);
