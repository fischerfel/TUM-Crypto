            Path pathP = Paths.get("../poem_encrypted.txt");//path provided in code

        byte[] poem = Files.readAllBytes(pathP);

        String codedbyte= poem.toString();

        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, bobDesKey);


        byte[] decoder = new BASE64Decoder().decodeBuffer(codedbyte);
        byte[] msgbyte = c.doFinal(decoder);
        String message = new String(msgbyte);


        System.out.println("message1"+message);
        Path path_e = Paths.get("/poem_decrypted.txt");
        FileOutputStream fout;

        fout = new FileOutputStream("/poem_decrypted.txt");

        fout.write(msgbyte);
        fout.close();
        System.out.println("File coded.");




            }
    catch (Exception e)
    {
        System.out.println("Error" + e);
    }
