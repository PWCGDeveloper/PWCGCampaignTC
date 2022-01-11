package pwcg.campaign.personnel;

import pwcg.aar.campaign.update.CampaignUpdateNewCompanyEquipper;
import pwcg.aar.campaign.update.CampaignUpdateNewCompanyStaffer;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class InitialCompanyBuilder
{
    public void buildNewCompanies(Campaign campaign) throws PWCGException
    {
        CampaignUpdateNewCompanyStaffer newCompanyStaffer = new CampaignUpdateNewCompanyStaffer(campaign);
        newCompanyStaffer.staffNewCompanies();

        CampaignUpdateNewCompanyEquipper newCompanyEquipper = new CampaignUpdateNewCompanyEquipper(campaign);
        newCompanyEquipper.equipNewCompanys();
    }
}
