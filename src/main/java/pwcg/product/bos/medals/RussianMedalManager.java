package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;

public class RussianMedalManager extends BoSMedalManager 
{
    public static int CREWS_BADGE = 1;    
    public static int ORDER_RED_STAR = 2;
    public static int ORDER_OF_GLORY = 3;
    public static int ORDER_PATRIOTIC_WAR_2 = 4;
    public static int ORDER_PATRIOTIC_WAR_1 = 5;
    public static int ORDER_RED_BANNER = 6;
    public static int ORDER_ALEXANDER_NEVSKY = 7;
    public static int HERO_SOVIET_UNION = 8;
        
	public static int WOUND_STRIPE = 99;

	public RussianMedalManager (Campaign campaign)
    {
        super(campaign);

        medals.put(CREWS_BADGE, new Medal ("CrewMembers Badge",                                   "ussr_crewMember_badge.png"));
		medals.put(WOUND_STRIPE, new Medal ("Wound Stripe", 		                          "ussr_wound_stripe.png"));

        medals.put(ORDER_RED_STAR, new Medal ("Order of the Red Star",                        "ussr_order_red_star.png"));
        medals.put(ORDER_OF_GLORY, new Medal ("Order of Glory",                               "ussr_order_of_glory.png"));
		medals.put(ORDER_PATRIOTIC_WAR_2, new Medal ("Order of the Patriotic War 2nd Class",  "ussr_order_patriotic_war_2nd_class.png"));
        medals.put(ORDER_PATRIOTIC_WAR_1, new Medal ("Order of the Patriotic War 1st Class",  "ussr_order_patriotic_war_1st_class.png"));
        medals.put(ORDER_RED_BANNER, new Medal ("Order of the Red Banner",                    "ussr_order_red_banner.png"));
        medals.put(ORDER_ALEXANDER_NEVSKY, new Medal ("Order of Alexander Nevsky",            "ussr_order_alexander_nevsky.png"));
        medals.put(HERO_SOVIET_UNION, new Medal ("Hero of the Soviet Union",                  "ussr_hero_of_the_soviet_union.png"));
	} 

	public Medal awardWoundedAward(CrewMember crewMember, ArmedService service) 
	{
        return medals.get(WOUND_STRIPE);
	}

    protected Medal awardWings(CrewMember crewMember) 
    {
        if (!hasMedal(crewMember, medals.get(CREWS_BADGE)))
        {
            return medals.get(CREWS_BADGE);
        }

        return null;
    }

	protected Medal awardTanker(CrewMember crewMember, ArmedService service, int numMissionVictories) throws PWCGException 
	{
	    int tankVictories = crewMember.getCrewMemberVictories().getTankVictoryCount();
		if ((tankVictories >= 2) && !hasMedal(crewMember, medals.get(ORDER_RED_STAR)))
		{
			return medals.get(ORDER_RED_STAR);
		}
		
		if ((tankVictories >= 4) && !hasMedal(crewMember, medals.get(ORDER_OF_GLORY)))
		{
			return medals.get(ORDER_OF_GLORY);
		}
		
        if ((tankVictories >= 6) && !hasMedal(crewMember, medals.get(ORDER_PATRIOTIC_WAR_2)))
        {
            return medals.get(ORDER_PATRIOTIC_WAR_2);
        }
        
        if ((tankVictories >= 10) && !hasMedal(crewMember, medals.get(ORDER_PATRIOTIC_WAR_1)))
        {
            return medals.get(ORDER_PATRIOTIC_WAR_1);
        }
        
        if ((tankVictories >= 15) && numMissionVictories >= 2 && !hasMedal(crewMember, medals.get(ORDER_RED_BANNER)))
        {
            return medals.get(ORDER_RED_BANNER);
        }
        
        if ((tankVictories >= 50) && !hasMedal(crewMember, medals.get(ORDER_ALEXANDER_NEVSKY)))
        {
            return medals.get(ORDER_ALEXANDER_NEVSKY);
        }

        if ((tankVictories >= 20) && numMissionVictories >= 2 && !hasMedal(crewMember, medals.get(HERO_SOVIET_UNION)))
        {
            return medals.get(HERO_SOVIET_UNION);
        }
		
		return null;
	}

    protected Medal awardAAA(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException 
    {
        int numCrewMemberGroundVictoryPoints = crewMember.getCrewMemberVictories().getGroundVictoryPointTotal();
        if (!hasMedal(crewMember, medals.get(ORDER_RED_STAR)))
        {
	        if ((crewMember.getBattlesFought() >= 20))
	        {
	            return medals.get(ORDER_RED_STAR);
	        }
            if (numCrewMemberGroundVictoryPoints > 3)
            {
                return medals.get(ORDER_RED_STAR);
            }
        }
        
        if (!hasMedal(crewMember, medals.get(ORDER_OF_GLORY)))
        {
            if (numCrewMemberGroundVictoryPoints > 15)
            {
                return medals.get(ORDER_OF_GLORY);
            }
        }
        
        if (!hasMedal(crewMember, medals.get(ORDER_PATRIOTIC_WAR_2)))
        {
            if ((crewMember.getBattlesFought() >= 20) && numCrewMemberGroundVictoryPoints > 20)
            {
                return medals.get(ORDER_PATRIOTIC_WAR_2);
            }
        }
        
        if (!hasMedal(crewMember, medals.get(ORDER_PATRIOTIC_WAR_1)))
        {
            if ((crewMember.getBattlesFought() >= 30) && numCrewMemberGroundVictoryPoints > 25)
            {
                return medals.get(ORDER_PATRIOTIC_WAR_1);
            }
        }
        
        if (!hasMedal(crewMember, medals.get(ORDER_ALEXANDER_NEVSKY)))
        {
            if (crewMember.getBattlesFought() >= 150)
            {
                return medals.get(ORDER_ALEXANDER_NEVSKY);
            }
        }
        
        if (!hasMedal(crewMember, medals.get(HERO_SOVIET_UNION)))
        {
            if ((crewMember.getBattlesFought() >= 40) && numCrewMemberGroundVictoryPoints > 50)
            {
                return medals.get(HERO_SOVIET_UNION);
            }
        }
        
        return awardTanker(crewMember, service, victoriesThisMission);
    }
}
