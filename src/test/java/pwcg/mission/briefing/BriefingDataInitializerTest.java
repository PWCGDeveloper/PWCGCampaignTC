package pwcg.mission.briefing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.rofmap.brief.BriefingDataInitializer;
import pwcg.gui.rofmap.brief.model.BriefingCrewMemberAssignmentData;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlights;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPlanes;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BriefingDataInitializerTest
{
    @Mock protected Campaign campaign;
    @Mock protected CampaignPersonnelManager personnelManager;
    @Mock protected CampaignEquipmentManager equipmentManager;
    @Mock protected Equipment equipment;

    @Mock protected Company company;
    @Mock protected CompanyPersonnel companyPersonnel;
    @Mock protected CrewMembers crewMembers;
    @Mock protected Mission mission;
    @Mock protected MissionFlights missionFlightBuilder;
    @Mock protected IFlight flight;
    @Mock protected IFlightPlanes flightPlanes;
    @Mock protected PlaneMcu plane1;
    @Mock protected PlaneMcu plane2;
    @Mock protected PlaneMcu plane3;
    @Mock protected PlaneMcu plane4;
    @Mock protected EquippedTank equippedTank1;
    @Mock protected EquippedTank equippedTank2;
    @Mock protected EquippedTank equippedTank3;
    @Mock protected EquippedTank equippedTank4;
    @Mock protected CrewMember crewMember1;
    @Mock protected CrewMember crewMember2;
    @Mock protected CrewMember crewMember3;
    @Mock protected CrewMember crewMember4;

    protected Map<Integer, CrewMember> companyPersonnelMap = new HashMap<>();
    protected List<PlaneMcu> planesInFlight = new ArrayList<>();
    protected Map<Integer, EquippedTank> equippedTanks = new HashMap<>();
    protected BriefingCrewMemberAssignmentData briefingAssignmentData = new BriefingCrewMemberAssignmentData();

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        Mockito.when(mission.getCampaign()).thenReturn(campaign);
        Mockito.when(mission.getFlights()).thenReturn(missionFlightBuilder);

        Mockito.when(missionFlightBuilder.getPlayerFlightForCompany(Mockito.anyInt())).thenReturn(flight);

        Mockito.when(flight.getFlightPlanes()).thenReturn(flightPlanes);
        Mockito.when(flightPlanes.getPlanes()).thenReturn(planesInFlight);

        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        
        Mockito.when(campaign.getEquipmentManager()).thenReturn(equipmentManager);
        Mockito.when(equipmentManager.getEquipmentForCompany(Mockito.any())).thenReturn(equipment);
        Mockito.when(equipment.getActiveEquippedTanks()).thenReturn(equippedTanks);

        Mockito.when(personnelManager.getCompanyPersonnel(Mockito.any())).thenReturn(companyPersonnel);
        Mockito.when(companyPersonnel.getCrewMembersWithAces()).thenReturn(crewMembers);
        Mockito.when(crewMembers.getCrewMemberCollection()).thenReturn(companyPersonnelMap);

        Mockito.when(company.getCompanyId()).thenReturn(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        
        Mockito.when(crewMember1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        Mockito.when(crewMember2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
        Mockito.when(crewMember3.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+3);
        Mockito.when(crewMember4.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER+4);
        companyPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+1, crewMember1);
        companyPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+2, crewMember2);
        companyPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+3, crewMember3);
        companyPersonnelMap.put(SerialNumber.AI_STARTING_SERIAL_NUMBER+4, crewMember4);

        Mockito.when(equippedTank1.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+1);
        Mockito.when(equippedTank2.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+2);
        Mockito.when(equippedTank3.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+3);
        Mockito.when(equippedTank4.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+4);
        Mockito.when(equippedTank1.getType()).thenReturn("bf109f4");
        Mockito.when(equippedTank2.getType()).thenReturn("bf109f2");
        Mockito.when(equippedTank3.getType()).thenReturn("bf109f4");
        Mockito.when(equippedTank4.getType()).thenReturn("bf109f2");
        equippedTanks.put(equippedTank1.getSerialNumber(), equippedTank1);
        equippedTanks.put(equippedTank2.getSerialNumber(), equippedTank2);
        equippedTanks.put(equippedTank3.getSerialNumber(), equippedTank3);
        equippedTanks.put(equippedTank4.getSerialNumber(), equippedTank4);

        Mockito.when(plane1.getTankCommander()).thenReturn(crewMember1);
        Mockito.when(plane2.getTankCommander()).thenReturn(crewMember2);
        Mockito.when(plane1.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+1);
        Mockito.when(plane2.getSerialNumber()).thenReturn(SerialNumber.PLANE_STARTING_SERIAL_NUMBER+2);

        planesInFlight.add(plane1);
        planesInFlight.add(plane2);
    }

    @Test
    public void initializePayloadsFromMissionTest () throws PWCGException
    {             
        
        BriefingDataInitializer briefingDataInitializer = new BriefingDataInitializer(mission);
        briefingAssignmentData = briefingDataInitializer.initializeFromMission(company);
        
        assert(briefingAssignmentData.getCrews().size() == 2);
        assert(briefingAssignmentData.getUnassignedCrewMembers().size() == 2);
        assert(briefingAssignmentData.getUnassignedPlanes().size() == 2);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getTank().getType().equals("bf109f4"));
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getTank().getType().equals("bf109f2"));
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+1).getCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+1);
        assert(briefingAssignmentData.findAssignedCrewPairingByCrewMember(SerialNumber.AI_STARTING_SERIAL_NUMBER+2).getCrewMember().getSerialNumber() == SerialNumber.AI_STARTING_SERIAL_NUMBER+2);
    }

    public BriefingCrewMemberAssignmentData getBriefingAssignmentData()
    {
        return briefingAssignmentData;
    }
}
