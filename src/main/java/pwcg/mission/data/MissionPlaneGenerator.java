package pwcg.mission.data;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.unit.ITankUnit;
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
        for (ITankUnit unit : mission.getUnits().getPlayerUnits())
        {
            makePlaneEntriesForUnit(unit);
        }
        
        return missionPlanes;
    }

    private void makePlaneEntriesForUnit(ITankUnit unit)
    {
        for (TankMcu vehicle : unit.getTanks())
        {
            makeMissionPlaneEntry(unit.getCompany(), vehicle);
        }
    }

    private void makeMissionPlaneEntry(Company squadron, TankMcu vehicle)
    {
        PwcgGeneratedMissionVehicleData missionPlaneData = new PwcgGeneratedMissionVehicleData();
        missionPlaneData.setCrewMemberName(vehicle.getTankCommander().getNameAndRank());
        missionPlaneData.setCrewMemberSerialNumber(vehicle.getTankCommander().getSerialNumber());
        missionPlaneData.setVehicleSerialNumber(vehicle.getSerialNumber());
        missionPlaneData.setCompanyId(squadron.getCompanyId());
        missionPlaneData.setVehicleType(vehicle.getType());
        
        missionPlanes.add(missionPlaneData);
    }
}
