package net.freecraft.client.util;

import net.freecraft.client.entity.RenderEntityType;
import net.freecraft.client.recipe.CraftingRecipe;
import net.freecraft.client.util.input.KeyBinding;
import net.freecraft.client.util.input.MouseBinding;
import net.freecraft.util.Registry;

public class ClientRegistries {
	public static final Registry<KeyBinding> KEY_BINDINGS = new Registry<>();
	public static final Registry<MouseBinding> MOUSE_BINDINGS = new Registry<>();
	public static final Registry<RenderEntityType> RENDER_ENTITY_TYPES = new Registry<>();
	public static final Registry<Atlas> ATLASES = new Registry<>();
	public static final Registry<ItemIcon> ITEM_ICONS = new Registry<>();
	public static final Registry<CraftingRecipe> CRAFTING_RECIPES = new Registry<>();
}
