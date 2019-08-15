    try {

        PackageInfo info = getPackageManager().getPackageInfo(
                    "Your PAckage here", 
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
    //              //              System.out.println("KEY HASH.........."+Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (NameNotFoundException e) {
                System.out.println("name not found...."+e);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("NoSuchAlgorithmException...."+e);
            }
