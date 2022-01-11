package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;

public class AceKilledEvent extends AARCrewMemberEvent
{
    private String status = "";

    public AceKilledEvent(Campaign campaign, String status, int companyId, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, companyId, crewMemberSerialNumber, date, isNewsWorthy);
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
}
