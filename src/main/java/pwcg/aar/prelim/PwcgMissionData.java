package pwcg.aar.prelim;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.mission.data.MissionHeader;
import pwcg.mission.data.PwcgGeneratedMissionVehicleData;

public class PwcgMissionData 
{
    private MissionHeader missionHeader = new MissionHeader();
    private String missionDescription = "";
	private Map<Integer, PwcgGeneratedMissionVehicleData> missionTanks  = new HashMap<>();

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

    public  Map<Integer, PwcgGeneratedMissionVehicleData> getMissionTanks()
    {
        return missionTanks;
    }

    public  PwcgGeneratedMissionVehicleData getMissionTank(Integer crewMemberSerialNumber)
    {
        return missionTanks.get(crewMemberSerialNumber);
    }

    public void addMissionTanks(PwcgGeneratedMissionVehicleData  missionTank) throws PWCGException
    {
        missionTanks.put(missionTank.getCrewMemberSerialNumber(), missionTank);
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
        this.missionTanks = missionTanks;
    }
    
    public FrontMapIdentifier getMapId()
    {
        String mapName = missionHeader.getMapName();
        FrontMapIdentifier mapId = FrontMapIdentifier.getFrontMapIdentifierForName(mapName);
        return mapId;
    }
 }
