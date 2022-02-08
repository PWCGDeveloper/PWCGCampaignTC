package pwcg.aar.inmission.prelim;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.aar.prelim.claims.AARClaimPanelData;
import pwcg.aar.prelim.claims.AARClaimPanelEventTabulator;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.data.MissionHeader;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
public class AARClaimPanelEventTabulatorTest
{
    @Mock private Campaign campaign;
    @Mock private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    @Mock private AARPreliminaryData aarPreliminarytData;
    @Mock private PwcgMissionData pwcgMissionData;
    @Mock private MissionHeader missionHeader;

    private Map<Integer, PwcgGeneratedMissionVehicleData> missionTanks  = new HashMap<>();    
    private Date campaignDate;
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        campaignDate = DateUtils.getDateYYYYMMDD("19420801");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        
        Mockito.when(aarPreliminarytData.getPwcgMissionData()).thenReturn(pwcgMissionData);

        PwcgGeneratedMissionVehicleData alliedTank1 = new PwcgGeneratedMissionVehicleData();
        alliedTank1.setVehicleType("t34-76stz");
        alliedTank1.setCompanyId(CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId());
        alliedTank1.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);

        PwcgGeneratedMissionVehicleData alliedTank2 = new PwcgGeneratedMissionVehicleData();
        alliedTank2.setVehicleType("t34-76stz");
        alliedTank2.setCompanyId(CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId());
        alliedTank2.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);

        PwcgGeneratedMissionVehicleData alliedTank3 = new PwcgGeneratedMissionVehicleData();
        alliedTank3.setVehicleType("kv1-s");
        alliedTank3.setCompanyId(CompanyTestProfile.TANK_DIVISION_146_PROFILE.getCompanyId());
        alliedTank3.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 3);

        missionTanks.put(alliedTank1.getCrewMemberSerialNumber(), alliedTank1);
        missionTanks.put(alliedTank2.getCrewMemberSerialNumber(), alliedTank2);
        missionTanks.put(alliedTank3.getCrewMemberSerialNumber(), alliedTank3);
       
        PwcgGeneratedMissionVehicleData axisTank = new PwcgGeneratedMissionVehicleData();
        axisTank.setVehicleType("pziiif4");
        axisTank.setCompanyId(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        axisTank.setCrewMemberSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 4);

        missionTanks.put(axisTank.getCrewMemberSerialNumber(), axisTank);

        Mockito.when(pwcgMissionData.getMissionPlayerTanks()).thenReturn(missionTanks);
    }
    
    @Test
    public void germanMission () throws PWCGException
    {             
        AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData, Side.AXIS);
        AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
        Assertions.assertTrue (claimPanelData.getEnemyTankTypesInMission().size() == 2);
        
    }
    
    @Test
    public void russianMission () throws PWCGException
    {             
        AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData, Side.ALLIED);
        AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
        Assertions.assertTrue (claimPanelData.getEnemyTankTypesInMission().size() == 1);
        
    }
}
