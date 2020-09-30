package net.colonymc.api.book.survey;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;

public class SurveyBook {
	
	UUID uuid;
	ArrayList<SurveyLine> lines = new ArrayList<SurveyLine>();
	static ArrayList<SurveyBook> books = new ArrayList<SurveyBook>();
	
	protected SurveyBook(ArrayList<SurveyLine> lines) {
		this.uuid = UUID.randomUUID();
		while(uuidExists()) {
			this.uuid = UUID.randomUUID();
		}
		this.lines = lines;
		books.add(this);
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public ArrayList<SurveyLine> getLines() {
		return lines;
	}
	
	@SuppressWarnings("unchecked")
	protected void open(Player p) {
		try {
			ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
			BookMeta meta = (BookMeta) book.getItemMeta();
			List<IChatBaseComponent> pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(meta);
			TextComponent text = new TextComponent("");
			for(SurveyLine line : lines) {
				text.addExtra(line.getText());
			}
			IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(new BaseComponent[]{text}));
			pages.add(page);
			book.setItemMeta(meta);
			openBook(book, p);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	private void openBook(final ItemStack book, final Player player) {
		final int slot = player.getInventory().getHeldItemSlot();
        final ItemStack old = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, book);
        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, (byte)0);
        buf.writerIndex(1);
        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
        CraftPlayer craftP = (CraftPlayer)player;
        craftP.getHandle().playerConnection.sendPacket(packet);
        player.getInventory().setItem(slot, old);
	}

	private boolean uuidExists() {
		for(SurveyBook a : books) {
			if(a.getUuid().equals(uuid)) {
				return true;
			}
		}
		return false;
	}
	
	public static SurveyBook getByUuid(UUID uuid) {
		for(SurveyBook b : books) {
			if(b.getUuid().equals(uuid)) {
				return b;
			}
		}
		return null;
	}
}
