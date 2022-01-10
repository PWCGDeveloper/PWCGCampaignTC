package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public interface ICampaignCache
{
    Campaign makeCampaignForceCreation(CompanyTestProfile profile) throws PWCGException;
}