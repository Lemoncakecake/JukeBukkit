package cc.thedudeguy.jukebukkit.jukebox.custom;

import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.block.design.Texture;

import cc.thedudeguy.jukebukkit.JukeBukkit;
import cc.thedudeguy.jukebukkit.materials.blocks.JukeboxBlock;

public class JukeboxLowRange extends JukeboxBlock {
	public JukeboxLowRange(JukeBukkit plugin)
	{
		this(plugin, plugin.getCustomsManager().getCustomBlockTexture());
	}
	public JukeboxLowRange(JukeBukkit plugin, Texture texture)
	{
		super(
			plugin, 
			"Low Range Jukebox",
			new GenericCubeBlockDesign(
				plugin, 
				texture, 
				new int[] { 0, 5, 5, 5, 5, 1 }
			),
			"jukebukkit.player.low"
		);
		//ints are { bottom, north, ?, ?, ?, top }
	}

	@Override
	public int getRange()
	{
		return 11;
	}
}
