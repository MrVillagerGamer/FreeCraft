package net.freecraft.util.stream;

import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class CompressedInputStream extends InflaterInputStream{
	public CompressedInputStream(InputStream in) {
		super(in, new Inflater(true));
	}

}
