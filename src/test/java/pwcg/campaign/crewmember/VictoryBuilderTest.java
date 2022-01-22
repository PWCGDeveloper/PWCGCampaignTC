package pwcg.campaign.crewmember;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogGroundUnit;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.TestIdentifiers;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VictoryBuilderTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private ConfigManagerCampaign configManager;
    
    @Mock
    private CampaignPersonnelManager personnelManager;
    
    @Mock
    private CrewMember victor;
    
    @Mock
    private CrewMember victim;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);

        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.DetailedVictoryDescriptionKey)).thenReturn(1);
        
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(victor);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2)).thenReturn(victim);
        
        Mockito.when(victor.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        Mockito.when(victim.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
        
        Mockito.when(victor.getNameAndRank()).thenReturn("Ofw Hans Schmidt");
        Mockito.when(victim.getNameAndRank()).thenReturn("Szt Ivan Ivanov");
    }
    
    @Test
    public void buildVictoryTankTank () throws PWCGException
    {
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.DetailedVictoryDescriptionKey)).thenReturn(0);

        LogTank logVictor = new LogTank(1);
        logVictor.setName(victor.getNameAndRank());
        logVictor.setCrewMemberSerialNumber(victor.getSerialNumber());
        logVictor.setVehicleType("pziii-l");
        logVictor.setCompanyId(TestIdentifiers.TEST_GERMAN_COMPANY_ID);
        logVictor.intializeCrewMember(victor.getSerialNumber());
        logVictor.getLogCrewMember().setStatus(CrewMemberStatus.STATUS_ACTIVE);

        LogTank logVictim = new LogTank(2);
        logVictim.setName(victim.getNameAndRank());
        logVictim.setCrewMemberSerialNumber(victim.getSerialNumber());
        logVictim.setVehicleType("kv1s");
        logVictim.setCompanyId(-1);
        logVictim.intializeCrewMember(victim.getSerialNumber());
        logVictim.getLogCrewMember().setStatus(CrewMemberStatus.STATUS_CAPTURED);

        LogVictory logVictory = new LogVictory(10);
        logVictory.setLocation(new Coordinate (100000, 0, 100000));
        logVictory.setVictim(logVictim);
        logVictory.setVictor(logVictor);

        VictoryBuilder victoryBuilder = new VictoryBuilder(campaign);
        Victory victory = victoryBuilder.buildVictory(DateUtils.getDateYYYYMMDD("19421103"), logVictory);
        
        Assertions.assertTrue (victory.getVictor().getCrewMemberName().equals(victor.getNameAndRank()));
        Assertions.assertTrue (victory.getVictim().getCrewMemberName().equals(victim.getNameAndRank()));
        
        VictoryDescription victoryDescription = new VictoryDescription(campaign, victory);
        String victoryDescriptionText = victoryDescription.createVictoryDescription();
        
        String verificationSegment =  "A KV1 was destroyed by Ofw Hans Schmidt";

        assert(victoryDescriptionText.contains(verificationSegment));
    }

    @Test
    public void buildVictoryTankGround () throws PWCGException
    {
        LogTank logVictor = new LogTank(1);
        logVictor.setName(victor.getNameAndRank());
        logVictor.setCrewMemberSerialNumber(victor.getSerialNumber());
        logVictor.setVehicleType("pziii-l");
        logVictor.setCompanyId(TestIdentifiers.TEST_GERMAN_COMPANY_ID);
        logVictor.intializeCrewMember(victor.getSerialNumber());
        logVictor.getLogCrewMember().setStatus(CrewMemberStatus.STATUS_ACTIVE);

        LogGroundUnit logVictim = new LogGroundUnit(1000);
        logVictim.setName(victim.getNameAndRank());
        logVictim.setVehicleType("gaz-aa");

        LogVictory logVictory = new LogVictory(10);
        logVictory.setLocation(new Coordinate (100000, 0, 100000));
        logVictory.setVictim(logVictim);
        logVictory.setVictor(logVictor);

        VictoryBuilder victoryBuilder = new VictoryBuilder(campaign);
        Victory victory = victoryBuilder.buildVictory(DateUtils.getDateYYYYMMDD("19421103"), logVictory);
        
        Assertions.assertTrue (victory.getVictor().getCrewMemberName().equals(victor.getNameAndRank()));
        Assertions.assertTrue (victory.getVictim().getType().equals("gaz-aa"));
        
        VictoryDescription victoryDescription = new VictoryDescription(campaign, victory);
        String victoryDescriptionText = victoryDescription.createVictoryDescription();
        
        String verificationSegment=  "A truck was destroyed by Ofw Hans Schmidt";

        assert(victoryDescriptionText.contains(verificationSegment));
    }
}
