try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.org.package", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign = Base64
                        .encodeToString(md.digest(), Base64.DEFAULT);

                    Log.e("MY KEY HASH:", sign);

            }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
