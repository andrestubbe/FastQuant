package fastquant;

import fastquant.binary.BinaryVector;
import fastquant.scalar.Int8Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FastQuantTest {

    @Test
    public void testInt8QuantizationAccuracy() {
        float[] original = {0.1f, -0.5f, 0.9f, 0.0f, -0.9f};
        Int8Vector int8 = FastQuant.quantizeInt8(original);

        assertEquals(5, int8.dimension());
        float[] restored = int8.dequantize();

        for (int i = 0; i < original.length; i++) {
            assertEquals(original[i], restored[i], 0.05f);
        }
    }

    @Test
    public void testBinaryQuantizationSimilarity() {
        float[] vecA = {0.5f, -0.2f, 0.9f, -0.7f, 0.1f};
        float[] vecB = {0.4f, -0.1f, 0.8f, -0.6f, 0.2f};

        BinaryVector binA = FastQuant.quantizeBinary(vecA);
        BinaryVector binB = FastQuant.quantizeBinary(vecB);

        assertEquals(0, binA.hammingDistance(binB));
        assertEquals(1.0f, binA.similarity(binB), 0.001f);
    }
}