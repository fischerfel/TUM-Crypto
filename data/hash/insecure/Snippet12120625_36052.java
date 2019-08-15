// for each 0 <= i < k generate a sequence of random numbers
val randomSeeds: Array[Array[Byte]] = ... ; // initialize by random sequences

def hash(i: Int, value: Array[Byte]): Array[Byte] = {
    val dg = java.security.MessageDigest.getInstance("SHA-1");
    // "seed" the digest by a random value based on the index
    dg.update(randomSeeds(i));
    return dg.digest(value);
    // if you need integer hash values, just take 4 bytes
    // of the result and convert them to an int
}
