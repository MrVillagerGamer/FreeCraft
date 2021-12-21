package net.freecraft.client.entity;

public interface IRenderEntity {
	// Called to dispose mesh
	public void dispose();
	// Called to render mesh, load if necessary
	public void render();
}
