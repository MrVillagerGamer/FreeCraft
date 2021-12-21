package net.freecraft.util;

@FunctionalInterface
public interface LayerAccessor {
	public int get(int layerDepth);
}
