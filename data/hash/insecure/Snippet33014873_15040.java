try {
        PackageInfo info = context.getPackageManager().getPackageInfo(
                packageName, PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());

            // writtenToFile("FB_KEY_HASH.txt",
            // Base64.encodeToString(md.digest(),
            // Base64.DEFAULT).toString(), false);

            if (AppUtills.showLogs)
                Log.v(pageName,
                        "KeyHash:"
                                + Base64.encodeToString(md.digest(),
                                        Base64.DEFAULT));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
