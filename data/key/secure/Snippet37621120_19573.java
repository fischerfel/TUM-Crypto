final File file = new File(path, fileName);
Key key = new SecretKeySpec(secret, "AES");
final Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, key);
try (FileInputStream fis = new FileInputStream(file); CipherInputStream cis = new CipherInputStream(fis, cipher); ZipInputStream zis = new ZipInputStream(new BufferedInputStream(cis))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {

                    List<String> lines;
                    try ( ByteArrayOutputStream output = new ByteArrayOutputStream(2048)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            output.write(buffer, 0, len);
                        }
                        try (ByteArrayInputStream bais = new ByteArrayInputStream(output.toByteArray()); Reader reader = new InputStreamReader(bais)) {
                            lines = readFile(reader);
                        }
                    }
                    //Do something with lines of the file...

                }

            }
