package pwcg.mission;

import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;

public class MissionAirfieldBuilder
{
    private CoordinateBox structureBorders;
    private TreeMap<String, Airfield> fieldSet = new TreeMap<>();

    public MissionAirfieldBuilder (CoordinateBox structureBorders)
    {
        this.structureBorders = structureBorders;
    }
    
    public MissionAirfields findFieldsForPatrol() throws PWCGException 
    {
        selectAirfieldsWithinMissionBoundaries();
        
        return new MissionAirfields(fieldSet);
    }
    
    public MissionAirfields buildFieldsForPatrol() throws PWCGException 
    {
        selectAirfieldsWithinMissionBoundaries();        
        return new MissionAirfields(fieldSet);
    }

    private void selectAirfieldsWithinMissionBoundaries() throws PWCGException
    {
        for (Airfield airfield :  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAllAirfields().values())
        {
            if (structureBorders.isInBox(airfield.getPosition()))
            {
                fieldSet.put(airfield.getName(), airfield);
            }
        }
    }
}
