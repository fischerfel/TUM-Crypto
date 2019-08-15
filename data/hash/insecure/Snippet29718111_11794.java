    public  void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.superreceptionist",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                 String sign=Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", sign);
                //  Toast.makeText(getApplicationContext(),sign,     Toast.LENGTH_LONG).show();
            }
            Log.d("KeyHash:", "****------------***");
        } catch (PackageManager.NameNotFoundException e) {
e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
e.printStackTrace();
        }
    }
