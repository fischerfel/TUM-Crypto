public void KEY(){
        try {
            PackageInfo info =     getActivity().getPackageManager().getPackageInfo("com.YourPackageName",     PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign=Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
                Toast.makeText(getActivity().getApplicationContext(),sign,     Toast.LENGTH_LONG).show();
                System.out.println(sign); 
            }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }
