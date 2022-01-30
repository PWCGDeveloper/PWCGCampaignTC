package pwcg.gui.rofmap.infoMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class InfoTownSelectPopup extends JPopupMenu
{
    private static final long serialVersionUID = 1L;

    public InfoTownSelectPopup(InfoMapPanel parent, String airfield)
    {
        JMenuItem townMenuItem = new JMenuItem("Select Town:"+airfield);
        townMenuItem.addActionListener(parent);
        add(townMenuItem);
        
        JMenuItem cancelWpItem = new JMenuItem("Cancel");
        add(cancelWpItem);
        cancelWpItem.addActionListener(parent);     
    }

}
