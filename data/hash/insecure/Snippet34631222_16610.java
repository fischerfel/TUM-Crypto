public static boolean checkSignature(Context context,String keyToCheck) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(<your package name>, PackageManager.GET_SIGNATURES);
            for (Signature sig : pi.signatures) {
                MessageDigest sha = MessageDigest.getInstance("SHA-1");
                sha.update(sig.toByteArray());
                byte[] digest = sha.digest();
                String str = "";
                for (int di = 0; di < digest.length; di++) {
                    str += Byte.toString(digest[di]) + ":";
                }
                if (str.equals(keyToCheck)) {
                    return true;
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();

        }
      return false;
    }
