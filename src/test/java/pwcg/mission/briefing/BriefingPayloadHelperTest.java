package pwcg.mission.briefing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.tank.TankAttributeMapping;
import pwcg.campaign.tank.payload.ITankPayload;
import pwcg.campaign.tank.payload.TankPayloadDesignation;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.BriefingPayloadHelper;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BriefingPayloadHelperTest extends BriefingDataInitializerTest
{

    @Mock private ITankPayload payload1;
    @Mock private ITankPayload payload2;
    @Mock private ITankPayload payload3;
    @Mock private ITankPayload payload4;
    @Mock private TankPayloadDesignation payloadDesignation1;
    @Mock private TankPayloadDesignation payloadDesignation2;
    @Mock private TankPayloadDesignation payloadDesignation3;
    @Mock private TankPayloadDesignation payloadDesignation4;
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        super.setupTest();
        super.initializePayloadsFromMissionTest();

        Mockito.when(payload1.getSelectedPayloadDesignation()).thenReturn(payloadDesignation1);
        Mockito.when(payload2.getSelectedPayloadDesignation()).thenReturn(payloadDesignation2);
        Mockito.when(payload3.getSelectedPayloadDesignation()).thenReturn(payloadDesignation3);
        Mockito.when(payload4.getSelectedPayloadDesignation()).thenReturn(payloadDesignation4);

        Mockito.when(payloadDesignation1.getPayloadId()).thenReturn(2);
        Mockito.when(payloadDesignation2.getPayloadId()).thenReturn(2);
        Mockito.when(payloadDesignation3.getPayloadId()).thenReturn(2);
        Mockito.when(payloadDesignation4.getPayloadId()).thenReturn(2);
        
        Mockito.when(tank1.getTankPayload()).thenReturn(payload1);
        Mockito.when(tank2.getTankPayload()).thenReturn(payload2);
        Mockito.when(tank3.getTankPayload()).thenReturn(payload3);
        Mockito.when(tank4.getTankPayload()).thenReturn(payload4);
    }
    

    @Test
    public void initializePayloadsFromMissionTest () throws PWCGException
    {             
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        
        assert(briefingAssignmentData.getCrews().size() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);        
    }

    @Test
    public void modifyPayloadTest () throws PWCGException
    {             
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        payloadHelper.modifyPayload(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, 1);
        
        assert(briefingAssignmentData.getCrews().size() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 1);
    }

    @Test
    public void modifyPayloadPlaneNotFoundTest () throws PWCGException
    {             
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        payloadHelper.modifyPayload(SerialNumber.AI_STARTING_SERIAL_NUMBER+3, 1);
        
        assert(briefingAssignmentData.getCrews().size() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);
    }

    @Test
    public void setPayloadForAddedSameTankTypeTest () throws PWCGException
    {       
        Mockito.when(equippedTank1.getType()).thenReturn(TankAttributeMapping.PZKW_III_L.getTankType());
        Mockito.when(equippedTank2.getType()).thenReturn(TankAttributeMapping.PZKW_III_L.getTankType());
        Mockito.when(tank1.getType()).thenReturn(TankAttributeMapping.PZKW_III_L.getTankType());
        Mockito.when(tank2.getType()).thenReturn(TankAttributeMapping.PZKW_III_L.getTankType());

        Mockito.when(crewMember1.determineSortKey(ArgumentMatchers.any())).thenReturn("A");
        Mockito.when(crewMember2.determineSortKey(ArgumentMatchers.any())).thenReturn("B");

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);

        payloadHelper.setPayloadForChangedPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        
        assert(briefingAssignmentData.getCrews().size() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 2);
    }

    @Test
    public void setPayloadForAddedDifferentTankTypeTest () throws PWCGException
    {       
        Mockito.when(equippedTank1.getType()).thenReturn(TankAttributeMapping.PZKW_III_L.getTankType());
        Mockito.when(equippedTank2.getType()).thenReturn(TankAttributeMapping.PZKW_III_M.getTankType());
        Mockito.when(tank1.getType()).thenReturn(TankAttributeMapping.PZKW_III_L.getTankType());
        Mockito.when(tank2.getType()).thenReturn(TankAttributeMapping.PZKW_III_M.getTankType());

        Mockito.when(crewMember1.determineSortKey(ArgumentMatchers.any())).thenReturn("A");
        Mockito.when(crewMember2.determineSortKey(ArgumentMatchers.any())).thenReturn("B");
        Mockito.when(crewMember3.determineSortKey(ArgumentMatchers.any())).thenReturn("C");
        Mockito.when(crewMember4.determineSortKey(ArgumentMatchers.any())).thenReturn("D");

        Mockito.when(payloadDesignation1.getPayloadId()).thenReturn(2);
        Mockito.when(payloadDesignation2.getPayloadId()).thenReturn(1);
        Mockito.when(payloadDesignation3.getPayloadId()).thenReturn(2);
        Mockito.when(payloadDesignation4.getPayloadId()).thenReturn(1);

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 1);

        payloadHelper.setPayloadForChangedPlane(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        assert(briefingAssignmentData.getCrews().size() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getPayloadId() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getPayloadId() == 0);
    }
}
