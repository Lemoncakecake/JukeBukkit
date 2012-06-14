package cc.thedudeguy.jukebukkit.materials.blocks.speakerwire;

import org.bukkit.block.BlockFace;
import org.getspout.spoutapi.block.SpoutBlock;

import cc.thedudeguy.jukebukkit.materials.blocks.SpeakerWireBlock;
import cc.thedudeguy.jukebukkit.materials.blocks.designs.SpeakerWireTurnDesign;

public class SpeakerWireNorthEast extends SpeakerWireBlock {
	
	private int rotationY = 270;
	
	public SpeakerWireNorthEast() {
		super(SpeakerWireBlock.NORTHtoEAST);
		this.setBlockDesign(new SpeakerWireTurnDesign(rotationY));
	}

	@Override
	public boolean hasOpenEnd(SpoutBlock block) {
		if (!this.isFaceConnected(block, BlockFace.NORTH)) return true;
		if (!this.isFaceConnected(block, BlockFace.EAST)) return true;
		return false;
	}
	
}