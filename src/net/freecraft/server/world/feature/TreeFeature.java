package net.freecraft.server.world.feature;

import net.freecraft.block.Block;
import net.freecraft.block.Material;
import net.freecraft.server.FreeCraftServer;
import net.freecraft.util.BlockPos;
import net.freecraft.util.Registries;

public class TreeFeature extends Feature {
	private TreeShape shape;
	private int woodId, leavesId;
	private int[][][] blocks;
	public TreeFeature(TreeShape shape, int woodId, int leavesId) {
		this.shape = shape;
		this.woodId = woodId;
		this.leavesId = leavesId;
		blocks = genBlocks(shape, woodId, leavesId);
		tries = 1024;
	}
	private int[][][] genBlocks(TreeShape shape, int woodId, int leavesId) {
		int[][][] blocks = new int[5][11][5];
		for(int x = 0; x < 5; x++) {
			for(int y = 0; y < 11; y++) {
				for(int z = 0; z < 5; z++) {
					blocks[x][y][z] = -1;
				}
			}
		}
		if(shape == TreeShape.DECIDUOUS) {
			int h = 2;
			for(int i = -2; i <= 2; i++) {
				for(int k = -2; k <= 2; k++) {
					for(int j = h; j < h+1; j++) {
						blocks[i+2][j][k+2] = leavesId;
					}
				}
			}
			for(int i = -2; i <= 2; i++) {
				for(int k = -1; k <= 1; k++) {
					for(int j = h+1; j < h+2; j++) {
						blocks[i+2][j][k+2] = leavesId;
						blocks[k+2][j][i+2] = leavesId;
					}
				}
			}
			for(int i = -1; i <= 1; i++) {
				for(int k = -1; k <= 1; k++) {
					blocks[i+2][h+2][k+2] = leavesId;
				}
				blocks[i+2][h+3][2] = leavesId;
				blocks[2][h+3][i+2] = leavesId;
			}
			for(int j = 0; j < h+2; j++) {
				blocks[2][j][2] = woodId;
			}
		}else if(shape == TreeShape.CONIFEROUS) {
			int h = 2;
			for(int i = -2; i <= 2; i++) {
				for(int k = -2; k <= 2; k++) {
					for(int j = h; j < h+2; j++) {
						blocks[i+2][j][k+2] = leavesId;
					}
				}
			}
			for(int i = -2; i <= 2; i++) {
				for(int k = -1; k <= 1; k++) {
					for(int j = h+2; j < h+4; j++) {
						blocks[i+2][j][k+2] = leavesId;
						blocks[k+2][j][i+2] = leavesId;
					}
				}
			}
			for(int i = -1; i <= 1; i++) {
				for(int k = -1; k <= 1; k++) {
					blocks[i+2][h+4][k+2] = leavesId;
					blocks[i+2][h+5][k+2] = leavesId;
				}
				blocks[i+2][h+6][2] = leavesId;
				blocks[2][h+6][i+2] = leavesId;
				blocks[i+2][h+7][2] = leavesId;
				blocks[2][h+7][i+2] = leavesId;
			}
			for(int j = 0; j < h+2; j++) {
				blocks[2][j][2] = woodId;
			}
		}
		return blocks;
	}
	public int[][][] getBlocks() {
		return blocks;
	}
	public TreeShape getShape() {
		return shape;
	}
	public int getLeavesId() {
		return leavesId;
	}
	public int getWoodId() {
		return woodId;
	}
	@Override
	public boolean canGenerateAt(BlockPos pos) {
		Block b1 = Registries.BLOCKS.get(FreeCraftServer.get().getWorld().getBlock(pos));
		Block b2 = Registries.BLOCKS.get(FreeCraftServer.get().getWorld().getBlock(new BlockPos(pos.x, pos.y-1, pos.z)));
		if(b2.getMaterial() == Material.GROUND && b1.getMaterial() == Material.AIR) {
			return true;
		}
		return false;
	}
	@Override
	public int getBlockFast(BlockPos localPos) {
		return blocks[localPos.x+2][localPos.y][localPos.z+2];
	}
	@Override
	public int getBlock(BlockPos localPos) {
		if(localPos.x >= -2 && localPos.x <= 2) {
			if(localPos.z >= -2 && localPos.z <= 2) {
				if(localPos.y >= 0 && localPos.y < 11) {
					return blocks[localPos.x+2][localPos.y][localPos.z+2];
				}
			}
		}
		return -1;
	}
	@Override
	public BlockPos getSize() {
		return new BlockPos(5, 11, 5);
	}
	@Override
	public BlockPos getStart() {
		return new BlockPos(-2, 0, -2);
	}
}
