package pwcg.mission.platoon.crew;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.MissionHumanParticipants;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlatoonCrewBuilderCoopTest extends PlatoonCrewBuilderTest
{
    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.COOP_COMPETITIVE_PROFILE);
        PWCGContext.getInstance().setCampaign(campaign);
    }

    @Test
    public void testOneOfTwoPlayerFlightGeneration() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
    	for (CrewMember player : campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
    	{
    		if (player.getName().contentEquals("Company Mate"))
    		{
    			participatingPlayers.addCrewMember(player);
    		}
    	}

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestMissionFromMissionType(participatingPlayers);

        verifyAllParticipatingPlayers(participatingPlayers, mission);
    }

    @Test
    public void testTwoPlayerFlightGeneration() throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        for (CrewMember player : campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
        {
            if (player.getCompanyId() == CompanyTestProfile.COOP_COMPETITIVE_PROFILE.getCompanyId())
            {
                participatingPlayers.addCrewMember(player);
            }
        }

        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeTestMissionFromMissionType(participatingPlayers);

        verifyAllParticipatingPlayers(participatingPlayers, mission);
    }
    

    @Test
    public void testTwoPlayerEnemyCompanyFlightGeneration() throws PWCGException
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

}
