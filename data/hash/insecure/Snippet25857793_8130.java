        String aid = Settings.Secure.getString(context.getContentResolver(), "android_id");
        try {
            Object obj;
            ((MessageDigest) (obj = MessageDigest.getInstance("MD5"))).update(aid.getBytes(), 0, aid.length());

            aid = String.format("%032X", new Object[] { new BigInteger(1, ((MessageDigest) obj).digest()) });
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            aid = aid.substring(0, 32);
        }

        adRequest.addTestDevice(aid);
