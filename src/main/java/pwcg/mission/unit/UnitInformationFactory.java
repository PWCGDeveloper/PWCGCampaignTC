package pwcg.mission.unit;

import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.unit.build.UnitObjectiveDefinitionBuilder;

public class UnitInformationFactory
{
    public static UnitInformation buildUnitInformation(Mission mission, Company playerCompany, MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        UnitObjectiveDefinition objective = getObjective(mission, playerCompany);
        List<CrewMember> crewMembers = getCrewmembers(playerCompany, participatingPlayers);
        
        UnitInformation unitInformation = new UnitInformation(mission, playerCompany, crewMembers, objective);
        return unitInformation;
    }

    private static UnitObjectiveDefinition getObjective(Mission mission, Company playerCompany) throws PWCGException
    {
        UnitObjectiveDefinitionBuilder objectiveBuilder = new UnitObjectiveDefinitionBuilder(mission, playerCompany);
        UnitObjectiveDefinition objective = objectiveBuilder.buildUnitObjective();
        return objective;
    }

    private static List<CrewMember> getCrewmembers(Company playerCompany, MissionHumanParticipants participatingPlayers)
    {
        return participatingPlayers.getParticipatingPlayersForCompany(playerCompany.getCompanyId());
    }

}
