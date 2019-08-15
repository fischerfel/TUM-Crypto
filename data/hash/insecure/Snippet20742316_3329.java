try {
            PackageInfo info = getPackageManager().getPackageInfo("com.snada.main.view",
                                        PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("Key: ", Base64.encodeToString(md.digest(), 0));
            }
        } catch (NameNotFoundException e) {
            Log.e("Test", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.e("Test", e.getMessage());
        }
