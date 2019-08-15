try {
                  PackageInfo info = getPackageManager().getPackageInfo("com.sipl.virtualbinoculars", PackageManager.GET_SIGNATURES);
                  for (Signature signature : info.signatures) {
                        MessageDigest md = MessageDigest.getInstance("SHA");
                        md.update(signature.toByteArray());
                        Log.d("KeyHash:",Base64.encodeToString(md.digest(), Base64.DEFAULT));
                        System.out.println("KEY HASH: "+Base64.encodeToString(md.digest(), Base64.DEFAULT));
                  }
                } catch (NameNotFoundException e) {
                    Log.d("KeyHash:",e.getMessage());
                } catch (NoSuchAlgorithmException e) {
                    Log.d("KeyHash:",e.getMessage());
                }
