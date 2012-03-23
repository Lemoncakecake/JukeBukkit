package cc.thedudeguy.jukebukkit.materials.items.colorblankdisc;

import cc.thedudeguy.jukebukkit.materials.items.BlankDisc;
import cc.thedudeguy.jukebukkit.materials.items.DiscColor;


public class BlankDiscBrown extends BlankDisc {

	public BlankDiscBrown() {
		super("Blank Brown Obsidyisc");
		setColor(DiscColor.BROWN);
	}
	
	@Override
	public String getTextureFileName() {
		return "blank_disc_brown.png";
	}

}
