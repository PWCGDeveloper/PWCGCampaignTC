package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARContext;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OutOfMissionGroundVictoryEventGeneratorTest
{
    private Campaign campaign;
    private static CrewMember selectedCrewMember;
    
    @Mock private AARContext aarContext;
    @Mock private ArmedService service;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        
        for (CrewMember crewMember : campaign.getPersonnelManager().getAllCampaignMembers().values())
        {
            if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ACTIVE && !crewMember.isPlayer())
            {
                selectedCrewMember = crewMember;
                break;
            }
        }
    }
    
    @Test
    public void testCrewMemberOutOfMissionVictoryNovice() throws PWCGException
    {
        testCrewMemberOutOfMissionVictory(AiSkillLevel.NOVICE);
    }
    
    @Test
    public void testCrewMemberOutOfMissionVictoryCommon() throws PWCGException
    {
        testCrewMemberOutOfMissionVictory(AiSkillLevel.COMMON);
    }
    
    @Test
    public void testCrewMemberOutOfMissionVictoryVeteran() throws PWCGException
    {
        testCrewMemberOutOfMissionVictory(AiSkillLevel.VETERAN);
    }
    
    @Test
    public void testCrewMemberOutOfMissionVictoryAce() throws PWCGException
    {
        testCrewMemberOutOfMissionVictory(AiSkillLevel.ACE);
    }
    
    public void testCrewMemberOutOfMissionVictory(AiSkillLevel aiSkillLevel) throws PWCGException
    {
        selectedCrewMember.setAiSkillLevel(aiSkillLevel);
        
        OutOfMissionVictoryData victoryData = new OutOfMissionVictoryData();

        int numMissionsInWar = 1000;
        for (int j = 0; j < numMissionsInWar; ++j)
        {
            OutOfMissionGroundVictoryEventGenerator victoryGenerator = new OutOfMissionGroundVictoryEventGenerator(campaign, selectedCrewMember);
            OutOfMissionVictoryData victoriesOutOMission = victoryGenerator.outOfMissionVictoriesForCrewMember();    
            victoryData.merge(victoriesOutOMission);
        }
        
        assert(victoryData.getVictoryAwardsByCrewMember().size() > 0);
        
        List<Victory> companyMemberVictories = victoryData.getVictoryAwardsByCrewMember().get(selectedCrewMember.getSerialNumber());
        assert(companyMemberVictories.size() > 0);
        
        Victory victory = companyMemberVictories.get(0);
        assert(victory.getVictor().getCrewMemberSerialNumber() == selectedCrewMember.getSerialNumber());
        assert(victory.getVictim().getCrewMemberSerialNumber() == SerialNumber.NO_SERIAL_NUMBER);
    }

}
