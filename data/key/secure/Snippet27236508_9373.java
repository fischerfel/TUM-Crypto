key = Arrays.copyOf(key, 32); // Use only first 256 bit
this.keyObj = new SecretKeySpec(key, "AES");
