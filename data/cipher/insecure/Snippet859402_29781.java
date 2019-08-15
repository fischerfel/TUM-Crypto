 next_pass :
        for (int pass = 0; pass < 100; pass++) {
          byte[] key = new byte[16];
          (new SecureRandom()).nextBytes(key);
          Cipher ciph = Cipher.getInstance("AES");
          SecretKeySpec ks = new SecretKeySpec(key, "AES");
          ByteBuffer bb = ByteBuffer.allocate(16);
          Set<String> already = new HashSet<String>(100000);
          int colls = 0;
          for (int i = 0; i < 200000; i++) {
            bb.putLong(0, i);
            ciph.init(Cipher.ENCRYPT_MODE, ks);
            byte[] encr = ciph.doFinal(bb.array());
            encr[0] &= 0x7f; // make all numbers positive
            BigInteger bigint = new BigInteger(encr);
            String userNo = bigint.toString();
            userNo = userNo.substring(4, 16);
            if (!already.add(userNo)) {
              System.out.println("Coll after " + i);
              continue next_pass;
            }
          }
          System.out.println("No collision.");
        }
