package pwcg.testutils;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.crewmember.VictoryEntity;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;

public class CampaignPersonnelTestHelper
{

    public static void addVictories (CrewMember crewMember, Date date, int numVictories) throws PWCGException
    {
        for (int i = 0; i < numVictories; ++i)
        {
            VictoryEntity victim = new VictoryEntity();
            victim.setAirOrGround(Victory.VEHICLE);
            victim.setType("pziv-g");
            VictoryEntity victor = new VictoryEntity();
            
            Victory victory = new Victory();
            victory.setDate(date);
            victory.setLocation("Somewhere");
            victory.setVictim(victim);
            victory.setVictor(victor);
            
            crewMember.addGroundVictory(victory);
        }
    }
    
    public static CrewMember getCrewMemberByRank(Campaign campaign, String rank) throws PWCGException
    {
        CrewMember selectedAiSquadMember = null;
        
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAI(campaign.getPersonnelManager().getAllCampaignMembers(), campaign.getDate());
        for (CrewMember aiSquadMember : crewMembers.getCrewMemberList())
        {
            System.out.println("Nanme: " + aiSquadMember.getNameAndRank() + "  from " + aiSquadMember.getCompanyId());
            if (aiSquadMember.getRank().equals(rank))
            {
                if (!(aiSquadMember instanceof TankAce))
                {
                    selectedAiSquadMember = aiSquadMember;
                }
            }
        }

        return selectedAiSquadMember;
    }

}
