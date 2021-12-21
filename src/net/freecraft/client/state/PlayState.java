package net.freecraft.client.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.freecraft.FreeCraft;
import net.freecraft.block.Block;
import net.freecraft.block.Blocks;
import net.freecraft.client.FreeCraftClient;
import net.freecraft.client.gui.UIComponent;
import net.freecraft.client.gui.UICrosshair;
import net.freecraft.client.gui.UIHotbar;
import net.freecraft.client.world.ClientWorld;
import net.freecraft.data.Data;
import net.freecraft.entity.Entity;
import net.freecraft.item.ItemStack;
import net.freecraft.item.inventory.Inventory;
import net.freecraft.util.BlockPos;
import net.freecraft.util.Registries;
import net.freecraft.util.Vec3D;
import net.freecraft.world.World;

public class PlayState extends InGameState {
	private UIHotbar hotbar;
	private UICrosshair crosshair;
	private boolean shifting;
	@Override
	public void start() {
		FreeCraftClient.get().getRenderer();
		hotbar = new UIHotbar(9);
		crosshair = new UICrosshair();
	}
	@Override
	public void render(BufferedImage target) {
		// UI rendering
		BufferedImage uitarget = new BufferedImage(FreeCraftClient.get().getUIWidth(), FreeCraftClient.get().getUIHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = uitarget.createGraphics();
		g.setColor(new Color(0, true));
		g.fillRect(0, 0, FreeCraftClient.get().getUIWidth(), FreeCraftClient.get().getUIHeight());
		hotbar.render(g);
		crosshair.render(g);
		g.dispose();
		// Main rendering
		g = target.createGraphics();
		g.clearRect(0, 0, FreeCraftClient.get().getGLWidth(), FreeCraftClient.get().getGLHeight());
		ClientWorld world = (ClientWorld)FreeCraftClient.get().getWorld();
		world.render();
		FreeCraftClient.get().getRenderer().swapBuffers(g);
		g.drawImage(uitarget, 0, 0, FreeCraftClient.get().getGLWidth(), FreeCraftClient.get().getGLHeight(), null);
		g.dispose();
		uitarget.flush();
	}
	@Override
	public void escape() {
		super.escape();
		State.set(States.PAUSE);
	}
	@Override
	public boolean isCursorVisible() {
		return false;
	}
	public void raycast(boolean placeInsteadOfBreak) {
		World world = FreeCraftClient.get().getWorld();
		Entity viewEntity = FreeCraftClient.get().getRenderer().getViewEntity();
		if(viewEntity != null) {
			BlockPos bpos = null;
			Block b = null;
			Vec3D pos = viewEntity.getPos();
			Vec3D dir = viewEntity.getForward().mul(0.1f);
			double dist = 0;
			while(dist < 8) {
				dist += dir.length();
				pos = pos.add(dir);
				if(!Registries.BLOCKS.get(world.getBlock(pos.toBlockPos())).isPassable()) {
					if(placeInsteadOfBreak) {
						pos = pos.add(dir.negate());
					}
					bpos = pos.toBlockPos();
					b = Registries.BLOCKS.get(world.getBlock(bpos));
					break;
				}
			}
			if(bpos != null && b != null) {
				Inventory inv = viewEntity.getData().getInventory();
				if(inv != null) {
					if(placeInsteadOfBreak) {
						pos = pos.add(dir);
						BlockPos bpos2 = pos.toBlockPos();
						if(shifting || !Registries.BLOCKS.get(FreeCraft.get().getWorld().getBlock(bpos2)).onActivated(bpos2)) {
							ItemStack stack = inv.get(hotbar.getSelectedSlot());
							if(stack.getCount() >= 1 && Registries.ITEMS.get(stack.getId()).isBlock()) {
								world.setBlock(bpos, stack.getId());
								Block b2 = Registries.BLOCKS.get(stack.getId());
								// If item's data exists, use it as tile data
								// Otherwise, generate new tile data
								if(b2.hasTileData(bpos)) {
									Data data = b2.genTileData(bpos, stack.getData());
									world.setTileData(bpos, data);
								}
								stack.setCount(stack.getCount()-1);
								inv.set(hotbar.getSelectedSlot(), stack);
								viewEntity.syncData(null);
							}
						}
					}else {
						if(inv.add(b.getDrop(bpos)) > 0) {
							world.setBlock(bpos, Blocks.AIR.getId());
							viewEntity.syncData(null);
							if(world.getTileData(bpos) != null) {
								world.setTileData(bpos, null);
							}
						}
					}
				}
			}
		}
	}
	public void leftClick() {
		raycast(false);
	}
	public void rightClick() {
		raycast(true);
	}
	public void shift(boolean shifting) {
		this.shifting = shifting;
	}
	@Override
	public UIComponent[] getUIComponents() {
		return new UIComponent[] {hotbar, crosshair};
	}
}
