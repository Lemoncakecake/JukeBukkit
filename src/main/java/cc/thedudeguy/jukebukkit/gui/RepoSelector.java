package cc.thedudeguy.jukebukkit.gui;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericListWidget;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.player.SpoutPlayer;

import cc.thedudeguy.jukebukkit.JukeBukkit;
import cc.thedudeguy.jukebukkit.gui.widget.CloseButton;
import cc.thedudeguy.jukebukkit.gui.widget.RepoBurnButton;
import cc.thedudeguy.jukebukkit.gui.widget.RepoMusicList;
import cc.thedudeguy.jukebukkit.gui.widget.ServerListButton;

/**
 * This class is based off of WindWakers class in TextureMe.
 *
 */
public class RepoSelector extends GenericPopup {
	
	public RepoSelector(Player player, Block block) {
		
		if (
				!JukeBukkit.instance.getConfig().getBoolean("enableWebServer") || 
				!JukeBukkit.instance.HTTPserver.isRunning()
				) {
			if (((SpoutPlayer)player).getMainScreen().getActivePopup() != null) {
				((SpoutPlayer)player).getMainScreen().getActivePopup().close();
			}
			((SpoutPlayer)player).getMainScreen().attachPopupScreen(new CustomURLSelecter(((SpoutPlayer)player), block));
			return;
		}
			
		
		// Label
		GenericLabel label = new GenericLabel("Music Repository");
		label.setX(175).setY(25);
		label.setPriority(RenderPriority.Lowest);
		label.setWidth(-1).setHeight(-1);
		
		// Border
		GenericTexture border = new GenericTexture("borderblue.png");
		border.setX(65).setY(20);
		border.setPriority(RenderPriority.High);
		border.setWidth(300).setHeight(200);

		// Background gradient
		GenericGradient gradient = new GenericGradient();
		gradient.setTopColor(new Color(0.25F, 0.25F, 0.25F, 1.0F));
		gradient.setBottomColor(new Color(0.35F, 0.35F, 0.35F, 1.0F));
		gradient.setWidth(300).setHeight(200);
		gradient.setX(65).setY(20);
		gradient.setPriority(RenderPriority.Highest);
		
		// Texture list
		GenericListWidget list = new RepoMusicList();
		list.setX(90).setY(50);
		list.setWidth(250).setHeight(125);
		list.setPriority(RenderPriority.Lowest);
		
		// Close button
		CloseButton close = new CloseButton();
		close.setX(155).setY(195);
		close.setWidth(60).setHeight(20);
		close.setPriority(RenderPriority.Lowest);
		
		// Select button
		RepoBurnButton burnButton = new RepoBurnButton(list, block);
		burnButton.setX(95).setY(195);
		burnButton.setWidth(60).setHeight(20);
		burnButton.setPriority(RenderPriority.Lowest);
		
		
		// switch to custom URL
		ServerListButton urlbutton = new ServerListButton(block);
		urlbutton.setX(215).setY(195);
		urlbutton.setWidth(60).setHeight(20);
		urlbutton.setPriority(RenderPriority.Lowest);
		
		this.setTransparent(true);
		this.attachWidgets(JukeBukkit.instance, border, gradient, burnButton, close, label, list);
		
		if (JukeBukkit.instance.getConfig().getBoolean("allowExternalURLs")) {
			this.attachWidget(JukeBukkit.instance, urlbutton);
		}
		
	}
	
}
