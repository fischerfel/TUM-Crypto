    // build the initialization vector.  This example is all zeros, but it 
    // could be any value or generated using a random number generator.
    byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    IvParameterSpec ivspec = new IvParameterSpec(iv);
