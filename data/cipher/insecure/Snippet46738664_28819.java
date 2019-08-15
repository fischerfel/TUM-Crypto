            String strToDecrypt="6t8Z5bKl5ybJL+MiFerNBmiTDS7wlTEUdWNwJJApWmQ==";
            byte[] input = strToDecrypt.getBytes();

            //Decrypt
            Cipher b = Cipher.getInstance("DESede/ECB/NoPadding");
            b.init(Cipher.DECRYPT_MODE, keySpec);
            byte output[] = b.doFinal(input);
            String out = new String(output);
            System.out.println(new String(out));
