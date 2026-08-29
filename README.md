# FastQuant 0.1.0 [ALPHA] — Hardware-Accelerated Low-Bit Quantization Engine for Java

[![Status](https://img.shields.io/badge/status-0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastQuant/releases/tag/0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Cross--Platform-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe/FastQuant)

---

**⚡ Hardware-accelerated low-bit quantization (INT8, INT4, 1-Bit Binary) and SIMD vector distance substrate for the FastJava ecosystem.**

**FastQuant** is a high-throughput numerical quantization engine designed to compress AI model weights, high-dimensional vector embeddings (**[FastAIVectorDB](https://github.com/andrestubbe/FastAIVectorDB)**), and audio DSP buffers with 75% to 96.8% memory reductions while preserving floating-point cosine similarity precision at tens of millions of operations per second.

---

## Quick Start

```java
import fastquant.FastQuant;
import fastquant.binary.BinaryVector;
import fastquant.scalar.Int8Vector;

public class Example {
    public static void main(String[] args) {
        float[] embedding = new float[1536]; // e.g. OpenAI / BGE embedding

        // 1. INT8 Scalar Quantization (75% RAM reduction)
        Int8Vector int8 = FastQuant.quantizeInt8(embedding);
        System.out.printf("INT8 Size: %d Bytes (Original: %d Bytes)%n",
            int8.getValues().length, embedding.length * 4);

        // 2. 1-Bit Binary Quantization (96.8% RAM reduction)
        BinaryVector binary = FastQuant.quantizeBinary(embedding);
        System.out.printf("1-Bit Size: %d Bytes%n", binary.getBits().length * 8);

        // 3. Fast Bitwise Hamming Distance Similarity
        float similarity = binary.similarity(binary);
        System.out.printf("Similarity: %.4f%n", similarity);
    }
}
```

---

## Table of Contents

- [Why FastQuant?](#why-fastquant)
- [Quick Start](#quick-start)
- [Features](#features)
- [Performance Benchmarks](#performance-benchmarks)
- [API Quick Reference](#api-quick-reference)
- [Technical Examples & Hero Demos](#technical-examples--hero-demos)
- [Installation](#installation)
- [Documentation](#documentation)
- [Platform Support](#platform-support)
- [License](#license)
- [Related Projects](#related-projects)

---

## Why FastQuant?

Standard JVM vector stores and numeric routines suffer from memory bloat when scaling to millions of embeddings:

- **Heavy FP32 Memory Footprint**: Storing 1,000,000 vectors of 1,536 dimensions in raw float format consumes **6.14 GB** of JVM heap memory.
- **Garbage Collection Pressure**: Allocating millions of float arrays triggers frequent, unpredictable GC stalls.
- **Cache Bottlenecks**: Large vector payloads cause L1/L2/L3 cache misses during linear similarity scans.

**FastQuant** solves this by providing native hardware quantization:

- **75% to 96.8% RAM Reduction**: Shrinks 1,536-dim vectors from **6,144 Bytes** down to **1,536 Bytes (INT8)** or **192 Bytes (1-Bit Binary)**.
- **Native Bitwise Acceleration**: Computes vector similarity via bitwise `XOR` and hardware `POPCNT` at over **77,000,000 ops/sec**.
- **Two-Stage Search Optimization**: Enables instant 1-bit filtering over millions of candidates followed by high-precision INT8/FP32 refinement.

---

## Features

- **🔢 INT8 Uniform Scalar Quantization**: Dynamic min/max scaling with symmetric and asymmetric zero-point mapping.
- **⚡ 1-Bit Binary Sign Quantization**: 64-bit word bit-packing for ultra-dense similarity caching.
- **🎯 SIMD Distance Metrics**: Fast dot-product, Euclidean distance, and hardware-accelerated Hamming distance.
- **📦 Zero-Allocation Structs**: Pre-sized value carriers preventing GC churn during high-throughput vector ingestion.
- **📊 FastANSI 120-Column HUD**: Terminal telemetry displaying memory densities, compression ratios, and microsecond latencies.

---

## Performance Benchmarks

FastQuant is rigorously profiled using **JMH** to guarantee zero overhead.

| Metric / Operation Type (1536-dim) | Score (ops/ms) | Ops per Second |
|---|---|---|
| **Binary Hamming Distance** | **~77,906 ops/ms** | **> 77.9 Million** |
| **INT8 Dot Product Similarity** | **~1,495 ops/ms** | **> 1.49 Million** |
| **1-Bit Binary Quantization** | **~599 ops/ms** | **> 599,000** |
| **INT8 Scalar Quantization** | **~174 ops/ms** | **> 174,000** |

*Measured on Windows 11 x64, Intel Core i5 (Surface Pro 8), JDK 21.0.12.1. Vector size: 1,536 dimensions (standard embedding width).*

---

## API Quick Reference

| Method | Description |
|---|---|
| `FastQuant.quantizeInt8(fp32)` | Compresses an FP32 vector into a 75% smaller INT8 representation. |
| `FastQuant.quantizeBinary(fp32)` | Compresses an FP32 vector into a 96.8% smaller bit-packed binary vector. |
| `FastQuant.dotProduct(int8A, int8B)` | Computes scalar similarity between two INT8 vectors. |
| `binaryVector.hammingDistance(other)` | Computes bitwise difference count using hardware POPCNT. |
| `binaryVector.similarity(other)` | Computes normalized binary similarity score in [0.0, 1.0]. |

---

## Technical Examples & Hero Demos

| Case | Java Example | Launcher | Description |
|---|---|---|---|
| **Interactive 120-Column HUD Demo** | [Demo.java](src/main/java/fastquant/Demo.java) | `run-demo.bat` | Terminal demonstration of FP32 vs INT8 vs 1-Bit memory footprints and SIMD scan speeds. |
| **JMH Microbenchmark Suite** | [FastQuantBenchmark.java](examples/Benchmark/src/main/java/fastquant/benchmark/FastQuantBenchmark.java) | `run-benchmark.bat` | Formal OpenJDK JMH throughput measurements across 1536-dim vector quantization and distance kernels. |

---

## Installation

### Option 1: Maven (Recommended)

Add the JitPack repository and the dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastQuant</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

### Option 2: Gradle (via JitPack)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.andrestubbe:FastQuant:0.1.0'
}
```

### Option 3: Direct Download (No Build Tool)
Download the latest JARs directly to add them to your classpath:

1. 📦 **[FastQuant-0.1.0.jar](https://github.com/andrestubbe/FastQuant/releases/download/0.1.0/FastQuant-0.1.0.jar)** (The Core Quantization Engine)
2. ⚙️ **[fastcore-0.1.0.jar](https://github.com/andrestubbe/FastCore/releases/download/0.1.0/fastcore-0.1.0.jar)** (The Mandatory Runtime Substrate)

---

## Documentation

* **[REFERENCE.md](docs/REFERENCE.md)**: Full API descriptions, methods, and mathematical quantization specs.
* **[PHILOSOPHY.md](docs/PHILOSOPHY.md)**: The architectural rationale for low-bit quantization on the JVM.
* **[ROADMAP.md](docs/ROADMAP.md)**: Future milestones, AVX-512 VNNI kernels, and Product Quantization (PQ).
* **[CHANGELOG.md](docs/CHANGELOG.md)**: Release history and version migration details.

---

## Platform Support

| Platform | Status |
|---|---|
| Windows 10/11 (x64) | ✅ Fully Supported |
| Linux (x64 / AArch64) | ✅ Fully Supported |
| macOS (Apple Silicon / Intel) | ✅ Fully Supported |

---

## License

MIT License — See [LICENSE](LICENSE) for details.

---

## Related Projects

Combine FastQuant with other FastJava compute and AI accelerators:

* [**FastAIVectorDB**](https://github.com/andrestubbe/FastAIVectorDB) — Ultrafast embedded vector database for Java.
* [**FastMath**](https://github.com/andrestubbe/FastMath) — Native SIMD mathematics, vectors, and matrix algebra.
* [**FastGPU**](https://github.com/andrestubbe/FastGPU) — Vulkan/CUDA GPU compute runtime.
* [**FastAIModel**](https://github.com/andrestubbe/FastAIModel) — In-process local LLM and ONNX embedding engine.

---

**Part of the FastJava Ecosystem** — *Making the JVM faster.*