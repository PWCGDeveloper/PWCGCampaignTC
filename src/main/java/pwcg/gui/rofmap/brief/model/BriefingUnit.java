package pwcg.gui.rofmap.brief.model;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberSorter;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankSorter;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.BriefingDataInitializer;
import pwcg.gui.rofmap.brief.BriefingPayloadHelper;
import pwcg.mission.Mission;
import pwcg.mission.playerunit.crew.CrewTankPayloadPairing;
import pwcg.mission.unit.ITankUnit;

public class BriefingUnit
{
    private int companyId;
    private BriefingUnitParameters briefingUnitParameters;
    private BriefingCrewMemberAssignmentData briefingAssignmentData;
    private double selectedFuel = 1.0;
    private Mission mission;

    public BriefingUnit(Mission mission, BriefingUnitParameters briefingUnitParameters, int companyId)
    {
        this.mission = mission;
        this.companyId = companyId;
        this.briefingUnitParameters = briefingUnitParameters;
        briefingAssignmentData = new BriefingCrewMemberAssignmentData();
    }
    
    public void initializeFromMission(Company company) throws PWCGException
    {
        BriefingDataInitializer crewMemberHelper = new BriefingDataInitializer(mission);
        briefingAssignmentData = crewMemberHelper.initializeFromMission(company);

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.initializePayloadsFromMission();
        
        initializeFuel();
    }

    public void changeTank(Integer crewMemberSerialNumber, Integer tankSerialNumber) throws PWCGException
    {
        briefingAssignmentData.changeTank(crewMemberSerialNumber, tankSerialNumber);

        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.setPayloadForChangedPlane(crewMemberSerialNumber);
    }


    public void assignCrewMemberFromBriefing(Integer crewMemberSerialNumber) throws PWCGException
    {
        EquippedTank tankForCrewMember = this.getSortedUnassignedTanks().get(0);
        assignCrewMemberAndTankFromBriefing(crewMemberSerialNumber, tankForCrewMember.getSerialNumber());
    }

    public void assignCrewMemberAndTankFromBriefing(Integer crewMemberSerialNumber, Integer tankSerialNumber) throws PWCGException
    {
        briefingAssignmentData.assignCrewMember(crewMemberSerialNumber, tankSerialNumber);
        
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.setPayloadForAddedPlane(crewMemberSerialNumber);
    }    

    public void unassignCrewMemberFromBriefing(int crewMemberSerialNumber) throws PWCGException
    {
        briefingAssignmentData.unassignCrewMember(crewMemberSerialNumber);
    }

    public void modifyPayload(Integer crewMemberSerialNumber, int newPayload)
    {
        BriefingPayloadHelper payloadHelper = new BriefingPayloadHelper(mission, briefingAssignmentData);
        payloadHelper.modifyPayload(crewMemberSerialNumber, newPayload);
    }

    public List<CrewMember> getSortedUnassignedCrewMembers() throws PWCGException 
    {       
        return CrewMemberSorter.sortCrewMembers(mission.getCampaign(), briefingAssignmentData.getUnassignedCrewMembers());
    }

    public List<EquippedTank> getSortedUnassignedTanks() throws PWCGException 
    {       
        return TankSorter.sortEquippedTanksByGoodness(new ArrayList<EquippedTank>(briefingAssignmentData.getUnassignedPlanes().values()));
    }

    public CrewTankPayloadPairing getPairingByCrewMember(Integer crewMemberSerialNumber) throws PWCGException 
    {       
        return briefingAssignmentData.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
    }

    public Mission getMission()
    {
        return mission;
    }

    public void setMission(Mission mission)
    {
        this.mission = mission;
    }

    public BriefingUnitParameters getBriefingUnitParameters()
    {
        return briefingUnitParameters;
    }

    public BriefingCrewMemberAssignmentData getBriefingAssignmentData()
    {
        return briefingAssignmentData;
    }

    public List<CrewTankPayloadPairing> getCrews()
    {
        return briefingAssignmentData.getCrews();
    }

    public void moveCrewMemberUp(int crewMemberSerialNumber)
    {
        briefingAssignmentData.moveCrewMemberUp(crewMemberSerialNumber);        
    }

    public void moveCrewMemberDown(int crewMemberSerialNumber)
    {
        briefingAssignmentData.moveCrewMemberDown(crewMemberSerialNumber);        
    }

    public double getSelectedFuel()
    {
        return selectedFuel;
    }

    public void setSelectedFuel(double selectedFuel)
    {
        this.selectedFuel = selectedFuel;
    }

    public int getCompanyId()
    {
        return companyId;
    }
    
    private void initializeFuel() throws PWCGException
    {
        ITankUnit unit = mission.getUnits().getPlayerUnitForCompany(companyId);
        this.selectedFuel = unit.getLeadVehicle().getFuel();
    }
}
