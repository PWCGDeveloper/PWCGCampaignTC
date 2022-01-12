package pwcg.campaign.promotion;

import java.util.Date;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.TCServiceManager;

public class PromotionMinimumCriteria
{
    private int crewMemberRankMedMinMissions = 15;
    private int crewMemberRankHighMinMissions = 30;
    private int crewMemberRankExecMinMissions = 50;
    private int crewMemberRankCommandMinMissions = 80;

    private int crewMemberRankMedMinVictories = 1;
    private int crewMemberRankHighMinVictories = 3;
    private int crewMemberRankExecMinVictories = 7;
    private int crewMemberRankCommandMinVictories = 10;

    public void setMinimumPromotionStandards(CrewMember crewMember, Date date) throws PWCGException
    {
        int serviceId = crewMember.determineCompany().getService();
        PwcgRoleCategory roleCategory = crewMember.determineCompany().determineCompanyPrimaryRoleCategory(date);

        setMissionsCompletedForPromotion(serviceId, roleCategory);
        setVictoriesForPromotion(serviceId, roleCategory);
    }

    private void setMissionsCompletedForPromotion(int serviceId, PwcgRoleCategory roleCategory)
    {
        if (roleCategory == PwcgRoleCategory.SELF_PROPELLED_AAA)
        {
            setMissionsForAAA();
        }
        else if (serviceId == TCServiceManager.WEHRMACHT)
        {
            setMissionsForWehrmacht();
        }
        else if (serviceId == TCServiceManager.US_ARMY || serviceId == TCServiceManager.BRITISH_ARMY)
        {
            setMissionsForUSArmy();
        }
    }

    private void setVictoriesForPromotion(int serviceId, PwcgRoleCategory roleCategory)
    {
        if (roleCategory == PwcgRoleCategory.SELF_PROPELLED_AAA)
        {
            setVictoriesForAAA();
        }
        else if (serviceId == TCServiceManager.WEHRMACHT)
        {
            setVictoriesForWehrmacht();
        }
    }

    private void setMissionsForAAA()
    {
        crewMemberRankMedMinMissions = 10;
        crewMemberRankHighMinMissions = 30;
        crewMemberRankExecMinMissions = 40;
        crewMemberRankCommandMinMissions = 50;
    }

    private void setMissionsForWehrmacht()
    {
        crewMemberRankMedMinMissions = 20;
        crewMemberRankHighMinMissions = 50;
        crewMemberRankExecMinMissions = 80;
        crewMemberRankCommandMinMissions = 100;
    }

    private void setMissionsForUSArmy()
    {
        crewMemberRankMedMinMissions = 10;
        crewMemberRankHighMinMissions = 30;
        crewMemberRankExecMinMissions = 40;
        crewMemberRankCommandMinMissions = 50;
    }

    private void setVictoriesForAAA()
    {

        crewMemberRankMedMinVictories = 1;
        crewMemberRankHighMinVictories = 2;
        crewMemberRankExecMinVictories = 3;
        crewMemberRankCommandMinVictories = 4;
    }

    private void setVictoriesForWehrmacht()
    {

        crewMemberRankMedMinVictories = 1;
        crewMemberRankHighMinVictories = 5;
        crewMemberRankExecMinVictories = 10;
        crewMemberRankCommandMinVictories = 15;
    }

    public int getCrewMemberRankMedMinMissions()
    {
        return crewMemberRankMedMinMissions;
    }

    public int getCrewMemberRankHighMinMissions()
    {
        return crewMemberRankHighMinMissions;
    }

    public int getCrewMemberRankExecMinMissions()
    {
        return crewMemberRankExecMinMissions;
    }

    public int getCrewMemberRankCommandMinMissions()
    {
        return crewMemberRankCommandMinMissions;
    }

    public int getCrewMemberRankMedMinVictories()
    {
        return crewMemberRankMedMinVictories;
    }

    public int getCrewMemberRankHighMinVictories()
    {
        return crewMemberRankHighMinVictories;
    }

    public int getCrewMemberRankExecMinVictories()
    {
        return crewMemberRankExecMinVictories;
    }

    public int getCrewMemberRankCommandMinVictories()
    {
        return crewMemberRankCommandMinVictories;
    }

}
