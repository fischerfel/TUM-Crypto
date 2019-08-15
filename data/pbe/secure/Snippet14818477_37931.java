int derivedKeyLength = 128;
...
KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations,
        derivedKeyLength);
