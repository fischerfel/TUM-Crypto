                 try{
                        SecretKeySpec skey = new SecretKeySpec(Hex.decodeHex(key.toCharArray()), "AES");
                        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        cipher.init(Cipher.ENCRYPT_MODE, skey);
                        output = cipher.doFinal(bFile);
                        String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();
                        FileOutputStream fileOuputStream = new FileOutputStream(SD_CARD_PATH+ "/" + "abcd.db"); 
                        fileOuputStream.write(output);
                        fileOuputStream.close();
                        //System.out.println(output);
                    }catch(Exception e){
                        System.out.println("Error: "+e);
                    }
