 try {
            PackageInfo info = getPackageManager().getPackageInfo("your pakage name here", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", "key is: "+Base64.encodeToString(md.digest(), Base64.DEFAULT));

                }
        } catch (NameNotFoundException e) {

            Log.e("error","error name not found");
        } catch (NoSuchAlgorithmException e) {

            Log.e("error","error no algorithm");

        }**strong text**
