# FastQuant Reference & API Specification

## 1. Core Vocabulary

*   **Quantization**: Mapping continuous or high-precision numbers (FP32) into discrete, low-bit integer representations (INT8, INT4, 1-Bit).
*   **Uniform Scalar Quantization**: Linear scaling of FP32 values into $[-128, 127]$ with dynamic scale and zero-point parameters.
*   **1-Bit Binary Quantization**: Sign-bit mapping into 64-bit words ($>0.0 \rightarrow 1, \le 0.0 \rightarrow 0$) for ultra-dense 96.8% memory compression.
*   **Hamming Distance**: The number of differing bit positions between two binary vectors, computed via bitwise `XOR` and hardware `POPCNT`.
*   **Two-Stage Search**: Fast 1-bit binary filtering over 1,000,000+ candidates followed by high-precision INT8/FP32 refinement.

## 2. API Quick Reference

### `FastQuant` (Main Facade)
*   `quantizeInt8(float[] fp32)`: Compresses FP32 array into `Int8Vector` (75% RAM reduction).
*   `quantizeBinary(float[] fp32)`: Compresses FP32 array into bit-packed `BinaryVector` (96.8% RAM reduction).
*   `dotProduct(Int8Vector a, Int8Vector b)`: Computes scalar dot product between two quantized INT8 vectors.

### `Int8Vector`
*   `getValues()`: Returns raw `byte[]` quantized payload.
*   `getScale()` / `getZeroPoint()`: Returns dequantization parameters.
*   `dequantize()`: Reconstructs FP32 approximation.

### `BinaryVector`
*   `getBits()`: Returns raw bit-packed `long[]` words.
*   `hammingDistance(BinaryVector other)`: Computes bitwise difference count using hardware POPCNT.
*   `similarity(BinaryVector other)`: Computes normalized similarity score in $[0.0, 1.0]$.

---
**Part of the FastJava Ecosystem** — *Making the JVM faster. Small package. Maximum speed. Zero bloat. 🚀📋*