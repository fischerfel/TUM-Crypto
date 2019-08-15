 try {
            Log.v("TAG_PACKNAME",""+"UUUU");
            PackageInfo info = getPackageManager().getPackageInfo(
                    "Your package name",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.v("TAG_PACKNAME",""+ Base64.encodeToString(md.digest(), Base64.DEFAULT));  // not printing

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
