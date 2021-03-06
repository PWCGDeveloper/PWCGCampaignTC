package pwcg.aar.inmission.phase2.logeval;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType12;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AARVehicleBuilderTest
{

    @Mock
    private Campaign campaign;

    @Mock
    private CampaignPersonnelManager campaignPersonnelManager;

    @Mock
    PwcgMissionData pwcgMissionData;
    
    @Mock
    private CrewMember crewMember;
    
    @Mock
    private LogEventData logEventData;

    @Mock
    private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    
    @Mock
    private CompanyPersonnel companyPersonnel;

    private TestMissionEntityGenerator testMissionEntityGenerator;

    public AARVehicleBuilderTest() throws PWCGException
    {
        
    }

    @Test
    public void testSortVehicles () throws PWCGException
    {        
        runTest(2, 1, 3, 4);
        runTest(0, 1, 3, 4);
        runTest(2, 0, 3, 4);
        runTest(2, 1, 3, 4);
        runTest(2, 1, 3, 4);
        runTest(2, 1, 3, 4);
        runTest(2, 1, 0, 4);
        runTest(2, 1, 3, 0);
        runTest(2, 1, 0, 0);
    }

    @Test
    public void testSortVehiclesNoTanks () throws PWCGException
    {        
        assertThrows(PWCGException.class, () -> 
        {
            runTest(0, 0, 3, 4);
        });
    }

    public void runTest(int numRussianPlanes, int numGermanPlanes,
                    int numRussianTrucks, int numGermanTrucks) throws PWCGException
    {
        Mockito.when(campaign.getPersonnelManager()).thenReturn(campaignPersonnelManager);
        Mockito.when(campaignPersonnelManager.getAnyCampaignMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);        
        Mockito.when(campaignPersonnelManager.getCompanyPersonnel(ArgumentMatchers.<Integer>any())).thenReturn(companyPersonnel);
        Mockito.when(companyPersonnel.getCrewMember(ArgumentMatchers.<Integer>any())).thenReturn(crewMember);
        
        Mockito.when(crewMember.isPlayer()).thenReturn(false);

        testMissionEntityGenerator = new TestMissionEntityGenerator();
        testMissionEntityGenerator.makeMissionArtifacts(numRussianPlanes, numGermanPlanes, numRussianTrucks, numGermanTrucks);
        
        List<IAType12> vehiclesSpawnEvents = testMissionEntityGenerator.getVehicles();
        Mockito.when(logEventData.getVehicles()).thenReturn(vehiclesSpawnEvents);
        
        AARVehicleBuilder aarVehicleBuilder = createAARVehicleBuilder(); 
        aarVehicleBuilder.buildVehicleListsByVehicleType(logEventData);
        
        assert(aarVehicleBuilder.getLogTanks().size() == (numRussianPlanes + numGermanPlanes));
        assert(aarVehicleBuilder.getLogGroundUNits().size() == (numRussianTrucks + numGermanTrucks));
    }
    
    
    private AARVehicleBuilder createAARVehicleBuilder() throws PWCGException
    {
        AARBotVehicleMapper botPlaneMapper = new AARBotVehicleMapper(logEventData);

        Mockito.when(pwcgMissionDataEvaluator.wasCrewMemberAssignedToMissionByName("Russian CrewMemberA")).thenReturn(true);
        Mockito.when(pwcgMissionDataEvaluator.wasCrewMemberAssignedToMissionByName("Russian CrewMemberB")).thenReturn(true);
        Mockito.when(pwcgMissionDataEvaluator.wasCrewMemberAssignedToMissionByName("German CrewMemberA")).thenReturn(true);
        Mockito.when(pwcgMissionDataEvaluator.wasCrewMemberAssignedToMissionByName("German CrewMemberB")).thenReturn(true);
        
        Mockito.when(pwcgMissionDataEvaluator.getTankForCrewMemberByName("Russian CrewMemberA")).thenReturn(testMissionEntityGenerator.getMissionTank(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1));
        Mockito.when(pwcgMissionDataEvaluator.getTankForCrewMemberByName("Russian CrewMemberB")).thenReturn(testMissionEntityGenerator.getMissionTank(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2));
        Mockito.when(pwcgMissionDataEvaluator.getTankForCrewMemberByName("German CrewMemberA")).thenReturn(testMissionEntityGenerator.getMissionTank(SerialNumber.AI_STARTING_SERIAL_NUMBER + 100));
        Mockito.when(pwcgMissionDataEvaluator.getTankForCrewMemberByName("German CrewMemberB")).thenReturn(testMissionEntityGenerator.getMissionTank(SerialNumber.AI_STARTING_SERIAL_NUMBER + 200));

        return new AARVehicleBuilder(botPlaneMapper, pwcgMissionDataEvaluator);
    }

}
