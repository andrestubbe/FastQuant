package fastquant.scalar;

public final class Int8Vector {
    private final byte[] values;
    private final float scale;
    private final float zeroPoint;

    public Int8Vector(byte[] values, float scale, float zeroPoint) {
        this.values = values;
        this.scale = scale;
        this.zeroPoint = zeroPoint;
    }

    public byte[] getValues() { return values; }
    public float getScale() { return scale; }
    public float getZeroPoint() { return zeroPoint; }
    public int dimension() { return values.length; }

    public float[] dequantize() {
        float[] out = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            out[i] = (values[i] - zeroPoint) * scale;
        }
        return out;
    }
}