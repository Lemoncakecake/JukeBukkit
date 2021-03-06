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
package cc.thedudeguy.jukebukkit.gui.widget;

import org.getspout.spoutapi.gui.ListWidgetItem;

public class RepoSongItem extends ListWidgetItem {

	private String title;
	private String artist;
	private String songId;
	private String filename;
	
	public RepoSongItem(String title, String artist, String songId, String filename) {
		super();
		
		this.title = title;
		this.artist = artist;
		this.songId = songId;
		this.filename = filename;
		
		this.setTitle(this.title);
		this.setText("by " + this.artist);
	}
	
	public String getSongId() {
		return songId;
	}
	
	public String getFilename() {
		return filename;
	}
}
