 try {
        PackageInfo info = getPackageManager().getPackageInfo(
                "integrate.social.arifhasnat.facebooklogindemo",
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            System.out.println(Base64.encodeToString(md.digest(), Base64.DEFAULT));
            Toast.makeText(MainActivity.this, Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_SHORT).show();
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
