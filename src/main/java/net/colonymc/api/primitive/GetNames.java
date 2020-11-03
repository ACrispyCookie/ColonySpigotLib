package net.colonymc.api.primitive;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.colonymc.api.Main;

public class GetNames {
	
	static final FileConfiguration namesConfig = Main.getInstance().getNamesConfig();
	
	@SuppressWarnings("deprecation")
	public static String itemName(ItemStack item, Boolean showData) {
		if (item == null) {
			return namesConfig.getString("items.UNKNOWN");
		}
		String typeId = "";
		String data = "";
		String name = null;
		StringBuilder prefix = new StringBuilder();
		StringBuilder suffix = new StringBuilder();
		typeId = Integer.toString(item.getTypeId());
		if (item.getType().getMaxDurability() > 0) {
			data = "0";
		} else if (item.getDurability() > 0) {
			data = Short.toString(item.getDurability());
		} else if (item.getData().getData() > 0) {
			data = Byte.toString(item.getData().getData());
		} else {
			data = "0";
		}
		if (typeId.equals("373")) {
			short potionData = Short.parseShort(data);
			if (potionData == 0) {
				name = namesConfig.getString("potion-parts.absolute-zero");
			} else {
				for (short bitPos = 14; bitPos > 5; bitPos--) {
					
					short bitPow = (short) Math.pow(2, bitPos);
					if (potionData >= bitPow) {
						potionData -= bitPow;
						String tmpPrefix = namesConfig.getString("potion-parts.prefix-bit-" + bitPos);
						if (tmpPrefix != null) prefix.append(tmpPrefix);
						String tmpSuffix = namesConfig.getString("potion-parts.suffix-bit-" + bitPos);
						if (tmpSuffix != null) suffix.append(tmpSuffix);
					}
				}
				data = Short.toString(potionData);
			}
		}
		if (typeId.equals("397") && data.equals("3")) {
			String headOwner = getHeadOwner((ItemStack) item);
			if (headOwner != null) {
				name = namesConfig.getString("special.players-head").replace("%p", headOwner);
			}
		}
		if (name == null) {
			name = namesConfig.getString("items." + typeId + ";" + data);
		}
		if (name == null) {
			name = namesConfig.getString("items." + typeId + ";0");
		}
		if (name == null) {
			name = namesConfig.getString("items.UNKNOWN");
		}
		name = prefix + name + suffix;
		if (showData) {
			name = "(" + typeId + ":" + data + ") " + name;
		}
		name = name.replace("%d", data);
		return name;
	}
	
	@SuppressWarnings("deprecation")
	public static String enchantmentName(Enchantment enchantment, Boolean showData) {
		if (enchantment == null) {
			return namesConfig.getString("enchantments.UNKNOWN");
		}
		String data = Integer.toString(enchantment.getId());
		String name = namesConfig.getString("enchantments." + data);
		if (name == null) {
			name = namesConfig.getString("enchantments.UNKNOWN");
		}
		if (showData) {
			name = "(" + enchantment.getId() + ") " + name;
		}
		return name;
	}
	
	private static String getHeadOwner(ItemStack item) {
		if (item == null) return null;
		ItemMeta itemMeta = item.getItemMeta();
		if (itemMeta == null) return null;
		if (itemMeta instanceof SkullMeta) {
			return ((SkullMeta)itemMeta).getOwner();
		}
		return null;
	}

}
