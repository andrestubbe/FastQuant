package fastquant;

import fastquant.binary.BinaryVector;
import fastquant.scalar.Int8Vector;

public final class FastQuant {

    /**
     * Quantizes an FP32 vector into an INT8 vector with uniform scalar mapping.
     */
    public static Int8Vector quantizeInt8(float[] fp32) {
        if (fp32 == null || fp32.length == 0) return new Int8Vector(new byte[0], 1.0f, 0.0f);

        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;

        for (float v : fp32) {
            if (v < min) min = v;
            if (v > max) max = v;
        }

        float scale = (max - min) / 255.0f;
        if (scale == 0.0f) scale = 1.0f;
        float zeroPoint = -min / scale - 128.0f;

        byte[] quantized = new byte[fp32.length];
        for (int i = 0; i < fp32.length; i++) {
            int q = Math.round(fp32[i] / scale + zeroPoint);
            if (q > 127) q = 127;
            if (q < -128) q = -128;
            quantized[i] = (byte) q;
        }

        return new Int8Vector(quantized, scale, zeroPoint);
    }

    /**
     * Computes fast dot product similarity directly between two INT8 vectors.
     */
    public static float dotProduct(Int8Vector a, Int8Vector b) {
        byte[] vA = a.getValues();
        byte[] vB = b.getValues();
        int len = Math.min(vA.length, vB.length);

        int dotInt = 0;
        for (int i = 0; i < len; i++) {
            dotInt += (int) vA[i] * (int) vB[i];
        }

        return dotInt * a.getScale() * b.getScale();
    }

    /**
     * 1-Bit Binary sign-quantization (positive = 1, negative = 0) with bit-packing into 64-bit words.
     */
    public static BinaryVector quantizeBinary(float[] fp32) {
        if (fp32 == null || fp32.length == 0) return new BinaryVector(new long[0], 0);

        int words = (fp32.length + 63) / 64;
        long[] bits = new long[words];

        for (int i = 0; i < fp32.length; i++) {
            if (fp32[i] > 0.0f) {
                bits[i / 64] |= (1L << (i % 64));
            }
        }

        return new BinaryVector(bits, fp32.length);
    }
}