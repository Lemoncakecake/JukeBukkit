package cc.thedudeguy.jukebukkit.materials.blocks.speakerwire;

import org.bukkit.block.BlockFace;
import org.getspout.spoutapi.block.SpoutBlock;

import cc.thedudeguy.jukebukkit.materials.blocks.SpeakerWireBlock;
import cc.thedudeguy.jukebukkit.materials.blocks.designs.SpeakerWireTurnDesign;

public class SpeakerWireSouthUp extends SpeakerWireBlock {

	private int rotationX = 270;
	private int rotationY = 0;
	private int rotationZ = 0;
	
	private float moveX = 0;
	private float moveY = -0.46875F;
	private float moveZ = 0.46875F;
	
	public SpeakerWireSouthUp() {
		super(SpeakerWireBlock.SOUTHtoUP);
		this.setBlockDesign(new SpeakerWireTurnDesign(rotationX, rotationY, rotationZ, moveX, moveY, moveZ));
	}

	@Override
	public boolean hasOpenEnd(SpoutBlock block) {
		if (!this.isFaceConnected(block, BlockFace.SOUTH)) return true;
		if (!this.isFaceConnected(block, BlockFace.UP)) return true;
		return false;
	}


}
