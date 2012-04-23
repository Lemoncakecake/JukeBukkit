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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.PersistenceException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import cc.thedudeguy.jukebukkit.database.DiscData;
import cc.thedudeguy.jukebukkit.database.LabelData;
import cc.thedudeguy.jukebukkit.database.RecordPlayerBlockDesigns;
import cc.thedudeguy.jukebukkit.database.RecordPlayerData;
import cc.thedudeguy.jukebukkit.listeners.DiscLabelListener;
import cc.thedudeguy.jukebukkit.materials.blocks.Blocks;
import cc.thedudeguy.jukebukkit.materials.items.Items;
import cc.thedudeguy.jukebukkit.server.MusicHandler;
import cc.thedudeguy.jukebukkit.server.ServerHandler;
import cc.thedudeguy.jukebukkit.util.DiscImporter;
import cc.thedudeguy.jukebukkit.util.Recipies;
import cc.thedudeguy.jukebukkit.util.ResourceManager;

/**
 * The main class for the Jukebukkit Plugin
 * @author Chris Churchwell
 */
public class JukeBukkit extends JavaPlugin {
	
	public static JukeBukkit instance;
	public Server HTTPserver;
	SelectChannelConnector HTTPconnector;
	
	Blocks blocks;
	Items items;
	
	public void onEnable()
	{	
		//copy the config
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		//double check for spout.
		if (!Bukkit.getPluginManager().isPluginEnabled("Spout")) {
			Bukkit.getLogger().log(Level.WARNING, "[JukeBukkit] Could not start: Spout not found.");
			setEnabled(false);
			return;
		}
		
		instance = this;
		
		ResourceManager.copyResources();
		ResourceManager.copyWebFiles();
		//ResourceManager.preLoginCache();
		
		setupDatabase();
		
		//incase the old discs.yml exists, we should import the old discs.
		DiscImporter.checkForOldDiscsImport();
		
		items = new Items();
		blocks = new Blocks();
		
		Recipies.load();
		
		this.getServer().getPluginManager().registerEvents(new DiscLabelListener(), this);
		
		this.getCommand("jukebukkit").setExecutor(new CommandHandler());
		
		ResourceManager.resetCache();
		
		//start the web server up
		
		HTTPserver = new Server();
		HTTPconnector = new SelectChannelConnector();
		HTTPconnector.setPort(getConfig().getInt("webServerPort"));
		HTTPserver.addConnector(HTTPconnector);
		
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(false);
		resourceHandler.setResourceBase(new File(this.getDataFolder(), "web").getAbsolutePath());
        //resource_handler.setWelcomeFiles(new String[]{ "index.html" });
 
		HandlerList handlers = new HandlerList();
		handlers.addHandler(new ServerHandler());
		handlers.addHandler(new MusicHandler());
		handlers.addHandler(resourceHandler);
		
		HTTPserver.setHandler(handlers);
		
		if (getConfig().getBoolean("enableWebServer") == true) {
			
			try {
				HTTPserver.start();
				//HTTPserver.join();
				Bukkit.getLogger().log(Level.INFO, "[JukeBukkit] Web Server started on port: " + getConfig().getString("webServerPort"));
			} catch (Exception e) {
				Bukkit.getLogger().log(Level.WARNING, "[JukeBukkit] Unable to start web server");
				e.printStackTrace();
			}
		}
	}
	
	public void onDisable()
	{
		//stop the web server
		if (HTTPserver.isRunning()) {
			try {
				//TODO: Crashes on stop with a ClassNotFoundException, need to fix.
				HTTPserver.stop();
			} catch (Exception e) {
				Bukkit.getLogger().log(Level.WARNING, "[JukeBukkit] Unable to stop web server");
				e.printStackTrace();
			}
		}
		
		Bukkit.getLogger().log(Level.INFO, "[JukeBukkit] Disabled.");
	}
	
	/**
	 * Sets up the ebean database if needed
	 */
	private void setupDatabase() {
		try {
            getDatabase().find(RecordPlayerBlockDesigns.class).findRowCount();
            getDatabase().find(RecordPlayerData.class).findRowCount();
            getDatabase().find(DiscData.class).findRowCount();
            getDatabase().find(LabelData.class).findRowCount();
        } catch (PersistenceException ex) {
            Bukkit.getLogger().log(Level.INFO, "[JukeBukkit] Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
	}
	
	 @Override	
	 public List<Class<?>> getDatabaseClasses() {
		 List<Class<?>> list = new ArrayList<Class<?>>();
	     list.add(RecordPlayerBlockDesigns.class);
	     list.add(RecordPlayerData.class);
	     list.add(DiscData.class);
	     list.add(LabelData.class);
	     return list;
	 }
	 
	 /*
		@EventHandler
		public void onBlockPlaced(BlockPlaceEvent event) {
			event.setBuild(true); //MEW!

			final Player ply = event.getPlayer();
			final Block block = ((SpoutBlock)event.getBlock()).getCustomBlock();
			if(!(block instanceof JukeboxBlock)) return;
			final JukeboxBlock jukeboxBlock = (JukeboxBlock)block;
			String permission = jukeboxBlock.getPermission();
			if(permission == null) return;
			if(!ply.hasPermission(permission)) {
				event.setBuild(false);
				event.setCancelled(true);
			}
		}
		
		
		@EventHandler
		public void onPlayerCraft(CraftItemEvent event) {
			final Player ply = event.getPlayer();
	                final ItemStack st = event.getResult();
	                if (st==null) return;
			final org.getspout.spoutapi.material.Material block = new SpoutItemStack(st).getMaterial();
			if(!(block instanceof JukeboxBlock)) return;
			final JukeboxBlock jukeboxBlock = (JukeboxBlock)block;
			String permission = jukeboxBlock.getPermission();
			if(permission == null) return;
			if(!ply.hasPermission(permission)) {
				event.setResult(null);
				event.setCancelled(true);
			}
		}
		*/
}
