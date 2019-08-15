try {
        PackageInfo info = getPackageManager().getPackageInfo("Your Package Name",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.i("KeyHash:",
                    "KeyHash: "
                            + Base64.encodeToString(md.digest(),
                                    Base64.DEFAULT));

        }
    } catch (NameNotFoundException e) {
        Log.i("KeyHash !!!!!!!:", e.getMessage());

    } catch (NoSuchAlgorithmException e) {
        Log.i("KeyHash $$$$$$$:", e.getMessage());
    }
