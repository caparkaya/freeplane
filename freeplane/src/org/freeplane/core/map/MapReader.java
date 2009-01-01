/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.core.map;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

import org.freeplane.core.io.ReadManager;
import org.freeplane.core.io.xml.TreeXmlReader;
import org.freeplane.n3.nanoxml.XMLParseException;

/**
 * @author Dimitry Polivaev
 * 20.12.2008
 */
public class MapReader {
	public class NodeTreeCreator {
		public NodeModel create(final Reader pReader) {
			final TreeXmlReader reader = new TreeXmlReader(readManager);
			reader.load(pReader);
			final NodeModel node = nodeBuilder.getMapChild();
			nodeBuilder.reset();
			return node;
		}

		public NodeModel createNodeTreeFromXml(final MapModel map, final Reader pReader)
		        throws XMLParseException, IOException {
			start(map);
			final NodeModel node = create(pReader);
			finish(node);
			return node;
		}

		public void finish(final NodeModel node) {
			final HashMap<String, String> newIds = nodeBuilder.getNewIds();
			readManager.readingCompleted(node, newIds);
			newIds.clear();
			createdMap = null;
		}

		void start(final MapModel map) {
			createdMap = map;
		}
	}

	private MapModel createdMap;
	private boolean mapLoadingInProcess;
	private final NodeBuilder nodeBuilder;
	final private ReadManager readManager;

	public MapReader(final ReadManager readManager) {
		this.readManager = readManager;
		nodeBuilder = new NodeBuilder(this);
		nodeBuilder.registerBy(readManager);
	}

	public NodeModel createNodeTreeFromXml(final MapModel map, final Reader pReader)
	        throws XMLParseException, IOException {
		try {
			mapLoadingInProcess = true;
			final NodeModel topNode = new NodeTreeCreator().createNodeTreeFromXml(map, pReader);
			mapLoadingInProcess = false;
			return topNode;
		}
		finally {
			mapLoadingInProcess = false;
		}
	}

	public MapModel getCreatedMap() {
		return createdMap;
	}

	public boolean isMapLoadingInProcess() {
		return mapLoadingInProcess;
	}

	public NodeTreeCreator nodeTreeCreator(final MapModel map) {
		final NodeTreeCreator nodeTreeCreator = new NodeTreeCreator();
		nodeTreeCreator.start(map);
		return nodeTreeCreator;
	}
}
