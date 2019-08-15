public class BloomFilter {
    int m;
    int k;
    HashSet<String> map = new HashSet<>();
    public BloomFilter(){
        int c = 100;
        float e = 0.1f;
        m = (int) Math.floor(  -1 * c * Math.log(e) / (Math.log(2)*Math.log(2)) ) + 1;
        k = (int) Math.floor( 0.7 * m / (float) c ) + 1;
    }
    private static int[] createHashes(String key, int hashes, int m) {
    byte[] data = key.getBytes();
    int[] result = new int[hashes];

    MessageDigest digestFunction;
    try {
        digestFunction = MessageDigest.getInstance("MD5");
    } catch (Exception e) {
        throw new RuntimeException();
    }

    int k = 0;
    byte salt = 0;
    while (k < hashes) {
        byte[] digest;

        digestFunction.update(salt);
        salt++;
        digest = digestFunction.digest(data);                

        for (int i = 0; i < digest.length / 4 && k < hashes; i++) {
            int h = 0;
            for (int j = (i * 4); j < (i * 4) + 4; j++) {
                h <<= 8;
                h |= ((int) digest[j]) & 0xFF;
            }
            result[k] = Math.abs(h % m);
            k++;
        }
    }
    return result;
    }
    public void add(String s){
        map.add(Arrays.toString(createHashes(s, k, m)));
    }
    public boolean contains(String s){
        return map.contains(Arrays.toString(createHashes(s, k, m)));
    }
}
