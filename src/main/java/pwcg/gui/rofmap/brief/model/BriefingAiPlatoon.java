package pwcg.gui.rofmap.brief.model;

import pwcg.campaign.api.Side;

public class BriefingAiPlatoon implements IBriefingPlatoon
{
    private Side side;
    private int companyId;
    private BriefingPlatoonParameters briefingPlatoonParameters;

    public BriefingAiPlatoon(BriefingPlatoonParameters briefingPlatoonParameters, int companyId, Side side)
    {
        this.companyId = companyId;
        this.side = side;
        this.briefingPlatoonParameters = briefingPlatoonParameters;
    }

    @Override
    public BriefingPlatoonParameters getBriefingPlatoonParameters()
    {
        return briefingPlatoonParameters;
    }

    @Override
    public int getCompanyId()
    {
        return companyId;
    }

    @Override
    public Side getSide()
    {
        return side;
    }
}
