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
    private List<PwcgGeneratedMissionVehicleData> missionTanks = new ArrayList<>();
    
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
        
        return missionTanks;
    }

    private void makeTankEntriesForPlatoon(ITankPlatoon platoon)
    {
        for (TankMcu vehicle : platoon.getPlatoonTanks().getTanks())
        {
            makeMissionTankEntry(platoon.getCompany(), vehicle);
        }
    }

    private void makeMissionTankEntry(ICompanyMission iCompanyMission, TankMcu vehicle)
    {
        PwcgGeneratedMissionVehicleData missionTankData = new PwcgGeneratedMissionVehicleData();
        missionTankData.setCrewMemberName(vehicle.getTankCommander().getNameAndRank());
        missionTankData.setCrewMemberSerialNumber(vehicle.getTankCommander().getSerialNumber());
        missionTankData.setVehicleSerialNumber(vehicle.getSerialNumber());
        missionTankData.setCompanyId(iCompanyMission.getCompanyId());
        missionTankData.setVehicleType(vehicle.getType());
        
        missionTanks.add(missionTankData);
    }
}
