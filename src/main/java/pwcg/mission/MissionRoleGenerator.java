package pwcg.mission;

import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;

public class MissionRoleGenerator
{

    public static PwcgRole getMissionRole(Campaign campaign, Map<Integer, PwcgRole> companyRoleOverride, Company playerCompany) throws PWCGException
    {
        PwcgRole missionRole = playerCompany.getCompanyRoles().selectRoleForMission(campaign.getDate());
        if (companyRoleOverride.containsKey(playerCompany.getCompanyId()))
        {
            missionRole = companyRoleOverride.get(playerCompany.getCompanyId());
        }
        return missionRole;
    }

    public static PwcgRole getMissionRole(Campaign campaign, Company playerCompany) throws PWCGException
    {
        PwcgRole missionRole = playerCompany.getCompanyRoles().selectRoleForMission(campaign.getDate());
        return missionRole;
    }
}
