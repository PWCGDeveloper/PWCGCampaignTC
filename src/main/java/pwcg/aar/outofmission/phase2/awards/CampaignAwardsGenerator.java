package pwcg.aar.outofmission.phase2.awards;

import java.util.List;

import pwcg.aar.AARFactory;
import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public class CampaignAwardsGenerator
{
    private AARContext aarContext;
    private CampaignMemberAwardsGenerator awardsGenerator;

    public CampaignAwardsGenerator(Campaign campaign, AARContext aarContext)
    {
        this.aarContext = aarContext;
        this.awardsGenerator =  AARFactory.makeCampaignMemberAwardsGenerator(campaign, aarContext);
    }

    public AARPersonnelAwards createCampaignMemberAwards(List<CrewMember> crewMembersToEvaluate) throws PWCGException
    {
        AARPersonnelAwards personnelAwards = AARFactory.makeAARPersonnelAwards();

        for (CrewMember crewMember : crewMembersToEvaluate)
        {
            if (!crewMember.isHistoricalAce())
            {                
                int victoriesToday = getTankVictoryCountToday(crewMember);
                AARPersonnelAwards personnelAwardsForCrewMember = awardsGenerator.generateAwards(crewMember, victoriesToday);
                personnelAwards.merge(personnelAwardsForCrewMember);
            }
        }
        
        return personnelAwards;
    }

    private int getTankVictoryCountToday(CrewMember crewMember)
    {
        int airVictoriesToday = aarContext.getPersonnelAcheivements().getVictoryCountForCrewMember(crewMember.getSerialNumber());
        return airVictoriesToday;
    }
}
