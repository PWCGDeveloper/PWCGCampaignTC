package pwcg.gui.colors;

import java.awt.Color;
import java.util.Date;

import pwcg.campaign.company.Company;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;

public interface IServiceColorMap
{
    Color getColorForRole(PwcgRoleCategory roleCategory);

    Color getColorForCompany(Company company, Date date) throws PWCGException;
}