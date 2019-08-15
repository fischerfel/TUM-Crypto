private void genertaeHashKey() {
        Log.d("DEBUG", "Generating Hash Key");
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", "" + e);
            Toast.makeText(SplashScreenActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        } catch (NoSuchAlgorithmException e)

        {
            Log.e("KeyHash:", "" + e);
        }
    }
