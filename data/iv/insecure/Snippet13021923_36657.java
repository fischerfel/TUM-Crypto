        String myiv = new String(new byte[] {
                0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0x31,0x30,0x31,0x31,0x31,0x32,0x33
        });

        char [] mykeyarray = new char[] {0x86,0xe5,0x30,0x90,0xff,0x62,0xa0,0x9a,0x81,0x00,0xad,0x9e,0x8f,0x00,0x00,0x00};
        String encoded = "dm8cfvs+c7pKM+WR+fde8b06SB+lqWLS4sZW+PfQSKtTfgPknzYzpTVOtJP3JBoU2Uo/7XWopjoPDOlPr24duuck0z+vAx91bYTwQo4INnIIBkj/lhJMWmvAKaUIO3qzBoGg8ynQOhuG6LY7Wo0uww==";

        IvParameterSpec ivspec = new IvParameterSpec(myiv.getBytes());

        byte [] decoded;    
        FileWriter fstream = new FileWriter("out.txt");
        BufferedWriter out = new BufferedWriter(fstream);
        String mykey;
        int repeat = 256;

        outerloop:
        for(int i=0;i<repeat;i++){
            for(int j=0;j<repeat;j++){
                for(int k=0;k<repeat;k++){

                    mykey = new String(mykeyarray);

                    SecretKeySpec keyspec = new SecretKeySpec(mykey.getBytes(), "AES");

                    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

                    System.out.println("I: "+i+" J: "+j+" K: "+k); 

                    decoded = new BASE64Decoder().decodeBuffer(encoded); 

                    cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

                         byte [] decrypted = cipher.doFinal(decoded);
                         String dec = new String(decrypted);

                         if(dec.contains("Mary")){
                             out.write(dec);
                            out.write("\n");
                            System.out.println(dec);
                            break outerloop;
                         }

                            mykeyarray[15]++;
                }
                mykeyarray[14]++;
                mykeyarray[15]=0x00;
            }
            mykeyarray[13]++;
            mykeyarray[14]=0x00;
            mykeyarray[15]=0x00;
        }
            out.close();
    }

    catch(Exception e){
        System.out.println("Error: " + e.getMessage());
    }
}
