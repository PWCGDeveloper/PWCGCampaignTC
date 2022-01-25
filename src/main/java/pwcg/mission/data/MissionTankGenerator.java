package pwcg.mission.data;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ICompanyMission;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.mission.platoon.tank.TankMcu;

public class MissionTankGenerator
{
    private Mission mission;
    private List<PwcgGeneratedMissionVehicleData> missionPlanes = new ArrayList<>();
    
    public MissionTankGenerator(Mission mission)
    {
        this.mission = mission;
    }
    
    public List<PwcgGeneratedMissionVehicleData> generateMissionTankData() throws PWCGException
    {
        for (ITankPlatoon unit : mission.getPlatoons().getPlayerPlatoons())
        {
            makeTankEntriesForPlatoon(unit);
        }
        
        return missionPlanes;
    }

    private void makeTankEntriesForPlatoon(ITankPlatoon platoon)
    {
        for (TankMcu vehicle : platoon.getTanks())
        {
            makeMissionTankEntry(platoon.getCompany(), vehicle);
        }
    }

    private void makeMissionTankEntry(ICompanyMission iCompanyMission, TankMcu vehicle)
    {
        PwcgGeneratedMissionVehicleData missionPlaneData = new PwcgGeneratedMissionVehicleData();
        missionPlaneData.setCrewMemberName(vehicle.getTankCommander().getNameAndRank());
        missionPlaneData.setCrewMemberSerialNumber(vehicle.getTankCommander().getSerialNumber());
        missionPlaneData.setVehicleSerialNumber(vehicle.getSerialNumber());
        missionPlaneData.setCompanyId(iCompanyMission.getCompanyId());
        missionPlaneData.setVehicleType(vehicle.getType());
        
        missionPlanes.add(missionPlaneData);
    }
}
