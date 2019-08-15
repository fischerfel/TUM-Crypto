            byte[] out=new byte[16];
            byte[] zero="00000000".getBytes();

            byte[] ivBytes; byte[] keyBytes; byte[] textBytes;
            ivBytes=Base64.decode("9pdfnd4JvpI=".getBytes("UTF-8"),Base64.DEFAULT);
            System.arraycopy(ivBytes,0,out,0,ivBytes.length);
            System.arraycopy(zero,0,out,ivBytes.length,zero.length);

            keyBytes=Base64.decode("/dn9yyUOTWobOQx1A+ZfAg==".getBytes("UTF-8"),Base64.DEFAULT);
            textBytes=Base64.decode("+JnDcw==".getBytes("UTF-8"),Base64.DEFAULT);
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(out);
            SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
            cipher.init(Cipher.DECRYPT_MODE, newKey,ivSpec);
            byte[] decodedBytes = cipher.doFinal(textBytes);
            String plain=new String(decodedBytes,"UTF-8");
