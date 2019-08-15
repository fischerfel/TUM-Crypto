String vote = br.readLine();
// the data that i now encrypt using RSA

          PublicKey pubKey = readKeyFromFilepublic("alicepublic.txt");
          Cipher cvote = Cipher.getInstance("RSA");
          cvote.init(Cipher.ENCRYPT_MODE, pubKey);
          byte[] voted = cvote.doFinal(vote.getBytes());
          System.out.println(voted);
          out.println(voted.length);
          dos.write(voted,0,voted.length); // here i am sending the array to the server
