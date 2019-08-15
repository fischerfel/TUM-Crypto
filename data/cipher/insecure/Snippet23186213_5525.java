            String poem = "abcdef ghijkl";
           byte[] poem_b = poem.getBytes("UTF-8");
           Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
           c.init(Cipher.ENCRYPT_MODE, aliceDesKey);
           byte[] coded = new byte[16];


          coded = c.doFinal(poem_b);
          String codedbyte= new BASE64Encoder().encodeBuffer(coded);




          Path path_e = Paths.get("/poem_encrypted.txt");
    PrintWriter fout;

    fout = new PrintWriter("C://Users//Ankita//Desktop//poem_encrypted.txt");

    fout.println(codedbyte);
    fout.close();
    System.out.println("File coded.");

    }
