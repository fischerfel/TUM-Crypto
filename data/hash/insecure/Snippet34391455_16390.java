try { SHH
        PackageInfo info = getPackageManager().getPackageInfo(
                "YOUR_PACKAGE",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("TAG:", Base64.encodeToString(md.digest(),  Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
