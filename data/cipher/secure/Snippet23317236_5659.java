 public static boolean verify () {
            String publickey = "MIGfMA0GCSqGSIb3DQE";
            byte[] encKey = Base64.decodeBase64(publickey.getBytes());
            try {
                byte[] MACaddress = GetData();
                BufferedReader in = new BufferedReader(new FileReader(
                        "EndSignatuer.txt"));
                FileInputStream keyfis = new FileInputStream("EndSignatuer.txt");
                byte[] Signen = new byte[keyfis.available()];
                keyfis.read(Signen);
                keyfis.close();

                X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, pubKey);
                byte[] deSignen = Base64.decodeBase64(Signen);
                byte[] decrypted_digest = cipher.doFinal(deSignen);

                MessageDigest md5_digest = MessageDigest.getInstance("MD5");
                md5_digest.update(MACaddress);
                byte[] digest = md5_digest.digest();

                   if (decrypted_digest == digest) {
                        return true;
                    }else {
                        return false;//her why give me false 
                    }
