String hashAlgorithm ="sha-256"
...
md=MessageDigest.getInstance(hashAlgorithm);
byte[] enteredPasswordDigest = md.digest(policy.getPassword().getBytes());
if (!MessageDigest.isEqual(enteredPasswordDigest, realPassword.getBytes())) {
    ...
}
