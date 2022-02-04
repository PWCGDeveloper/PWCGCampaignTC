package pwcg.gui.utils;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import pwcg.core.location.Coordinate;

public class MapPointInfoPopup extends JPopupMenu
{
	private static final long serialVersionUID = 1L;

	public MapPointInfoPopup(ActionListener parent, Coordinate coordinate)
    {
        JMenuItem coordinateitem = new JMenuItem("Coordinates: " + coordinate.getXPos() + ", " + coordinate.getZPos());
        add(coordinateitem);
        coordinateitem.addActionListener(parent);
    }
}
