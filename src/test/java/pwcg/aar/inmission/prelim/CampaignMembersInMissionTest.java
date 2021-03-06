package pwcg.aar.inmission.prelim;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.prelim.CampaignMembersInMissionFinder;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CampaignMembersInMissionTest
{    
    @Mock
    private PwcgMissionData pwcgMissionData;
    
    private Campaign campaign;
    private Map<Integer, PwcgGeneratedMissionVehicleData> missionTanks  = new HashMap<>();

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        
        campaign = CampaignCache.makeCampaign(CompanyTestProfile.TANK_DIVISION_147_PROFILE);
    }


    @Test
    public void testAceRetrieval() throws PWCGException
    {
        missionTanks.clear();
        for (int i = 0; i < 50; ++i)
        {
            PwcgGeneratedMissionVehicleData planeData = new PwcgGeneratedMissionVehicleData();
            planeData.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + (i * 2) + 1);
            missionTanks.put(planeData.getCrewMemberSerialNumber(), planeData);
        }
        
        Mockito.when(pwcgMissionData.getMissionPlayerTanks()).thenReturn(missionTanks);
        
        CampaignMembersInMissionFinder campaignMembersInMissionHandler = new CampaignMembersInMissionFinder();
        CrewMembers crewMembersInMission = campaignMembersInMissionHandler.determineCampaignMembersInMission(campaign, pwcgMissionData);

        assert(crewMembersInMission.getActiveCount(campaign.getDate()) == 50);
        
        for (CrewMember crewMember : crewMembersInMission.getCrewMemberCollection().values())
        {
            assert((crewMember.getSerialNumber() % 2) == 1);
            assert(crewMember.getSerialNumber() > SerialNumber.AI_STARTING_SERIAL_NUMBER);
            assert(crewMember.getSerialNumber() < SerialNumber.AI_STARTING_SERIAL_NUMBER + 102);
        }
    }

}
