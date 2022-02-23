package pwcg.mission.ground.builder;

import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ICompanyMission;
import pwcg.mission.Mission;
import pwcg.mission.MissionCompanyBuilder;
import pwcg.mission.ground.MissionPlatoons;
import pwcg.mission.platoon.AiPlatoon;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.mission.platoon.PlatoonInformation;
import pwcg.mission.platoon.PlatoonMissionType;
import pwcg.mission.platoon.PlatoonMissionTypeFactory;
import pwcg.mission.platoon.PlayerPlatoon;

public abstract class PlatoonBuilderBase implements IPlatoonBuilder
{
    protected Mission mission;
    protected MissionPlatoons missionPlatoons;

    public PlatoonBuilderBase(Mission mission)
    {
        this.mission = mission;
    }

    protected void buildPlatoonsForSide(Side side) throws PWCGException
    {
        List<ICompanyMission> companiesForSide = MissionCompanyBuilder.getCompaniesInMissionForSide(mission, side);
        for (ICompanyMission company : companiesForSide)
        {
            buildPlatoon(company);
        }
    }

    protected void buildPlatoon(ICompanyMission company) throws PWCGException
    {
        List<CrewMember> playerCrews = mission.getParticipatingPlayers().getParticipatingPlayersForCompany(company.getCompanyId());
        PlatoonInformation platoonInformation = new PlatoonInformation(mission, company, playerCrews);

        ITankPlatoon tankPlatoon = null;
        if (company.isPlayercompany())
        {
            tankPlatoon = new PlayerPlatoon(platoonInformation);
            tankPlatoon.createUnit();
        }
        else
        {
            tankPlatoon = new AiPlatoon(platoonInformation);
            tankPlatoon.createUnit();
        }

        PlatoonMissionType platoonMissionType = PlatoonMissionTypeFactory.getPlatoonMissionType(mission, company, tankPlatoon.getLeadVehicle());
        tankPlatoon.setPlatoonMissionType(platoonMissionType);

        missionPlatoons.addPlatoon(tankPlatoon);
    }
}
