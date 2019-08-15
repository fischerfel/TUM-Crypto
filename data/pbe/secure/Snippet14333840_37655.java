SecretKey MyKey = SecretKeyFactory.getInstance("DES")
                    .generateSecret(new PBEKeySpec(Password.toCharArray()));
