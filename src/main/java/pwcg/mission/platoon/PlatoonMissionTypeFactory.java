package pwcg.mission.platoon;

import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ICompanyMission;
import pwcg.mission.Mission;
import pwcg.mission.MissionRoleGenerator;
import pwcg.mission.platoon.tank.TankMcu;

public class PlatoonMissionTypeFactory
{
    public static PlatoonMissionType getPlatoonMissionType(Mission mission, ICompanyMission company, TankMcu tank) throws PWCGException
    {
        PwcgRole missionRole = getMissionRole(mission, company, tank);

        if (mission.getObjective().getAssaultingCountry().getCountry() == company.getCountry().getCountry())
        {
            return getMissionTypeForAssault(missionRole);
        }
        else
        {
            return getMissionTypeForDefense(missionRole);
        }
    }

    private static PwcgRole getMissionRole(Mission mission, ICompanyMission company, TankMcu tank) throws PWCGException
    {
        PwcgRole missionRole = PwcgRole.ROLE_NONE;
        if (company.getCompanyRoles() != null)
        {
            missionRole = MissionRoleGenerator.getMissionRole(mission.getCampaign(), company);
        }
        
        if (missionRole == PwcgRole.ROLE_NONE)
        {
            missionRole = tank.getRoleCategories().get(0).getDefaultRole();
        }
        
        if (missionRole == PwcgRole.ROLE_NONE)
        {
            missionRole = PwcgRole.ROLE_MAIN_BATTLE_TANK;
        }
        return missionRole;
    }

    private static PlatoonMissionType getMissionTypeForAssault(PwcgRole missionRole)
    {
        if (missionRole == PwcgRole.ROLE_MAIN_BATTLE_TANK)
        {
            return PlatoonMissionType.ASSAULT;
        }
        else if (missionRole == PwcgRole.ROLE_TANK_DESTROYER)
        {
            return PlatoonMissionType.ASSAULT;
        }
        else if (missionRole == PwcgRole.ROLE_SELF_PROPELLED_GUN)
        {
            return PlatoonMissionType.INFANTRY_SUPPORT;
        }
        else if (missionRole == PwcgRole.ROLE_SELF_PROPELLED_GUN)
        {
            return PlatoonMissionType.AAA;
        }
        else
        {
            return PlatoonMissionType.ASSAULT;
        }
    }

    private static PlatoonMissionType getMissionTypeForDefense(PwcgRole missionRole)
    {
        if (missionRole == PwcgRole.ROLE_MAIN_BATTLE_TANK)
        {
            return PlatoonMissionType.COUNTER_ATTACK;
        }
        else if (missionRole == PwcgRole.ROLE_SELF_PROPELLED_GUN)
        {
            return PlatoonMissionType.AAA;
        }
        else
        {
            return PlatoonMissionType.DEFENSE;
        }
    }
}
