package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARBotVehicleMapperTest
{
    @Mock
    private Campaign campaign;

    @Mock
    private CampaignPersonnelManager campaignPersonnelManager;

    @Mock
    private CrewMember crewMember;

    @Mock
    private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    
    private TestMissionEntityGenerator testMissionEntityGenerator;
    
    public AARBotVehicleMapperTest() throws PWCGException
    {
        
    }

    @Test
    public void testMappingBotToPlaneForFighters () throws PWCGException
    {        
        testInstanceMappingBotToTank(0, 0, 3, 4);
        testInstanceMappingBotToTank(2, 1, 3, 4);
        testInstanceMappingBotToTank(0, 1, 3, 4);
        testInstanceMappingBotToTank(2, 0, 3, 4);
        testInstanceMappingBotToTank(2, 1, 3, 4);
        testInstanceMappingBotToTank(2, 1, 3, 4);
        testInstanceMappingBotToTank(2, 1, 3, 4);
        testInstanceMappingBotToTank(2, 1, 0, 4);
        testInstanceMappingBotToTank(2, 1, 3, 0);
        testInstanceMappingBotToTank(2, 1, 0, 0);
    }

    public void testInstanceMappingBotToTank(
                    int numRussianTanks, 
                    int numGermanTanks,
                    int numRussianTrucks, 
                    int numGermanTrucks) throws PWCGException
    {
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        //Mockito.when(campaignPersonnelManager.getCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);
        Mockito.when(campaignPersonnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);
        Mockito.when(campaignPersonnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);
        Mockito.when(crewMember.isPlayer()).thenReturn(false);

        testMissionEntityGenerator = new TestMissionEntityGenerator();
        testMissionEntityGenerator.makeMissionArtifacts(numRussianTanks, numGermanTanks, numRussianTrucks, numGermanTrucks);

        PwcgMissionData pwcgMissionData = new PwcgMissionData();
        List<PwcgGeneratedMissionVehicleData> missionTanks = new ArrayList<>(testMissionEntityGenerator.getMissionTanks().values());
        for (PwcgGeneratedMissionVehicleData missionTank : missionTanks)
        {
            pwcgMissionData.addMissionPlayerTanks(missionTank);
        }
        
        LogEventData logEventData = testMissionEntityGenerator.getAARLogEventData();
        AARBotVehicleMapper aarBotVehicleMapper = new AARBotVehicleMapper(logEventData);

        Map <String, LogTank> aiEntities = testMissionEntityGenerator.getTankAiEntities();
        aarBotVehicleMapper.mapBotsToCrews(aiEntities);
                
        assert(aiEntities.size() == numRussianTanks + numGermanTanks);
        for (LogTank logTank : aiEntities.values())
        {
            assert(logTank.getLogCrewMember() != null);
            if (logTank.getId().equals("1001"))
            {
                assert(logTank.getLogCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
            }
            if (logTank.getId().equals("1002"))
            {
                assert(logTank.getLogCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
            }
            if (logTank.getId().equals("2001"))
            {
                assert(logTank.getLogCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER + 100);
            }
            if (logTank.getId().equals("2002"))
            {
                assert(logTank.getLogCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER + 200);
            }
        }
    }
}
