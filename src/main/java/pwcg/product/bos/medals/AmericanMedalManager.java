package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;

public class AmericanMedalManager extends BoSMedalManager 
{
    public static int CREWS_BADGE = 1;

    public static int BRONZE_STAR = 2;
    public static int SILVER_STAR = 4;
    public static int DISTINGUISHED_SERVICE_CROSS = 5;
	public static int MEDAL_OF_HONOR = 6;
	
	public static int PURPLE_HEART = 20;

    AmericanMedalManager (Campaign campaign)
    {
        super(campaign);
	        
        medals.put(CREWS_BADGE, new Medal ("CrewMembers Badge",                                 "us_crew_badge.png"));
		medals.put(BRONZE_STAR, new Medal ("Bronze Star",	                                "us_bronze_star.png"));
        medals.put(SILVER_STAR, new Medal ("Silver Star",                                   "us_silver_star.png"));
        medals.put(DISTINGUISHED_SERVICE_CROSS, new Medal ("Distinguished Service Cross",   "us_distinguished_service_cross.png"));
		medals.put(MEDAL_OF_HONOR, new Medal ("Medal of Honor",							    "us_medal_of_honor.png"));
		
		medals.put(PURPLE_HEART, new Medal ("Purple Heart", 							    "us_purple_heart.png"));
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
		return medals.get(PURPLE_HEART);
	}

	public Medal awardTanker(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException 
	{
        int tankVictories = crewMember.getCrewMemberVictories().getTankVictoryCount();
        if ((tankVictories >= 3) && !hasMedal(crewMember, medals.get(BRONZE_STAR)))
        {
            return medals.get(BRONZE_STAR);
        }
        else if (tankVictories >= 10 && !hasMedal(crewMember, medals.get(SILVER_STAR)))
        {
            return medals.get(SILVER_STAR);
        }
        else if (tankVictories >= 15 && !hasMedal(crewMember, medals.get(DISTINGUISHED_SERVICE_CROSS)))
        {
            return medals.get(DISTINGUISHED_SERVICE_CROSS);
        }
		else
		{
			if (!hasMedal(crewMember, medals.get(MEDAL_OF_HONOR)))
			{
				if ((tankVictories >= 15) && (victoriesThisMission >= 3))
				{
                    return medals.get(MEDAL_OF_HONOR);
				}
				else if ((tankVictories >= 20) && (victoriesThisMission >= 2))
				{
                    return medals.get(MEDAL_OF_HONOR);
				}
                else if ((tankVictories >= 25))
                {
                    return medals.get(MEDAL_OF_HONOR);
                }
			}
		}
		
		return null;
	}

    public Medal awardAAA(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException 
    {
        int numCrewMemberGroundVictoryPoints = crewMember.getCrewMemberVictories().getGroundVictoryPointTotal();

        if (!hasMedal(crewMember, medals.get(BRONZE_STAR)))
        {
            if (crewMember.getBattlesFought() >= 20)
            {
                if (numCrewMemberGroundVictoryPoints > 10)
                {
                    return medals.get(BRONZE_STAR);
                }
            }
        }
        else if (!hasMedal(crewMember, medals.get(DISTINGUISHED_SERVICE_CROSS)))
        {
            if (crewMember.getBattlesFought() >= 30)
            {
                if (numCrewMemberGroundVictoryPoints > 20)
                {
                    return medals.get(DISTINGUISHED_SERVICE_CROSS);
                }
            }
        }
        else if (!hasMedal(crewMember, medals.get(SILVER_STAR)))
        {
            if (crewMember.getBattlesFought() >= 40)
            {
                if (numCrewMemberGroundVictoryPoints > 30)
                {
                    return medals.get(SILVER_STAR);
                }
            }
        }
        else if (!hasMedal(crewMember, medals.get(MEDAL_OF_HONOR)))
        {
            if (crewMember.getBattlesFought() >= 80)
            {
                if (numCrewMemberGroundVictoryPoints > 100)
                {
                    return medals.get(MEDAL_OF_HONOR);
                }
            }
        }

        return awardTanker(crewMember, service, victoriesThisMission);
    }
}
