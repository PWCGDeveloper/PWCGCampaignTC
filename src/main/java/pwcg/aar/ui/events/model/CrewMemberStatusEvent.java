package pwcg.aar.ui.events.model;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMemberStatus;

public class CrewMemberStatusEvent  extends AARCrewMemberEvent
{
	private int status = CrewMemberStatus.STATUS_ACTIVE;

    public CrewMemberStatusEvent(Campaign campaign, int status, int companyId, int crewMemberSerialNumber, Date date, boolean isNewsWorthy)
    {
        super(campaign, companyId, crewMemberSerialNumber, date, isNewsWorthy);
        this.status = status;
    }

	public int getStatus() {
		return status;
	}
}
