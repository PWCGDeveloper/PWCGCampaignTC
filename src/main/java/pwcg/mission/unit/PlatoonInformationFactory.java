package pwcg.mission.unit;

import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;

public class PlatoonInformationFactory
{
    public static PlatoonInformation buildUnitInformation(Mission mission, Company playerCompany, List<CrewMember> playerCrewsForPlatoon, List<Coordinate>waypointPositions) throws PWCGException
    {
        PlatoonObjectiveDefinition objective = getObjective(mission, playerCompany, waypointPositions);
        List<CrewMember> crewMembers = getCrewmembers(playerCompany, playerCrewsForPlatoon);
        
        PlatoonInformation platoonInformation = new PlatoonInformation(mission, playerCompany, crewMembers, objective);
        return platoonInformation;
    }

    private static PlatoonObjectiveDefinition getObjective(Mission mission, Company playerCompany, List<Coordinate>waypointPositions) throws PWCGException
    {
        PlatoonMissionType platoonMissionType = PlatoonMissionType.ASSAULT;        
        PlatoonObjectiveDefinition playerPlatoonObjectiveDefinition = new PlatoonObjectiveDefinition(platoonMissionType, waypointPositions.get(0), waypointPositions.subList(1, waypointPositions.size()-1));
        return playerPlatoonObjectiveDefinition;
    }

    private static List<CrewMember> getCrewmembers(Company playerCompany, MissionHumanParticipants participatingPlayers)
    {
        return participatingPlayers.getParticipatingPlayersForCompany(playerCompany.getCompanyId());
    }

}
