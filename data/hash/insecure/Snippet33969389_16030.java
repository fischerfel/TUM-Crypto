if (savedInstanceState == null) {
        // Add the fragment on initial activity setup
        try {
            Log.i("TAG", "Inside Hash Key");
            PackageInfo info = getPackageManager().getPackageInfo("com.package", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, mainFragment).commit();

    } else {

        // Or set the fragment from restored state info
        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
    }
    try {
        Log.i("TAG", "Inside Hash Key");
        PackageInfo info = getPackageManager().getPackageInfo("com.package", PackageManager.GET_SIGNATURES);

        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    } catch (PackageManager.NameNotFoundException e) {

    } catch (NoSuchAlgorithmException e) {

    }
