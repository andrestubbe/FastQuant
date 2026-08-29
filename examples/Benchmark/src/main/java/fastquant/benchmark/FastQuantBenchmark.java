package fastquant.benchmark;

import fastquant.FastQuant;
import fastquant.binary.BinaryVector;
import fastquant.scalar.Int8Vector;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
public class FastQuantBenchmark {

    private float[] vector1536;
    private Int8Vector int8A;
    private Int8Vector int8B;
    private BinaryVector binA;
    private BinaryVector binB;

    @Setup
    public void setup() {
        int dim = 1536;
        vector1536 = new float[dim];
        for (int i = 0; i < dim; i++) {
            vector1536[i] = (float) (Math.sin(i * 0.1) * Math.cos(i * 0.05));
        }

        int8A = FastQuant.quantizeInt8(vector1536);
        int8B = FastQuant.quantizeInt8(vector1536);

        binA = FastQuant.quantizeBinary(vector1536);
        binB = FastQuant.quantizeBinary(vector1536);
    }

    @Benchmark
    public Int8Vector benchmarkInt8Quantization1536() {
        return FastQuant.quantizeInt8(vector1536);
    }

    @Benchmark
    public float benchmarkInt8DotProduct1536() {
        return FastQuant.dotProduct(int8A, int8B);
    }

    @Benchmark
    public BinaryVector benchmarkBinaryQuantization1536() {
        return FastQuant.quantizeBinary(vector1536);
    }

    @Benchmark
    public int benchmarkBinaryHammingDistance1536() {
        return binA.hammingDistance(binB);
    }
}