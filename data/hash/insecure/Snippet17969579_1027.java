// Add code to print out the key hash
        try {
            System.out.println("Inside try for keyhash");
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.myapp.facebookint", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.println("KeyHash:"+Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {
            System.out.println("keyhash name not found");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("keyhash algo not found");
        }
