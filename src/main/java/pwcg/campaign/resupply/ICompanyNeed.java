package pwcg.campaign.resupply;

import pwcg.core.exception.PWCGException;

public interface ICompanyNeed
{
    public void determineResupplyNeeded() throws PWCGException;
    public int getCompanyId();
    public boolean needsResupply();
    public void noteResupply();
    public int getNumNeeded();
}
