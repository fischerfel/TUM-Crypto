            MessageDigest digest = null;
......
            try {
                digest = MessageDigest.getInstance("MD5");
                ........
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
