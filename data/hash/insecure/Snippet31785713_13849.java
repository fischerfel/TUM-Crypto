try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "your package name", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign = Base64
                        .encodeToString(md.digest(), Base64.DEFAULT);

                Toast.makeText(getApplicationContext(), sign, Toast.LENGTH_LONG)
                        .show();
            }

        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
