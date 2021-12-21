package net.freecraft.util.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class CompressedOutputStream extends DeflaterOutputStream{
	public CompressedOutputStream(OutputStream out) {
		super(out, new Deflater(Deflater.DEFAULT_COMPRESSION, true));
	}
	@Override
	public void flush() throws IOException {
		finish();
		super.flush();
	}
}
