package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;

public class GermanMedalManager extends BoSMedalManager 
{
    public static int CREWS_BADGE = 1;
	public static int IRON_CROSS_2 = 2;
	public static int IRON_CROSS_1 = 3;
	public static int GERMAN_CROSS_GOLD = 4;
	public static int KNIGHTS_CROSS = 5;
	public static int KNIGHTS_CROSS_OAK_LEAVES = 6;
    public static int KNIGHTS_CROSS_SWORDS = 7;
    public static int KNIGHTS_CROSS_DIAMONDS = 8;
	
	public static int WOUND_BADGE_BLACK = 97;
	public static int WOUND_BADGE_SILVER = 98;
	public static int WOUND_BADGE_GOLD = 99;

	GermanMedalManager (Campaign campaign)
    {
        super(campaign);
		
        medals.put(CREWS_BADGE, new Medal ("CrewMembers Badge",                                     "ger_crew_badge.png"));
        medals.put(IRON_CROSS_2, new Medal ("Iron Cross 2nd Class",                                 "ger_iron_cross_second_class.png"));
		medals.put(IRON_CROSS_1, new Medal ("Iron Cross 1st Class", 					            "ger_iron_cross_first_class.png"));
		medals.put(GERMAN_CROSS_GOLD, new Medal ("German Cross Gold", 	                            "ger_german_cross_gold.png"));
		medals.put(KNIGHTS_CROSS, new Medal (KNIGHTS_CROSS_NAME, 	                                "ger_knights_cross.png"));
		medals.put(KNIGHTS_CROSS_OAK_LEAVES, new Medal (KNIGHTS_CROSS_NAME + " with Oak Leaves",    "ger_knights_cross_oak_leaves.png"));
        medals.put(KNIGHTS_CROSS_SWORDS, new Medal (KNIGHTS_CROSS_NAME + " with Swords",            "ger_knights_cross_swords.png"));
        medals.put(KNIGHTS_CROSS_DIAMONDS, new Medal (KNIGHTS_CROSS_NAME + " with Diamonds",        "ger_knights_cross_diamonds.png"));
		
		medals.put(WOUND_BADGE_BLACK, new Medal (GERMAN_WOUND_BADGE + "(Black)", 		"ger_wound_badge_black.png"));
		medals.put(WOUND_BADGE_SILVER, new Medal (GERMAN_WOUND_BADGE + "(Silver)", 	"ger_wound_badge_silver.png"));
		medals.put(WOUND_BADGE_GOLD, new Medal (GERMAN_WOUND_BADGE + "(Gold)", 		"ger_wound_badge_gold.png"));
	} 

	public Medal awardWoundedAward(CrewMember crewMember, ArmedService service) 
	{
	    if (!hasMedal(crewMember, medals.get(WOUND_BADGE_BLACK)))
	    {
	        return medals.get(WOUND_BADGE_BLACK);
	    }
	    else if (!hasMedal(crewMember, medals.get(WOUND_BADGE_SILVER)))
	    {
            return medals.get(WOUND_BADGE_SILVER);
	    }
	    else if (!hasMedal(crewMember, medals.get(WOUND_BADGE_GOLD)))
	    {
            return medals.get(WOUND_BADGE_GOLD);
	    }
        
        return null;
	}

    protected Medal awardWings(CrewMember crewMember) 
    {
        if (!hasMedal(crewMember, medals.get(CREWS_BADGE)))
        {
            return medals.get(CREWS_BADGE);
        }
        
        return null;
    }

	protected Medal awardTanker(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException 
	{
        int tankVictories = crewMember.getCrewMemberVictories().getTankVictoryCount();
		if ((tankVictories >= 2) && !hasMedal(crewMember, medals.get(IRON_CROSS_2)))
		{
			return medals.get(IRON_CROSS_2);
		}
		if ((tankVictories >= 10)  && !hasMedal(crewMember, medals.get(IRON_CROSS_1)))
		{
			return medals.get(IRON_CROSS_1);
		}
		if ((tankVictories >= 20) && !hasMedal(crewMember, medals.get(GERMAN_CROSS_GOLD)))
		{
			return medals.get(GERMAN_CROSS_GOLD);
		}
		if ((tankVictories >= 40) && !hasMedal(crewMember, medals.get(KNIGHTS_CROSS)))
		{
			return medals.get(KNIGHTS_CROSS);
		}
		if ((tankVictories >= 80) && !hasMedal(crewMember, medals.get(KNIGHTS_CROSS_OAK_LEAVES)))
		{
			return medals.get(KNIGHTS_CROSS_OAK_LEAVES);
		}
        if ((tankVictories >= 100) && !hasMedal(crewMember, medals.get(KNIGHTS_CROSS_SWORDS)))
        {
            return medals.get(KNIGHTS_CROSS_SWORDS);
        }
        if ((tankVictories >= 120) && !hasMedal(crewMember, medals.get(KNIGHTS_CROSS_DIAMONDS)))
        {
            return medals.get(KNIGHTS_CROSS_DIAMONDS);
        }
		
		return null;
	}

    protected Medal awardAAA(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException 
    {
        int numCrewMemberGroundVictoryPoints = crewMember.getCrewMemberVictories().getGroundVictoryPointTotal();

        if (!hasMedal(crewMember, medals.get(IRON_CROSS_2)))
        {
	        if ((crewMember.getBattlesFought() >= 15))
	        {
                if (numCrewMemberGroundVictoryPoints > 1)
                {
                    return medals.get(IRON_CROSS_2);
                }
	        }
        }
        
        if (!hasMedal(crewMember, medals.get(IRON_CROSS_1)))
        {
            if (crewMember.getBattlesFought() >= 40)
            {
                if (numCrewMemberGroundVictoryPoints > 5)
                {
                    return medals.get(IRON_CROSS_1);
                }
            }
        }
        
        if (!hasMedal(crewMember, medals.get(GERMAN_CROSS_GOLD)))
        {
            if (crewMember.getBattlesFought() >= 50)
            {
                if (numCrewMemberGroundVictoryPoints > 10)
                {
                    return medals.get(GERMAN_CROSS_GOLD);
                }
            }
        }
        
        if (!hasMedal(crewMember, medals.get(KNIGHTS_CROSS)))
        {
            if (crewMember.getBattlesFought() >= 60)
            {
                if (numCrewMemberGroundVictoryPoints > 25)
                {
                    return medals.get(KNIGHTS_CROSS);
                }
            }
        }
        
        return awardTanker(crewMember, service, victoriesThisMission);
    }
}
