MadZip is a lightweight, high-performance file compression and decompression utility. It uses the efficient Huffman coding algorithm to compress files, significantly reducing file sizes while maintaining file integrity. MadZip is easy to integrate into any project and features a simple command-line interface for quick file operations.

# Key Features
Efficient compression using the Huffman coding algorithm
High-speed decompression with low memory footprint
Supports a wide range of file types
Simple command-line interface
Java-based implementation for platform independence
Easy integration into existing projects

# Usage

Command-line Interface

First, compile the MadZip source files:
```
javac madzip/*.java
```

To compress a file:
```
java madzip.MadZip compress <input_file> <output_file>
```

To decompress a file:
```
java madzip.MadZip decompress <input_file> <output_file>
```

## API Integration
You can easily integrate MadZip into your Java projects as a library.

To compress a file:
```
import madzip.MadZip;

MadZip madZip = new MadZip();
madZip.compress("input.txt", "output.madzip");
```

To decompress a file:
```
import madzip.MadZip;

MadZip madZip = new MadZip();
madZip.decompress("input.madzip", "output.txt");
```
