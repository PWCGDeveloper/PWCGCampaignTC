package pwcg.mission;

import java.util.TreeMap;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;

public class MissionAirfieldBuilder
{
    private Mission mission;
    private CoordinateBox structureBorders;
    private TreeMap<String, Airfield> fieldSet = new TreeMap<>();

    public MissionAirfieldBuilder (Mission mission, CoordinateBox structureBorders)
    {
        this.mission = mission;
        this.structureBorders = structureBorders;
    }
    
    public MissionAirfields findFieldsForPatrol() throws PWCGException 
    {
        selectAirfieldsWithinMissionBoundaries();
        selectPlayerAirfields();
        
        return new MissionAirfields(fieldSet);
    }
    
    public MissionAirfields buildFieldsForPatrol() throws PWCGException 
    {
        selectAirfieldsWithinMissionBoundaries();
        selectPlayerAirfields();
        
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

    private void selectPlayerAirfields() throws PWCGException
    {
        for (CrewMember player : mission.getParticipatingPlayers().getAllParticipatingPlayers())
        {
            Company company = player.determineCompany();
            Airfield playerField = company.determineCurrentAirfieldAnyMap(mission.getCampaign().getDate());
            fieldSet.put(playerField.getName(), playerField);
        }
    }
}
