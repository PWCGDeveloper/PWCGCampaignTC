package pwcg.mission.platoon.crew;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.mission.platoon.tank.TankMcu;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlatoonCrewBuilderTest 
{
    protected Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
    }

    @Test
    public void testPlayerFlightGeneration() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        for (CrewMember player : campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
        {
            participatingPlayers.addCrewMember(player);
        }

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestMissionFromMissionType(participatingPlayers);

        verifyAllParticipatingPlayers(participatingPlayers, mission);
    }

    protected void verifyAllParticipatingPlayers(MissionHumanParticipants participatingPlayers, Mission mission) throws PWCGException
    {
        boolean allparticipatingPlayersFound = true;
        for (CrewMember player : participatingPlayers.getAllParticipatingPlayers())
        {
            boolean playerFound = false;
            for (ITankPlatoon unit : mission.getPlatoons().getPlayerUnits())
            {
                for(TankMcu tank : unit.getTanks())
                {
                    if (tank.getName().contains(player.getName()))
                    {
                        playerFound = true;
                    }
                }
            }
            
            if(playerFound == false)
            {
                allparticipatingPlayersFound = false;
            }
        }

        assert(allparticipatingPlayersFound);
    }
}
