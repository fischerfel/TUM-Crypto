  try {
                PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                    Log.e("MY KEY HASH:", sign);
                    //textInstructionsOrLink = (TextView)findViewById(R.id.textstring);
                    //textInstructionsOrLink.setText(sign);
                    Toast.makeText(getApplicationContext(), sign, Toast.LENGTH_LONG).show();
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.d("nope", "nope");
            } catch (NoSuchAlgorithmException e) {
            }
