//          //**********AES Encryption
            //checking time in msec
            long startTime=System.currentTimeMillis();
            //System.out.println(startTime);
            //           // Create key and cipher
            SecretKey aesKey = KeysGeneration.getKeys(keypass);

            Cipher cipher = Cipher.getInstance("AES");
            //              // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(email.getBytes());
            System.out.println("checking encryption "+new String(encrypted));

            BASE64Encoder encoder = new BASE64Encoder();
            email = encoder.encode(encrypted);
            System.out.println("Encoded Encrypted Value="+email);

            // text="&email="+URLEncoder.encode(new String(encrypted), "UTF-8");
            writer.write("&email="+email);
            //Calculating Encryption Time
            long stopTime = System.currentTimeMillis();
            Duration ms=Duration.ofMillis(stopTime);

            //System.out.println(stopTime);
            long elapsedTime = stopTime - startTime;
            System.out.println("Time required for encryption (milliseconds)= "+elapsedTime);
