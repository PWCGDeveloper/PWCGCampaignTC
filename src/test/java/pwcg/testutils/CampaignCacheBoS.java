package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;

public class CampaignCacheBoS extends CampaignCacheBase implements ICampaignCache
{    
    protected Campaign makeCampaignFromModel(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate();          
        PWCGContext.getInstance().setCampaign(campaign);
        return campaign;
    }

    protected void initialize() throws PWCGException
    {
        
        PWCGContext.getInstance();
        if (campaignProfiles.isEmpty())
        {
            loadCampaignProfiles();
        }
    }

    @Override
    protected void loadCampaignProfiles() throws PWCGException
    {
        makeProfile(CompanyTestProfile.COOP_COOPERATIVE_PROFILE);
        makeProfile(CompanyTestProfile.COOP_COMPETITIVE_PROFILE);
        makeProfile(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);        
        makeProfile(CompanyTestProfile.TANK_DIVISION_147_PROFILE);        
        makeProfile(CompanyTestProfile.THIRD_DIVISION_PROFILE);        
        makeProfile(CompanyTestProfile.SEVENTH_DIVISION_PROFILE);        
    }
}
