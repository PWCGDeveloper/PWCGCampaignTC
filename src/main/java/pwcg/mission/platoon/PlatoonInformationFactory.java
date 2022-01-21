package pwcg.mission.platoon;

import java.util.List;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ICompanyMission;
import pwcg.mission.Mission;

public class PlatoonInformationFactory
{
    public static PlatoonInformation buildUnitInformation(Mission mission, ICompanyMission company,List<CrewMember> playerCrewsForPlatoon) throws PWCGException
    {
        PlatoonInformation platoonInformation = new PlatoonInformation(mission, company, playerCrewsForPlatoon);
        return platoonInformation;
    }
}
