package net.freecraft.client.util;

import net.freecraft.item.Item;
import net.freecraft.util.Registries;

public class ItemIcons {
	public static void init() {
		for(Item item : Registries.ITEMS.getAll()) {
			ClientRegistries.ITEM_ICONS.bind(item, new ItemIcon(item));
		}
	}
}
