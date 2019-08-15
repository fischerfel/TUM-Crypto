try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "your package", 
                PackageManager.GET_SIGNATURES);
        for (android.content.pm.Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
    } catch (NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
