package pwcg.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.coop.CoopUserManager;
import pwcg.core.exception.PWCGUserException;

public class CrewMemberReplacerCoop extends CrewMemberReplacer  implements ICrewMemberReplacer
{    
    public CrewMemberReplacerCoop(Campaign campaign)
    {
        super(campaign);
    }
	
    public CrewMember createPersona(String playerCrewMemberName, String rank, String companyName, String coopUsername) throws PWCGUserException, Exception
    {        
        CrewMember newCompanyMewmber = super.createPersona(playerCrewMemberName, rank, companyName, coopUsername);
        if (campaign.isCoop())
        {
            CoopUserManager.getIntance().createCoopPersona(campaign.getName(), newCompanyMewmber, coopUsername);
        }
        return newCompanyMewmber;
    }
}
