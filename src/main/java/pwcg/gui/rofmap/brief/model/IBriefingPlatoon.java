package pwcg.gui.rofmap.brief.model;

import pwcg.campaign.api.Side;

public interface IBriefingPlatoon
{

    BriefingPlatoonParameters getBriefingPlatoonParameters();

    int getCompanyId();

    Side getSide();

}