package fastquant.binary;

public final class BinaryVector {
    private final long[] bits;
    private final int dimension;

    public BinaryVector(long[] bits, int dimension) {
        this.bits = bits;
        this.dimension = dimension;
    }

    public long[] getBits() { return bits; }
    public int getDimension() { return dimension; }

    /**
     * Fast Bitwise Hamming Distance calculation.
     */
    public int hammingDistance(BinaryVector other) {
        int dist = 0;
        int words = Math.min(bits.length, other.bits.length);
        for (int i = 0; i < words; i++) {
            dist += Long.bitCount(bits[i] ^ other.bits[i]);
        }
        return dist;
    }

    /**
     * Normalized Hamming similarity score in [0.0, 1.0].
     */
    public float similarity(BinaryVector other) {
        int dist = hammingDistance(other);
        return 1.0f - ((float) dist / dimension);
    }
}