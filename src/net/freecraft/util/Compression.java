package net.freecraft.util;

import java.util.Arrays;

import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

public class Compression {
	private static LZ4Compressor compressor;
	private static LZ4FastDecompressor decompressor;
	private static void init() {
		LZ4Factory factory = LZ4Factory.fastestInstance();
		compressor = factory.fastCompressor();
		decompressor = factory.fastDecompressor();
	}
	public static CompressedData compress(byte[] src) {
		if(compressor == null) init();
		int maxlen = compressor.maxCompressedLength(src.length);
		byte[] dst = new byte[maxlen];
		int complen = compressor.compress(src, 0, src.length, dst, 0, maxlen);
		CompressedData res = new CompressedData();
		res.data = Arrays.copyOf(dst, complen);
		res.decompLength = src.length;
		return res;
	}
	public static byte[] decompress(CompressedData src) {
		if(compressor == null) init();
		byte[] dst = new byte[src.decompLength];
		int complen2 = decompressor.decompress(src.data, 0, dst, 0, src.decompLength);
		return dst;
	}
}
