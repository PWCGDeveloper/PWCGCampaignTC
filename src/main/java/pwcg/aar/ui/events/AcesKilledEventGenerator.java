package pwcg.aar.ui.events;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.AceKilledEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.core.exception.PWCGException;

public class AcesKilledEventGenerator
{
    private Campaign campaign;
    
    public static int NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY = 15;
    
    public AcesKilledEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }


    public List<AceKilledEvent> createAceKilledEvents(List<CrewMember> acesKilledInMissionAndElapsedTime) throws PWCGException
    {
        List<AceKilledEvent> aceKilledEvents = new ArrayList<>();
        for (CrewMember ace : acesKilledInMissionAndElapsedTime)
        {
            if (ace == null)
            {
                continue;
            }
            
            Company aceSquad =  ace.determineCompany();
            if (aceSquad != null)
            {
                AceKilledEvent aceKilledEvent = makeAceKilledEvent(ace, aceSquad);
                if (aceKilledEvent != null)
                {
                    aceKilledEvents.add(aceKilledEvent);
                }
            }
        }
        
        return aceKilledEvents;
    }
    
    private AceKilledEvent makeAceKilledEvent(CrewMember ace, Company aceCompany) throws PWCGException
    {
        AceKilledEvent aceKilledEvent = null;
        
        if (ace.getCrewMemberVictories().getAirToAirVictoryCount() >= NUM_VICTORIES_FOR_ACE_TO_BE_NEWSWORTHY)
        {
            String status = CrewMemberStatus.crewMemberStatusToStatusDescription(ace.getCrewMemberActiveStatus());
    
            int aceSerialNumber = ace.getSerialNumber();
            int aceCompanyId = aceCompany.getCompanyId();
            boolean isNewsworthy = true;
            aceKilledEvent = new AceKilledEvent(campaign, status, aceCompanyId, aceSerialNumber, campaign.getDate(), isNewsworthy);
        }
        
        return aceKilledEvent;
    }

}
