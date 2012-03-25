package cc.thedudeguy.jukebukkit.materials.blocks;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.material.block.GenericCustomBlock;
import org.getspout.spoutapi.player.SpoutPlayer;

import cc.thedudeguy.jukebukkit.JukeBukkit;
import cc.thedudeguy.jukebukkit.database.DiscData;
import cc.thedudeguy.jukebukkit.database.RecordPlayerBlockDesigns;
import cc.thedudeguy.jukebukkit.database.RecordPlayerData;
import cc.thedudeguy.jukebukkit.materials.blocks.designs.RecordPlayerDesign;
import cc.thedudeguy.jukebukkit.materials.items.BurnedDisc;
import cc.thedudeguy.jukebukkit.materials.items.Items;
import cc.thedudeguy.jukebukkit.materials.items.Needle;

public class RecordPlayer extends GenericCustomBlock {
	
	public static HashMap<String, RecordPlayer> subBlocks = new HashMap<String, RecordPlayer>();
	
	public static class RecordPlayerSubBlock extends RecordPlayer {
		
		public RecordPlayerSubBlock(RecordPlayerDesign rpDesign) {
			super(rpDesign.getDesignTypeId());
			
			this.setName("Record Player SubBlock (Do Not Use)");
			this.setBlockDesign(rpDesign);
			
			SpoutItemStack dropItem = new SpoutItemStack(getSubBlock(RecordPlayerDesign.NEEDLE_NONE, RecordPlayerDesign.DISC_NONE, RecordPlayerDesign.INDICATOR_RED), 1);
			
			this.setItemDrop(dropItem);
		}
	}
	
	public static RecordPlayer getSubBlock(int needle, int discColor, int indicator) {
		
		int color = RecordPlayerDesign.DISC_NONE;
		
		if (RecordPlayerDesign.discColorToTextureMap.containsKey(discColor)) {
			color = RecordPlayerDesign.discColorToTextureMap.get(discColor);
		}
		
		RecordPlayerDesign rpDesign = new RecordPlayerDesign(needle, color, indicator);
		
		if (!subBlocks.containsKey(rpDesign.getDesignTypeId())) {
			subBlocks.put(rpDesign.getDesignTypeId(), new RecordPlayerSubBlock(rpDesign));
			
			//save the combo to the db if it doesnt already exist.
			RecordPlayerBlockDesigns rpbd = JukeBukkit.instance.getDatabase().find(RecordPlayerBlockDesigns.class)
					.where()
						.eq("needle", rpDesign.getNeedle())
						.eq("disc", rpDesign.getDisc())
						.eq("indicator", rpDesign.getIndicator())
					.findUnique();
			if (rpbd == null) {
				rpbd = new RecordPlayerBlockDesigns();
				rpbd.setDisc(rpDesign.getDisc());
				rpbd.setNeedle(rpDesign.getNeedle());
				rpbd.setIndicator(rpDesign.getIndicator());
				JukeBukkit.instance.getDatabase().save(rpbd);
			}
		} 
		
		return subBlocks.get(rpDesign.getDesignTypeId());
	}
	
	public static void updateBlockDesign(SpoutBlock block, RecordPlayerData data) {
		int color;
		if (data.hasDisc()) {
			//get the disc color.
			DiscData discData = JukeBukkit.instance.getDatabase().find(DiscData.class)
					.where()
						.ieq("nameKey", data.getDiscKey())
					.findUnique();
			if (discData == null) {
				Bukkit.getLogger().log(Level.WARNING, "Disc Key is missing from discs table");
				color = RecordPlayerDesign.DISC_NONE;
			} else {
				color = discData.getColor();
			}
		} else {
			color = RecordPlayerDesign.DISC_NONE;
		}
		
		int indicator;
		if (data.getNeedleType() != RecordPlayerDesign.NEEDLE_NONE && color != RecordPlayerDesign.DISC_NONE)
		{
			indicator = RecordPlayerDesign.INDICATOR_GREEN;
		} else {
			indicator = RecordPlayerDesign.INDICATOR_RED;
		}
		
		block.setCustomBlock(getSubBlock(data.getNeedleType(), color, indicator));
		
	}
	
	public RecordPlayer() {
		super(JukeBukkit.instance, "Record Player", 3);
		
		RecordPlayerDesign rpDesign = new RecordPlayerDesign();
		this.setBlockDesign(rpDesign);
		
		this.setHardness(MaterialData.wood.getHardness());
		this.setLightLevel(1);
		
		//store this into the hashmap since it will always be the defaul
		subBlocks.put(rpDesign.getDesignTypeId(), this);
		
		//Bukkit.getLogger().log(Level.INFO, rpDesign.getDesignTypeId());
		
		initDesigns();
	}
	
	public RecordPlayer(String nameId) {
		super(JukeBukkit.instance, nameId, 3);
		
		this.setHardness(MaterialData.wood.getHardness());
		this.setLightLevel(1);
	}
	
	private void initDesigns() {
		 List<RecordPlayerBlockDesigns> rpbd = JukeBukkit.instance.getDatabase().find(RecordPlayerBlockDesigns.class).findList();
		 
		 if (rpbd.isEmpty()) {
			 Bukkit.getLogger().log(Level.INFO, "[JukeBukkit] No RecordPlayer Designs to load.");
		 } else {
			 int count = 0;
			 for (RecordPlayerBlockDesigns record : rpbd) {
				 RecordPlayerDesign rpDesign = new RecordPlayerDesign(record.getNeedle(), record.getDisc(), record.getIndicator());
				 subBlocks.put(rpDesign.getDesignTypeId(), new RecordPlayerSubBlock(rpDesign));
				 count++;
	         }
			 Bukkit.getLogger().log(Level.INFO, "[JukeBukkit] Initialized "+ String.valueOf(count) +" RecordPlayer Designs.");
		 }
		 
	}
	
	/**
	 * Event fired when a player right clicks on a block.
	 * Lots of things to do here. if theres a disc in it, the disc needs to be ejected, and if the player
	 * is holding the right items in their hand, then those select items may be taken by this block.
	 */
	public boolean onBlockInteract(org.bukkit.World world, int x, int y, int z, SpoutPlayer player) {
		
		Location location = new Location(world, (double)x, (double)y, (double)z);
		
		//get data from the db
		RecordPlayerData rpdata = JukeBukkit.instance.getDatabase().find(RecordPlayerData.class)
				.where()
					.eq("x", (double)x)
					.eq("y", (double)y)
					.eq("z", (double)z)
					.ieq("worldName", world.getName())
				.findUnique();
		if (rpdata == null) {
			Bukkit.getLogger().log(Level.WARNING, "[JukeBukkit] Missing Record Player Data, this data should have been created when the block was placed.");
			return false;
		}
		
		SpoutItemStack inHand = new SpoutItemStack(player.getItemInHand());
		
		if ( !rpdata.hasDisc() && inHand.getMaterial() instanceof BurnedDisc) {
			BurnedDisc discInHand = (BurnedDisc)inHand.getMaterial();
			
			rpdata.setDiscKey(discInHand.getKey());
			JukeBukkit.instance.getDatabase().save(rpdata);
			
			//we know its a custom item, go ahaed and remove 1 from the hand.
			if (inHand.getAmount()<2) {
				player.setItemInHand(new ItemStack(Material.AIR));
			} else {
				player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount()-1);
			}
			
			//start the music
			if (rpdata.getNeedleType() != RecordPlayerDesign.NEEDLE_NONE) {
				playMusic(discInHand.getUrl(), location);
			}
			
			updateBlockDesign((SpoutBlock)world.getBlockAt(x, y, z), rpdata);
			
			return true;
			
		}
		
		if ( rpdata.getNeedleType() == RecordPlayerDesign.NEEDLE_NONE && inHand.isCustomItem() && inHand.getMaterial() instanceof Needle ) {
			rpdata.setNeedleType(RecordPlayerDesign.NEEDLE_WOOD_FLINT);
			JukeBukkit.instance.getDatabase().save(rpdata);
			
			 //remove 1 from hand
			if (inHand.getAmount()<2) {
				player.setItemInHand(new ItemStack(Material.AIR));
			} else {
				inHand.setAmount(inHand.getAmount()-1);
				player.setItemInHand(inHand);
			}
			
			updateBlockDesign((SpoutBlock)world.getBlockAt(x, y, z), rpdata);
			
			return true;
		}
		
		if ( rpdata.hasDisc() ) {
			//get disc.
			DiscData discData = JukeBukkit.instance.getDatabase().find(DiscData.class)
					.where()
						.ieq("nameKey", rpdata.getDiscKey())
					.findUnique();
			if (discData == null) {
				Bukkit.getLogger().log(Level.WARNING, "Disc Key is missing from discs table");
			} else {
				//create disc to spawn
				BurnedDisc disc = new BurnedDisc(discData);
				ItemStack iss = new SpoutItemStack(disc, 1);
				Location spawnLoc = location;
				spawnLoc.setY(spawnLoc.getY()+1);
				spawnLoc.getWorld().dropItem(spawnLoc, iss);
			}
			
			rpdata.setDiscKey(null);
			JukeBukkit.instance.getDatabase().save(rpdata);
			
			stopMusic(location);
			
			updateBlockDesign((SpoutBlock)world.getBlockAt(x, y, z), rpdata);
			
			return true;
		}
		
		if ( rpdata.getNeedleType() != RecordPlayerDesign.NEEDLE_NONE ) {
			
			rpdata.setNeedleType(RecordPlayerDesign.NEEDLE_NONE);
			JukeBukkit.instance.getDatabase().save(rpdata);
			Location spawnLoc = location;
			spawnLoc.setY(spawnLoc.getY()+1);
			world.dropItem(spawnLoc, new SpoutItemStack(Items.needle, 1));
			
			updateBlockDesign((SpoutBlock)world.getBlockAt(x, y, z), rpdata);
			
			return true;
		}
		
		return false;
	}
	
	public void onBlockClicked(World world, int x, int y, int z, SpoutPlayer player) {
		//when the block is placed we need to make sure to get data set up for it.
		RecordPlayerData rpd = JukeBukkit.instance.getDatabase().find(RecordPlayerData.class)
				.where()
					.eq("x", (double)x)
					.eq("y", (double)y)
					.eq("z", (double)z)
					.ieq("worldName", world.getName())
				.findUnique();
		if (rpd == null) {
			rpd = new RecordPlayerData();
			rpd.setDiscKey(null);
			rpd.setNeedleType(0);
			rpd.setX((double)x);
			rpd.setY((double)y);
			rpd.setZ((double)z);
			rpd.setWorldName(world.getName());
			JukeBukkit.instance.getDatabase().save(rpd);
		}
		
		if (rpd.hasDisc() && rpd.getNeedleType() != RecordPlayerDesign.NEEDLE_NONE) {
			DiscData discData = JukeBukkit.instance.getDatabase().find(DiscData.class)
					.where()
						.ieq("nameKey", rpd.getDiscKey())
					.findUnique();
			if (discData == null) {
				Bukkit.getLogger().log(Level.WARNING, "Disc Key is missing from discs table");
			} else {
				Location location = new Location(world, (double)x, (double)y, (double)z);
				playMusic(discData.getUrl(), location);
			}
		}
	}
	
	/**
	 * Event Fired when this block is placed.
	 * Update/Insert the data into the DB for this block, so we can keep an eye on it.
	 */
	public void onBlockPlace(org.bukkit.World world, int x, int y, int z) {
		//when the block is placed we need to make sure to get data set up for it.
		RecordPlayerData rpd = JukeBukkit.instance.getDatabase().find(RecordPlayerData.class)
				.where()
					.eq("x", (double)x)
					.eq("y", (double)y)
					.eq("z", (double)z)
					.ieq("worldName", world.getName())
				.findUnique();
		if (rpd == null) {
			rpd = new RecordPlayerData();
			rpd.setDiscKey(null);
			rpd.setNeedleType(0);
			rpd.setX((double)x);
			rpd.setY((double)y);
			rpd.setZ((double)z);
			rpd.setWorldName(world.getName());
			JukeBukkit.instance.getDatabase().save(rpd);
		}
		/* If its still set, well go ahead and leave it, because it could be an blockplace even from setting the custom block to a different subblock for this location */
	}
	
	/**
	 * Event Fired when this block is destroyed.
	 * Firstly, if this player has any items in it, like a record, or a needle, then those items need
	 * to be spawned into the world.
	 * lastly, remove the block data we have saved in the database to keep it nice and tidy.
	 */
	public void onBlockDestroyed(org.bukkit.World world, int x, int y, int z) {
		
		Location location = new Location(world, (double)x, (double)y, (double)z);
		Location spawnLoc = location;
		spawnLoc.setY(spawnLoc.getY()+1);
		
		//if theres junk in this block we need to make sure it drops too
		RecordPlayerData rpd = JukeBukkit.instance.getDatabase().find(RecordPlayerData.class)
				.where()
					.eq("x", (double)x)
					.eq("y", (double)y)
					.eq("z", (double)z)
					.ieq("worldName", world.getName())
				.findUnique();
		if (rpd != null) {
			if (rpd.getNeedleType() != RecordPlayerDesign.NEEDLE_NONE) {
				world.dropItem(spawnLoc, new SpoutItemStack(Items.needle, 1));
			}
			
			if (rpd.hasDisc()) {
				//get disc.
				DiscData discData = JukeBukkit.instance.getDatabase().find(DiscData.class)
						.where()
							.ieq("nameKey", rpd.getDiscKey())
						.findUnique();
				if (discData == null) {
					Bukkit.getLogger().log(Level.WARNING, "Disc Key is missing from discs table");
				} else {
					//create disc to spawn
					BurnedDisc disc = new BurnedDisc(discData);
					ItemStack iss = new SpoutItemStack(disc, 1);
					spawnLoc.getWorld().dropItem(spawnLoc, iss);
				}
				
				//just in case there was a disc
				stopMusic(location);
			}
		}
		
		//delete ALL data associated to this location, just incase somehow multiples got into the database this will take care of that.
		List<RecordPlayerData> rpdall = JukeBukkit.instance.getDatabase().find(RecordPlayerData.class)
				.where()
					.eq("x", (double)x)
					.eq("y", (double)y)
					.eq("z", (double)z)
					.ieq("worldName", world.getName())
				.findList();
		if (!rpdall.isEmpty()) {
			JukeBukkit.instance.getDatabase().delete(rpdall);
		}
	}
	
	public int getRange() {
		return 15;
	}
	
	public void playMusic(String url, Location location) {
		
		//get players in radius of the jukebox and start it for only those players
		for(Player p:location.getWorld().getPlayers()) {
			double distance = location.toVector().distance(p.getLocation().toVector());
			if (distance<=(double)getRange()) {
				SpoutPlayer sp = SpoutManager.getPlayer(p);
				if (sp.isSpoutCraftEnabled()) {
					try {
						SpoutManager.getSoundManager().playCustomMusic(JukeBukkit.instance, sp, url, true, location, getRange());
					} catch (Exception e) {
						//the disc has an error.
						SpoutManager.getSoundManager().playGlobalCustomSoundEffect(JukeBukkit.instance, "jb_error.wav", false, location, 8);
					}
				}
			}
		}
		
	}
	
	public void stopMusic(Location location) {
		//get players in radius of the jukebox and start it for only those players
		for(Player p:location.getWorld().getPlayers()) {
			double distance = location.toVector().distance(p.getLocation().toVector());
			if (distance<=(double)getRange()) {
				SpoutPlayer sp = SpoutManager.getPlayer(p);
				if (sp.isSpoutCraftEnabled()) {
					SpoutManager.getSoundManager().stopMusic(sp);
				}
			}
		}
	}
	
}
