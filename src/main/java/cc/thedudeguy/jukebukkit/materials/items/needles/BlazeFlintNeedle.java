/**
 * This file is part of JukeBukkit
 *
 * Copyright (C) 2011-2012  Chris Churchwell
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
 */
package cc.thedudeguy.jukebukkit.materials.items.needles;

import java.io.File;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.material.CustomItem;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import cc.thedudeguy.jukebukkit.JukeBukkit;
import cc.thedudeguy.jukebukkit.materials.blocks.designs.RPNeedle;
import cc.thedudeguy.jukebukkit.permission.CraftPermissible;

public class BlazeFlintNeedle extends GenericCustomItem implements Needle, CraftPermissible {

	public BlazeFlintNeedle() {
		super(JukeBukkit.instance, "Blaze Needle");
		setTexture(new File(JukeBukkit.instance.getDataFolder(), new File("textures", "needle_blaze-flint.png").getPath()));
	}

	@Override
	public RPNeedle getNeedleType() {
		return RPNeedle.BLAZE_FLINT;
	}

	@Override
	public String getCraftPermission() {
		return "jukebukkit.craft.needle";
	}
	
	public CustomItem setTexture(File texture) {
		this.texture = texture.getName();
		SpoutManager.getFileManager().addToCache(JukeBukkit.instance, texture);
		return this;
	}
	
}
