package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;

public class AARCrewMemberEvent extends AAREvent
{
    public static final int ALL_CompanyS = -1;

    private Integer companyId = ALL_CompanyS;
    private int crewMemberSerialNumber = 0;
    private String companyName = "";
    private String crewMemberName = "";

    public AARCrewMemberEvent(Campaign campaign, int companyId, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(date, isNewsWorthy);
        this.crewMemberSerialNumber = crewMemberSerialNumber;
        this.companyId = companyId;
        
        try
        {
            CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
            Company company = companyManager.getCompany(companyId);
            if (company != null)
            {
                this.companyName = company.determineDisplayName(date);
            }
            
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(crewMemberSerialNumber);
            if (crewMember != null)
            {
                this.crewMemberName = crewMember.getNameAndRank();
            }
        }
        catch (Exception e)
        {
            this.companyName = "";
            e.printStackTrace();
        }
    }

    public Integer getCompanyId()
    {
        return companyId;
    }

    public int getCrewMemberSerialNumber()
    {
        return crewMemberSerialNumber;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public String getCrewMemberName()
    {
        return crewMemberName;
    }
}
