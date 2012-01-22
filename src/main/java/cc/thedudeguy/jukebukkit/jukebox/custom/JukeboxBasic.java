package cc.thedudeguy.jukebukkit.jukebox.custom;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.player.SpoutPlayer;

import cc.thedudeguy.jukebukkit.CustomsManager;
import cc.thedudeguy.jukebukkit.JukeBukkit;
import cc.thedudeguy.jukebukkit.jukebox.JukeboxBlock;

public class JukeboxBasic extends JukeboxBlock {
	public JukeboxBasic(JukeBukkit plugin)
	{
		this(plugin, plugin.getCustomsManager().getCustomBlockTexture());
	}
	public JukeboxBasic(JukeBukkit plugin, Texture texture)
	{
		super(
			plugin, 
			"Basic Jukebox",
			new GenericCubeBlockDesign(
				plugin, 
				texture, 
				new int[] { 0, 0, 0, 0, 0, 1 }
			),
			"jukebukkit.player.basic"
		);
		//ints are { bottom, north, ?, ?, ?, top }
	}
	
	@Override
	public int getRange()
	{
		return 7;
	}
}
