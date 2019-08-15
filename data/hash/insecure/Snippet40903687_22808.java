def ii = (userDetailsInstance.imei ?: "") + (userDetailsInstance.devideId ?: "") + (userDetailsInstance.imsi ?: "")
        def digest = MessageDigest.getInstance("MD5")
        digest.update(ii.getBytes());
        def messageDigest = digest.digest();

        def hexString = new StringBuffer();
        for (int i=0; i<messageDigest.length; i++)
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
