package net.freecraft.client.gui.container;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import net.freecraft.client.gui.IComponentMouseHandler;
import net.freecraft.client.gui.UIComponent;
import net.freecraft.item.ItemStack;

public class UIContainer extends UIComponent implements IComponentMouseHandler {
	protected List<UISlot> slots = new ArrayList<>();
	protected UISlot movedSlot;
	public UIContainer() {

	}
	@Override
	public void update() {

	}
	@Override
	public void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
		g.setColor(Color.WHITE);
		g.drawLine(x+1, y+1, x+width-2, y+1);
		g.drawLine(x+1, y+1, x+1, y+height-2);
		g.setColor(Color.DARK_GRAY);
		g.drawLine(x+2, y+height-1, x+width-1, y+height-1);
		g.drawLine(x+width-1, y+2, x+width-1, y+height-1);
		for (UISlot slot : slots) {
			slot.render(g);
		}
	}
	@Override
	public void wheelMove(int amt) {

	}
	@Override
	public void mouseClick(int x, int y, int btn) {
		if(movedSlot != null) {
			for(UISlot slot : slots) {
				if(slot.inBounds(x, y) && slot != movedSlot && !movedSlot.getSlot().get().isEmpty()) {
					if(btn == 0 && slot.getSlot().canGive() && movedSlot.getSlot().canTake()) {
						if(slot.getSlot().get().canCombineWith(movedSlot.getSlot().get()) && slot.getSlot().get().getId() == movedSlot.getSlot().get().getId()) {
							int count = movedSlot.getSlot().get().getCount();
							int count2 = slot.getSlot().get().getCount();
							slot.getSlot().set(movedSlot.getSlot().get());
							int addAmt = count;
							if(count2 + addAmt > movedSlot.getSlot().get().getMaxCount()) {
								addAmt = movedSlot.getSlot().get().getMaxCount() - count2;
							}
							slot.getSlot().get().setCount(count2 + addAmt);
							movedSlot.getSlot().get().setCount(movedSlot.getSlot().get().getCount() - addAmt);
						}else {
							ItemStack stk = slot.getSlot().get().copy();
							slot.getSlot().get().set(movedSlot.getSlot().get());
							movedSlot.getSlot().get().set(stk);
						}
						movedSlot.getSlot().onTake();
						slot.getSlot().onGive();
					}else if(btn == 2 && slot.getSlot().get().canCombineWith(movedSlot.getSlot().get())
							&& movedSlot.getSlot().canTake() && slot.getSlot().canGive()) {
						int count = movedSlot.getSlot().get().getCount();
						int count2 = slot.getSlot().get().getCount();
						slot.getSlot().get().set(movedSlot.getSlot().get());
						int addAmt = (count-count/2);
						if(count2 + addAmt > movedSlot.getSlot().get().getMaxCount()) {
							addAmt = movedSlot.getSlot().get().getMaxCount() - count2;
						}
						slot.getSlot().get().setCount(count2 + addAmt);
						movedSlot.getSlot().get().setCount(movedSlot.getSlot().get().getCount() - addAmt);
						movedSlot.getSlot().onTake();
						slot.getSlot().onGive();
					}
				}
			}
			movedSlot = null;
		}
		for(UISlot slot : slots) {
			slot.setSelected(false);
			if(slot.inBounds(x, y)) {
				slot.setSelected(true);
				movedSlot = slot;
			}
		}
	}
}
