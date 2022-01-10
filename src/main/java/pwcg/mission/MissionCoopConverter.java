package pwcg.mission;

import pwcg.core.exception.PWCGException;
import pwcg.mission.options.MissionOptions;
import pwcg.mission.options.MissionType;
import pwcg.mission.unit.ITankUnit;


public class MissionCoopConverter
{

    public void convertToCoop(Mission mission) throws PWCGException 
    {
        MissionOptions missionOptions = mission.getMissionOptions();
        missionOptions.setMissionType(MissionType.COOP_MISSION);

        for (ITankUnit unit : mission.getUnits().getPlayerUnits())
        {
            unit.preparePlaneForCoop();
        }
    }
}
