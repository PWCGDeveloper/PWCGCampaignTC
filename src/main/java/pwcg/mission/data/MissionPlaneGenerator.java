package pwcg.mission.data;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.unit.ITankPlatoon;
import pwcg.mission.unit.TankMcu;

public class MissionPlaneGenerator
{
    private Mission mission;
    private List<PwcgGeneratedMissionVehicleData> missionPlanes = new ArrayList<>();
    
    public MissionPlaneGenerator(Mission mission)
    {
        this.mission = mission;
    }
    
    public List<PwcgGeneratedMissionVehicleData> generateMissionPlaneData() throws PWCGException
    {
        for (ITankPlatoon unit : mission.getPlatoons().getPlayerUnits())
        {
            makePlaneEntriesForUnit(unit);
        }
        
        return missionPlanes;
    }

    private void makePlaneEntriesForUnit(ITankPlatoon unit)
    {
        for (TankMcu vehicle : unit.getTanks())
        {
            makeMissionPlaneEntry(unit.getCompany(), vehicle);
        }
    }

    private void makeMissionPlaneEntry(Company company, TankMcu vehicle)
    {
        PwcgGeneratedMissionVehicleData missionPlaneData = new PwcgGeneratedMissionVehicleData();
        missionPlaneData.setCrewMemberName(vehicle.getTankCommander().getNameAndRank());
        missionPlaneData.setCrewMemberSerialNumber(vehicle.getTankCommander().getSerialNumber());
        missionPlaneData.setVehicleSerialNumber(vehicle.getSerialNumber());
        missionPlaneData.setCompanyId(company.getCompanyId());
        missionPlaneData.setVehicleType(vehicle.getType());
        
        missionPlanes.add(missionPlaneData);
    }
}
