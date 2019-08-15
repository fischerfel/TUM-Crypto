PackageInfo packageInfo = this.getPackageManager()

                    .getPackageInfo(this.getPackageName(),

                            PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {

                byte[] signatureBytes = signature.toByteArray();

                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);

                Log.d("Signature", "Here is the value for SIGNATURE:" + currentSignature);
