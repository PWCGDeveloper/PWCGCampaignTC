package pwcg.aar;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class CompanyForMissionBuilder
{
    Campaign campaign;
    List<Company> companysInMission = new ArrayList<>();


    public CompanyForMissionBuilder (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<Company> makeCompanysInMission() throws PWCGException
    {
        Company gd = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        Company stg77 = PWCGContext.getInstance().getCompanyManager().getCompany(20121077);
        Company kg76 = PWCGContext.getInstance().getCompanyManager().getCompany(20132076);
        Company reg11 = PWCGContext.getInstance().getCompanyManager().getCompany(10111011);
        Company reg132 = PWCGContext.getInstance().getCompanyManager().getCompany(10131132);
        Company reg175 = PWCGContext.getInstance().getCompanyManager().getCompany(10121175);
        
        companysInMission.add(gd);
        companysInMission.add(stg77);
        companysInMission.add(kg76);
        companysInMission.add(reg11);
        companysInMission.add(reg132);
        companysInMission.add(reg175);
        
        return companysInMission;
    }

}
