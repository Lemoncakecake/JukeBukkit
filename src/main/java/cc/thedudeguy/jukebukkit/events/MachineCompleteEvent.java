package cc.thedudeguy.jukebukkit.events;

import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.block.SpoutBlock;


public class MachineCompleteEvent extends MachineEvent {

	public MachineCompleteEvent(SpoutBlock block, ItemStack primaryItem, ItemStack addItem, String label) {
		super(block, primaryItem, addItem, label);
		// TODO Auto-generated constructor stub
	}
	
}
