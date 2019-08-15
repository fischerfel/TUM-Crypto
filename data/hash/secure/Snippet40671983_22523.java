MessageDigest digester = MessageDigest.getInstance("SHA-256");
digester.update(sentence.getBytes());
int hashValue = new String(digester.digest()).hashCode();
