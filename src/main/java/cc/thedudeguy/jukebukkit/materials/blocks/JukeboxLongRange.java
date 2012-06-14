package cc.thedudeguy.jukebukkit.materials.blocks;

import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;

import cc.thedudeguy.jukebukkit.JukeBukkit;
import cc.thedudeguy.jukebukkit.materials.Blocks;

public class JukeboxLongRange extends JukeboxBlock {
	
	public JukeboxLongRange()
	{
		super("Long Range Jukebox");
	}
	
	@Override
	public GenericCubeBlockDesign getCustomBlockDesign() {
		
		return new GenericCubeBlockDesign(
				JukeBukkit.instance, 
				Blocks.blocksTexture, 
				new int[] { 0, 7, 7, 7, 7, 1 }
			);
	}
	
	@Override
	public int getRange()
	{
		return 60;
	}
	
	@Override
	public String getPermission() {
		return "jukebukkit.player.long";
	}
	
	@Override
	public boolean canRedstoneActivate() {
		return true;
	}
}
