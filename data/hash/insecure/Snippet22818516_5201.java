PackageInfo info = getPackageManager().getPackageInfo(
                                    "com.pakage.example", 
                                    PackageManager.GET_SIGNATURES);
                            for (Signature signature : info.signatures) {
                                MessageDigest md = MessageDigest.getInstance("SHA");
                                md.update(signature.toByteArray());
                                String result = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                                Toast.makeText(MainActivity.this , result + result.toUpperCase(), Toast.LENGTH_LONG).show();
                            }    
