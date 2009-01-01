/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitry Polivaev
 *
 *  This file is modified by Dimitry Polivaev in 2008.
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

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.ListIterator;

import org.freeplane.core.ui.FreeplaneAction;

/**
 * @author foltin
 */
class CommonToggleFoldedAction extends FreeplaneAction {
	public CommonToggleFoldedAction() {
		super("toggle_folded");
	}

	public void actionPerformed(final ActionEvent e) {
		toggleFolded();
	}

	private ListIterator resetIterator(final ListIterator iterator) {
		while (iterator.hasPrevious()) {
			iterator.previous();
		}
		return iterator;
	}

	public void toggleFolded() {
		toggleFolded(getModeController().getSelectedNodes().listIterator());
	}

	public void toggleFolded(final ListIterator listIterator) {
		final boolean fold = getModeController().getMapController().getFoldingState(
		    resetIterator(listIterator));
		for (final Iterator i = resetIterator(listIterator); i.hasNext();) {
			final NodeModel node = (NodeModel) i.next();
			getModeController().getMapController().setFolded(node, fold);
		}
	}
}
