            // Ignore this line this is for encoding
            //String input = "Congratulation, You've sucessfully decoded!";

            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // When I tried with this its gives "pad block corrupted" exception else work as above i told

            /*byte[] key = CommonUtilities.encryptionKey.getBytes("UTF-8");
            System.out.println(key.length);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            System.out.println(key.length);
            System.out.println(new String(key,"UTF-8"));
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");*/

            // encryptionKey = "12345678901234561234567890123456"; Same in IOS and PHP
            SecretKeySpec skeySpec = new SecretKeySpec(CommonUtilities.encryptionKey.getBytes("UTF-8"), "AES");
            Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

            // Ignore these lines these are for encoding
            /*ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] dstBuff = ecipher.doFinal(input.getBytes("UTF-8"));              
            System.out.println("encrypted: " + new String(dstBuff, "UTF-8")); 
            String enbin2hex = com.byte2hex(dstBuff);    
            String en = Base64.encodeToString(dstBuff, Base64.DEFAULT);*/    


            // this is Hex2Binay that IOS gives me to decrypt
            // Original Text: "hello shani how are you doing , Stuck in encryption ?"
            String strBin2Hex = "30BEF4AB063D0D72F91D8D11A7ADEE1B1EC58F67C4D9CC20F59FB56B8B23B7C665198CFF805897BD1AFB82E578AC82C6C18C0EA909E17540D0B95A81E8446168";



            ecipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] de = ecipher.doFinal(com.hex2Byte(strBin2Hex));  

            //de = removeTrailingNulls(de);
            //int bytesDecryptedAfter = de.length;

            System.out.println("decrypted: " + new String(de, "UTF-8"));
            // Decrypted String "igohj&t`hnh"kkr&are you doing , Stuck in encryption ?"
