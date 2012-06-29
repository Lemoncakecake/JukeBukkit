package cc.thedudeguy.jukebukkit.gui.widget;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericSlot;
import org.getspout.spoutapi.player.SpoutPlayer;

import cc.thedudeguy.jukebukkit.events.MachineStartEvent;

public class MachineStartButton extends GenericButton{
	
	protected SpoutBlock block;
	
	protected GenericSlot primarySlot;
	protected GenericSlot additiveSlot;
	
	private Player player;
	
	public MachineStartButton(SpoutPlayer player, SpoutBlock block, GenericSlot primary, GenericSlot secondary) {
		this("Start!", player, block, primary, secondary);
		this.player = player;
	}
	
	public MachineStartButton(String label, SpoutPlayer player, SpoutBlock block, GenericSlot primary, GenericSlot secondary) {
		super(label);
		this.block = block;
		this.primarySlot = primary;
		this.additiveSlot = secondary;
		this.player = player;
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		
		if (player.getItemOnCursor() != null && !player.getItemOnCursor().getType().equals(Material.AIR) ) {
			tossItem((SpoutPlayer) player, player.getItemOnCursor());
			player.setItemOnCursor(new ItemStack(Material.AIR));
		}
		
		event.getPlayer().getMainScreen().getActivePopup().close();
		
		MachineStartEvent startEvent = new MachineStartEvent(block, primarySlot.getItem(), additiveSlot.getItem(), null);
		Bukkit.getServer().getPluginManager().callEvent(startEvent);
		
		
	}
	
	private void tossItem(SpoutPlayer player, ItemStack dropItem) {
		Location loc = player.getLocation();
        loc.setY(loc.getY() + 1);
        
        Item item = loc.getWorld().dropItem(loc, dropItem);
        Vector v = loc.getDirection().multiply(0.2);
        v.setY(0.2);
        item.setVelocity(v);
	}
}
