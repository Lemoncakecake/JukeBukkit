/**
 * Copyright (C) 2011  Chris Churchwell
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
package cc.thedudeguy.jukebukkit;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class for the Jukebukkit Plugin
 * @author Chris Churchwell
 */
public class JukeBukkit extends JavaPlugin {
	
	public Logger log = Logger.getLogger("Minecraft");
	
	public PluginManager pm;
	//private JukeBukkitCommandExecutor commandExecutor;
	
	private DiscsManager discsManager;
	private JukeBoxManager jukeBoxManager;
	private LabelManager labelManager;
	private CustomsManager customsManager;
	
	//public CustomBlock blockPrototypeDiscPlayer;
	
	//listeners
	private final JukeBukkitPlayerListener playerListener = new JukeBukkitPlayerListener(this);
	private final JukeBukkitInventoryListener inventoryListener = new JukeBukkitInventoryListener(this);
	
	public void onEnable()
	{	
		//load the textures and precaches
		customsManager = new CustomsManager(this);
				
		//load the disc manager so that it can be used throughout
		discsManager = new DiscsManager(this);
		//reinit the discs to fix a reload/restart bug
		discsManager.reInitDiscs();
		//initialize the jukebox manager
		jukeBoxManager = new JukeBoxManager(this);
		//initialize the label manager
		labelManager = new LabelManager(this);
		
		//TODO: Cleanup no longer used item ids for lables, and discs
		
		//load the command executor
		//commandExecutor = new JukeBukkitCommandExecutor(this);
		//getCommand("jukebukkit").setExecutor(commandExecutor);
		
		//load the plugin manager and listeners
		pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.CUSTOM_EVENT, inventoryListener, Event.Priority.Normal, this);
		
		
		log.info("[JukeBukkit] Enabled");
		
	}
	
	public void onDisable()
	{
		log.info("[JukeBukkit] Disabled.");
	}
	
	public DiscsManager getDiscsManager()
	{
		return discsManager;
	}
	public JukeBoxManager getJukeBoxManager()
	{
		return jukeBoxManager;
	}
	public CustomsManager getCustomsManager()
	{
		return customsManager;
	}
	public LabelManager getLabelManager()
	{
		return labelManager;
	}
}
