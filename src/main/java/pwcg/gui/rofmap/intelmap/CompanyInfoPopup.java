package pwcg.gui.rofmap.intelmap;

import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class CompanyInfoPopup extends JPopupMenu
{
	private static final long serialVersionUID = 1L;

	public CompanyInfoPopup(IntelMapPanel parent, List<IntelCompanyMapPoint>mapPoints)
    {
		for (IntelCompanyMapPoint mapPoint : mapPoints)
		{
			JMenuItem mapPointItem = new JMenuItem(mapPoint.desc);
			mapPointItem.setActionCommand("Company:" + mapPoint.desc);
	    	add(mapPointItem);
	    	mapPointItem.addActionListener(parent);
		}
    }
}
