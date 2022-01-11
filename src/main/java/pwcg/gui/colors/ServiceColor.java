package pwcg.gui.colors;

import java.awt.Color;
import java.util.Date;

import pwcg.campaign.company.Company;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;

public abstract class ServiceColor implements IServiceColorMap
{
    public Color getColorForCompany(Company company, Date date) throws PWCGException
    {
        PwcgRoleCategory primaryRole = company.determineCompanyPrimaryRoleCategory(date);

        return getColorForRole(primaryRole);
    }

    public abstract Color getColorForRole(PwcgRoleCategory roleCategory);
}
