public AlgorithmParameterSpec getIV() {
    byte[] iv = { 1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6 };
    IvParameterSpec ivParameterSpec;
    ivParameterSpec = new IvParameterSpec(iv);

    return ivParameterSpec;
}
