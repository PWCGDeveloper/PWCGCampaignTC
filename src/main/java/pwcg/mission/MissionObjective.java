package pwcg.mission;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;

public class MissionObjective
{
    private MissionObjectiveType type;
    private String objectiveDescription;
    private Coordinate objectiveLocation;
    private ICountry assaultingCountry = null;
    private ICountry defendingCountry = null;

    public MissionObjective(Block block)
    {
        type = MissionObjectiveType.RAILROAD_STATION;
        objectiveDescription = block.getName(); 
        objectiveLocation = block.getPosition().copy(); 
    }

    public MissionObjective(PWCGLocation town)
    {
        type = MissionObjectiveType.TOWN;
        objectiveDescription = town.getName(); 
        objectiveLocation = town.getPosition().copy(); 
    }

    public MissionObjective(Airfield airfield)
    {
        type = MissionObjectiveType.AIRFIELD;
        objectiveDescription = airfield.getName(); 
        objectiveLocation = airfield.getPosition().copy(); 
    }

    public MissionObjective(Bridge bridge)
    {
        type = MissionObjectiveType.BRIDGE;
        objectiveDescription = bridge.getName(); 
        objectiveLocation = bridge.getPosition().copy(); 
    }

    public MissionObjectiveType getType()
    {
        return type;
    }

    public String getObjectiveDescription()
    {
        return objectiveDescription;
    }

    public Coordinate getPosition()
    {
        return objectiveLocation;
    }

    public ICountry getAssaultingCountry()
    {
        return assaultingCountry;
    }

    public void setAssaultingCountry(ICountry assaultingCountry)
    {
        this.assaultingCountry = assaultingCountry;
    }

    public ICountry getDefendingCountry()
    {
        return defendingCountry;
    }

    public void setDefendingCountry(ICountry defendingCountry)
    {
        this.defendingCountry = defendingCountry;
    }    
}
