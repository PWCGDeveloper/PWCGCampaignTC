package pwcg.aar.prelim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.tank.ITankTypeFactory;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;

public class PwcgMissionDataEvaluator
{
    private Campaign campaign;
    private AARPreliminaryData aarPreliminarytData;

    public PwcgMissionDataEvaluator (Campaign campaign, AARPreliminaryData aarPreliminarytData) throws PWCGException
    {
        this.campaign = campaign;
        this.aarPreliminarytData = aarPreliminarytData;
    }

    public List<String> determineAxisTankTypesInMission() throws PWCGException
    {
        return determineTankTypesForSide(Side.AXIS);
    }

    public List<String> determineAlliedTankTypesInMission() throws PWCGException
    {
        return determineTankTypesForSide(Side.ALLIED);
    }

    private List<String> determineTankTypesForSide(Side side) throws PWCGException
    {
        Set<String> uniqueTanksForSide = new HashSet<>();

        for (String tankTypeInMission : aarPreliminarytData.getPwcgMissionData().getTankTypesInMission())
        {
            ITankTypeFactory tankTypeFactory = PWCGContext.getInstance().getFullTankTypeFactory();
            TankTypeInformation tankType = tankTypeFactory.createTankTypeByAnyName(tankTypeInMission);
            if (tankType != null)
            {
                ICountry tankCountry = CountryFactory.makeCountryByCountry(tankType.getPrimaryUsedBy().get(0));
                if(tankCountry.getSide() == side)
                {
                    uniqueTanksForSide.add(tankTypeInMission);
                }
            }
        }

        return new ArrayList<String>(uniqueTanksForSide);
    }

    public PwcgGeneratedMissionVehicleData getTankForCrewMemberBySerialNumber(Integer serialNumber) throws PWCGException
    {
        return aarPreliminarytData.getPwcgMissionData().getMissionTank(serialNumber);
    }

    public PwcgGeneratedMissionVehicleData getTankForCrewMemberByName(String name) throws PWCGException
    {
        List<PwcgGeneratedMissionVehicleData> missionTanks = new ArrayList<>(aarPreliminarytData.getPwcgMissionData().getMissionPlayerTanks().values());
        for (PwcgGeneratedMissionVehicleData missionTank : missionTanks)
        {
            CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(missionTank.getCrewMemberSerialNumber());
            if (crewMember.isCrewMemberName(name))
            {
                return missionTank;
            }
        }

        return null;
    }

    public boolean wasCrewMemberAssignedToMission(Integer serialNumber) throws PWCGException
    {
        for (CrewMember crewMemberInMission : aarPreliminarytData.getCampaignMembersInMission().getCrewMemberCollection().values())
        {
            if (crewMemberInMission.getSerialNumber() == serialNumber)
            {
                return true;
            }
        }

        return false;
    }

    public boolean wasCrewMemberAssignedToMissionByName(String destroyedEntityName) throws PWCGException
    {
        if (VehicleDefinitionManager.isLocomotive(destroyedEntityName))
        {
            return false;
        }

        for (CrewMember crewMemberInMission : aarPreliminarytData.getCampaignMembersInMission().getCrewMemberCollection().values())
        {
            if (destroyedEntityName == null)
            {
                return false;
            }

            if (crewMemberInMission.getName() == null)
            {
                return false;
            }

            if (crewMemberInMission.isCrewMemberName(destroyedEntityName))
            {
                return true;
            }
        }

        return false;
    }
}
