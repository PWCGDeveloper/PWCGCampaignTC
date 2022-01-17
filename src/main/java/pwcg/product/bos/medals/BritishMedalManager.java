package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;

public class BritishMedalManager extends BoSMedalManager
{
    public static int CREWS_BADGE = 1;

    public static int MM = 2;
    public static int MC = 3;
    public static int DSM = 4;
    public static int DSC = 5;
    public static int DSO = 6;
    public static int DSO_BAR = 7;
    public static int VC = 9;

    public static int WOUND_STRIPE = 20;

    BritishMedalManager(Campaign campaign)
    {
        super(campaign);

        medals.put(CREWS_BADGE, new Medal("CrewMembers Badge",          "gb_crew_badge.png"));
        medals.put(MM, new Medal ("Military Medal",                    "gb_military_medal.png"));
        medals.put(MC, new Medal ("Military Cross",                    "gb_military_cross.png"));
        medals.put(DSM, new Medal ("Distinguished Service Medal",       "gb_distinguished_service_medal.png"));
        medals.put(DSC, new Medal ("Distinguished Service Cross",       "gb_distinguished_service_cross.png"));

        medals.put(DSO, new Medal (DISTINGUISHED_SERVICE_ORDER_NAME,                    "gb_distinguished_service_order.png"));
        medals.put(DSO_BAR, new Medal (DISTINGUISHED_SERVICE_ORDER_NAME + " With Bar",  "gb_distinguished_service_order_bar.png"));
        medals.put(VC, new Medal("Victoria Cross", "gb_victoria_cross.png"));

        medals.put(WOUND_STRIPE, new Medal("Wound Stripe", "gb_wound_chevron.png"));
    }

    protected Medal awardWings(CrewMember crewMember)
    {
        if (!hasMedal(crewMember, medals.get(CREWS_BADGE)))
        {
            return medals.get(CREWS_BADGE);
        }

        return null;
    }

    public Medal awardWoundedAward(CrewMember crewMember, ArmedService service)
    {
        return medals.get(WOUND_STRIPE);
    }

    public Medal awardTanker(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException
    {
        int tankVictories = crewMember.getCrewMemberVictories().getTankVictoryCount();
        boolean isOfficer = true;
        if(crewMember.determineRankPos(campaign.getDate()) >= 3)
        {
            isOfficer = false;
        }

        if ((tankVictories >= 2) && !hasMedal(crewMember, medals.get(MM)) && !isOfficer)
        {
            return medals.get(MM);
        }

        if ((tankVictories >= 2) && !hasMedal(crewMember, medals.get(MC)) && isOfficer)
        {
            return medals.get(MC);
        }

        if ((tankVictories >= 5) && !hasMedal(crewMember, medals.get(DSM)) && !isOfficer)
        {
            return medals.get(DSM);
        }

        if ((tankVictories >= 5) && !hasMedal(crewMember, medals.get(DSC)) && isOfficer)
        {
            return medals.get(DSC);
        }

        if ((tankVictories >= 10) && !hasMedal(crewMember, medals.get(DSO)))
        {
            return medals.get(DSO);
        }
        
        if ((tankVictories >= 10) && !hasMedal(crewMember, medals.get(DSO)))
        {
            return medals.get(DSO);
        }

        if ((tankVictories >= 15) && !hasMedal(crewMember, medals.get(DSO_BAR)))
        {
            return medals.get(DSO_BAR);            
        }

        if (!hasMedal(crewMember, medals.get(VC)))
        {
            if (tankVictories >= 30)
            {
                if (victoriesThisMission >= 2)
                {
                    return medals.get(VC);
                }
            }
            if (tankVictories >= 20)
            {
                if (victoriesThisMission >= 5)
                {
                    return medals.get(VC);
                }
            }
        }

        return null;
    }

    public Medal awardAAA(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException
    {
        int numCrewMemberGroundVictoryPoints = crewMember.getCrewMemberVictories().getGroundVictoryPointTotal();

        if (numCrewMemberGroundVictoryPoints >= 20 && !hasMedal(crewMember, medals.get(DSO)))
        {
            if (crewMember.getBattlesFought() >= 50)
            {
                return medals.get(DSO);
            }
        }

        if (numCrewMemberGroundVictoryPoints >= 50 && !hasMedal(crewMember, medals.get(DSO_BAR)))
        {
            if (crewMember.getBattlesFought() >= 70)
            {
                return medals.get(DSO_BAR);
            }
        }

        if (numCrewMemberGroundVictoryPoints >= 50 && !hasMedal(crewMember, medals.get(VC)))
        {
            if (crewMember.getBattlesFought() >= 100)
            {
                return medals.get(VC);
            }
        }

        return awardTanker(crewMember, service, victoriesThisMission);
    }
}
