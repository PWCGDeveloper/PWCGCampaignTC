package pwcg.gui.rofmap.brief;

import pwcg.campaign.tank.payload.ITankPayload;
import pwcg.campaign.tank.payload.TankPayloadElement;
import pwcg.campaign.tank.payload.TankPayloadFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.rofmap.brief.model.BriefingCrewMemberAssignmentData;
import pwcg.mission.Mission;
import pwcg.mission.playerunit.crew.CrewTankPayloadPairing;
import pwcg.mission.unit.ITankPlatoon;
import pwcg.mission.unit.TankMcu;

public class BriefingPayloadHelper
{
    private Mission mission;
    private BriefingCrewMemberAssignmentData briefingAssignmentData = new BriefingCrewMemberAssignmentData();
    
	public BriefingPayloadHelper(Mission mission, BriefingCrewMemberAssignmentData briefingAssignmentData)
	{
        this.mission = mission;
        this.briefingAssignmentData = briefingAssignmentData;
	}

    public void initializePayloadsFromMission() throws PWCGException
    {
        assignPayloadsToCrewPlanes();
        assignModificationsToCrewPlanes();
    }

    public void modifyPayload(Integer crewMemberSerialNumber, int payloadId) 
    {
        try
        {
            CrewTankPayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
            if (crewPlane != null)
            {
                crewPlane.setPayloadId(payloadId);
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }

    public void setPayloadForAddedPlane(Integer crewMemberSerialNumber) throws PWCGException
    {
        CrewTankPayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
        executePayloadAssignmentSequence(crewPlane);
    }

    public void setPayloadForChangedPlane(Integer crewMemberSerialNumber) throws PWCGException
    {
        CrewTankPayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByCrewMember(crewMemberSerialNumber);
        executePayloadAssignmentSequence(crewPlane);
    }

    private void executePayloadAssignmentSequence(CrewTankPayloadPairing crewPlane) throws PWCGException
    {
        if (setPayloadToSimilarPlane(crewPlane))
        {
            return;
        }
        else
        {
            setPayloadFromPayloadFactory(crewPlane);
        }
    }

    private void assignPayloadsToCrewPlanes() throws PWCGException
    {
        ITankPlatoon playerPlatoon = mission.getPlatoons().getPlayerUnitForCompany(briefingAssignmentData.getCompany().getCompanyId());
        for (TankMcu tank : playerPlatoon.getTanks())
        {
            CrewTankPayloadPairing crewTank = briefingAssignmentData.findAssignedCrewPairingByPlane(tank.getSerialNumber());
            if (crewTank != null)
            {
                crewTank.setPayloadId(tank.getTankPayload().getSelectedPayloadDesignation().getPayloadId());
            }
        }
    }

    private void assignModificationsToCrewPlanes() throws PWCGException
    {
        ITankPlatoon playerPlatoon = mission.getPlatoons().getPlayerUnitForCompany(briefingAssignmentData.getCompany().getCompanyId());
        for (TankMcu tank : playerPlatoon.getTanks())
        {
            CrewTankPayloadPairing crewPlane = briefingAssignmentData.findAssignedCrewPairingByPlane(tank.getSerialNumber());
            if (crewPlane != null)
            {
            	for (TankPayloadElement modification : tank.getTankPayload().getSelectedModifications())
            	{
            		crewPlane.addModification(modification.getDescription());
            	}
            }
        }
    }

    private boolean setPayloadToSimilarPlane(CrewTankPayloadPairing crewPlane) throws PWCGException
    {
        boolean setToSimilarPlane = false;
        for (CrewTankPayloadPairing sourceCrewPlane : briefingAssignmentData.getCrews())
        {
            if (sourceCrewPlane.getTank().getType().equals(crewPlane.getTank().getType()))
            {
                if (sourceCrewPlane.getCrewMember().getSerialNumber() != crewPlane.getCrewMember().getSerialNumber())
                {
                    mapPayloadFromPlane(crewPlane, sourceCrewPlane);
                    setToSimilarPlane = true;
                    break;
                }
            }
        }
        return setToSimilarPlane;
    }

    private void mapPayloadFromPlane(CrewTankPayloadPairing targetPlane, CrewTankPayloadPairing sourcePlane)
    {
        targetPlane.setPayloadId(sourcePlane.getPayloadId());
        for (String modification : sourcePlane.getModifications())
        {
            targetPlane.addModification(modification);
        }
    }
    
    private void setPayloadFromPayloadFactory(CrewTankPayloadPairing crewPlane) throws PWCGException
    {
        ITankPlatoon playerPlatoon = mission.getPlatoons().getPlayerUnitForCompany(briefingAssignmentData.getCompany().getCompanyId());
        TankPayloadFactory payloadFactory = new TankPayloadFactory();
        ITankPayload payload = payloadFactory.createPayload(crewPlane.getTank().getType(), mission.getCampaign().getDate());
        payload.createWeaponsPayload(playerPlatoon);
        crewPlane.setPayloadId(payload.getSelectedPayloadId());
    }
}
