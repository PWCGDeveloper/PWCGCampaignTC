package pwcg.aar.prelim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.MissionHeader;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

public class PwcgMissionData
{
    private MissionHeader missionHeader = new MissionHeader();
    private String missionDescription = "";
    private Map<Integer, PwcgGeneratedMissionVehicleData> missionPlayerTanks  = new HashMap<>();
    private List<String> tankTypesInMission = new ArrayList<>();

    public PwcgMissionData ()
    {
    }

    public MissionHeader getMissionHeader()
    {
        return missionHeader;
    }

    public void setMissionHeader(MissionHeader missionHeader)
    {
        this.missionHeader = missionHeader;
    }

    public  Map<Integer, PwcgGeneratedMissionVehicleData> getMissionPlayerTanks()
    {
        return missionPlayerTanks;
    }

    public List<String> getTankTypesInMission()
    {
        return tankTypesInMission;
    }

    public void addTankTypeInMission(String tankTypeInMission)
    {
        if(!tankTypesInMission.contains(tankTypeInMission))
        {
            tankTypesInMission.add(tankTypeInMission);
        }
    }

    public  PwcgGeneratedMissionVehicleData getMissionTank(Integer crewMemberSerialNumber)
    {
        return missionPlayerTanks.get(crewMemberSerialNumber);
    }

    public void addMissionPlayerTanks(PwcgGeneratedMissionVehicleData  missionTank) throws PWCGException
    {
        missionPlayerTanks.put(missionTank.getCrewMemberSerialNumber(), missionTank);
    }

    public String getMissionDescription()
    {
        return missionDescription;
    }

    public void setMissionDescription(String missionDescription)
    {
        this.missionDescription = missionDescription;
    }

    public void setMissionTanks(Map<Integer, PwcgGeneratedMissionVehicleData> missionTanks)
    {
        this.missionPlayerTanks = missionTanks;
    }

    public FrontMapIdentifier getMapId()
    {
        String mapName = missionHeader.getMapName();
        FrontMapIdentifier mapId = FrontMapIdentifier.getFrontMapIdentifierForName(mapName);
        return mapId;
    }
}
