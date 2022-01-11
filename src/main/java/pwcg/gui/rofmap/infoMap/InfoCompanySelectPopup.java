package pwcg.gui.rofmap.infoMap;

import java.util.Date;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;

public class InfoCompanySelectPopup extends JPopupMenu
{
    private static final long serialVersionUID = 1L;

    public InfoCompanySelectPopup(InfoMapPanel parent, List<Company> companysAtBase, Date date)
    {
        try
        {
            for (Company company : companysAtBase)
            {
                String companyName = company.determineDisplayName(date);
                JMenuItem companyMenuItem = new JMenuItem("Select Company:"+company.getCompanyId()+":"+companyName);
                companyMenuItem.addActionListener(parent);
                add(companyMenuItem);
            }
            
            JMenuItem cancelWpItem = new JMenuItem("Cancel");
            add(cancelWpItem);
            cancelWpItem.addActionListener(parent);     
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }
    }

}
