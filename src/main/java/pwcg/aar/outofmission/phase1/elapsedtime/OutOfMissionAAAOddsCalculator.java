package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyRoleSet;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRole;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.TCServiceManager;

public class OutOfMissionAAAOddsCalculator
{
    private Campaign campaign;
    
    public OutOfMissionAAAOddsCalculator (Campaign campaign)
    {
        this.campaign = campaign;
    }
    

    public int oddsShotDownByAAA(CrewMember crewMember) throws PWCGException
    {
        int shotDownOdds = intitialOddsBasedOnCompanyRole(crewMember);
        shotDownOdds = companyMemberDeathOdds(crewMember, shotDownOdds);
        return shotDownOdds;
    }
    
    private int intitialOddsBasedOnCompanyRole(CrewMember crewMember) throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(crewMember.getCompanyId());
        CompanyRoleSet companyRoles = company.getCompanyRoles();
        PwcgRole roleThisMission = companyRoles.selectRoleForMission(campaign.getDate());
        
        int shotDownOdds = 5;
        if (roleThisMission.isRoleCategory(PwcgRoleCategory.ATTACK))
        {
            shotDownOdds += 10;
        }
        if (roleThisMission.isRoleCategory(PwcgRoleCategory.BOMBER))
        {
            shotDownOdds += 7;
        }
        if (roleThisMission.isRoleCategory(PwcgRoleCategory.FIGHTER))
        {
            shotDownOdds -= 10;
        }
        if (roleThisMission.isRoleCategory(PwcgRoleCategory.TRANSPORT))
        {
            shotDownOdds -= 15;
        }
        
        return shotDownOdds;
    }
    
    private int companyMemberDeathOdds(CrewMember crewMember, int shotDownOdds) throws PWCGException
    {
        if (crewMember.getAiSkillLevel() == AiSkillLevel.NOVICE)
        {
        	shotDownOdds += 17;
        }
        else if (crewMember.getAiSkillLevel() == AiSkillLevel.COMMON)
        {
        	shotDownOdds += 8;
        }
        else if (crewMember.getAiSkillLevel() == AiSkillLevel.VETERAN)
        {
            shotDownOdds -= 7;
        }
        else if (crewMember.getAiSkillLevel() == AiSkillLevel.ACE)
        {
            shotDownOdds -= 20;
        }
        
        ArmedService service = crewMember.determineService(campaign.getDate());
        if (service.getServiceId() == TCServiceManager.SSV)
        {
            shotDownOdds += 20;
        }

        return shotDownOdds;
    }
}
