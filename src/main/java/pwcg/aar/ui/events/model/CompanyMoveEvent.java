package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class CompanyMoveEvent  extends AAREvent
{
    private String newAirfield;
    private String lastAirfield;
    private int companyId;
    private boolean needsFerryMission;
    private String companyName = "";

    
    public CompanyMoveEvent(String lastAirfield, String newAirfield, int companyId, boolean needsFerryMission, Date date, boolean isNewsWorthy)
    {
        super(date, isNewsWorthy);
        this.lastAirfield = lastAirfield;
        this.newAirfield = newAirfield;
        this.companyId = companyId;
        this.needsFerryMission = needsFerryMission;
        
        try
        {
            CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
            Company company = companyManager.getCompany(companyId);
            if (company != null)
            {
                this.companyName = company.determineDisplayName(date);
            }
        }
        catch (PWCGException e)
        {
            this.companyName = "";
            e.printStackTrace();
        }
    }

    public String getNewAirfield()
    {
        return newAirfield;
    }

    public String getLastAirfield()
    {
        return lastAirfield;
    }

    public int getCompanyId()
    {
        return companyId;
    }

    public boolean isNeedsFerryMission()
    {
        return needsFerryMission;
    }

    public String getCompanyName()
    {
        return companyName;
    }
}
