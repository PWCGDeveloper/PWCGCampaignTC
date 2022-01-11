package pwcg.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class CrewMemberSkill 
{
    public static int CrewMemberSkillCommonVictories = 1;                // Number of victories to advance crewMember skill from novice to common
    public static int CrewMemberSkillMedCommonMissions = 10;             // Number of missions to advance crewMember skill from novice to common
    
    public static int CrewMemberSkillVeteranAirVictories = 3;            // Number of victories to advance crewMember skill from common to veteran
    public static int CrewMemberSkillVeteranMinAirVictories = 2;         // Minimum victories to advance crewMember skill from common to veteran
    public static int CrewMemberSkillVeteranMinAirMissions = 40;         // Minimum victories to advance crewMember skill from common to veteran

    public static int CrewMemberSkillAceAirVictories = 5;                // Number of victories to advance crewMember skill from veteran to ace

    public static int CrewMemberSkillCommonGroundVictoryPoints = 5;          // Number of victories to advance crewMember skill from novice to common
    public static int CrewMemberSkillVeteranGroundVictoryPoints = 25;        // Number of victories to advance crewMember skill from common to veteran
    public static int CrewMemberSkillAceGroundVictoryPoints = 50;            // Number of victories to advance crewMember skill from veteran to ace

    private Campaign campaign;
    
	public CrewMemberSkill(Campaign campaign) 
	{
		this.campaign = campaign;
	}
    
    public void advanceCrewMemberSkillForPerformance(CrewMember crewMember) throws PWCGException 
    {
        if (isAdjustSkill(crewMember))
        {
            adjustCrewMemberSkillForPerformance(crewMember);
        }
    }
    
    public void advanceCrewMemberSkillForInitialCreation(CrewMember crewMember, Company company) throws PWCGException 
    {
        if (isAdjustSkill(crewMember))
        {
            adjustCrewMemberSkillForPerformance(crewMember);
            adjustCrewMemberSkillForCompanySkill(crewMember, company);
        }
    }

    private boolean isAdjustSkill(CrewMember crewMember)
    {
        if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_KIA)
        {
            return false;
        }
        
        if (crewMember.isPlayer())
        {
            return false;
        }
        
        if (crewMember instanceof TankAce)
        {
            return false;
        }
        
        return true;
    }
	
    private void adjustCrewMemberSkillForPerformance(CrewMember crewMember) throws PWCGException
    {
        int numCrewMemberVictories = crewMember.getCrewMemberVictories().getAirToAirVictoryCount();
        int numCrewMemberGroundVictoryPoints = crewMember.getCrewMemberVictories().getGroundVictoryPointTotal();
		int numMissions = crewMember.getBattlesFought();
		
		if (numCrewMemberVictories >= CrewMemberSkillCommonVictories || numCrewMemberGroundVictoryPoints >= CrewMemberSkillCommonGroundVictoryPoints || numMissions > CrewMemberSkillMedCommonMissions)
		{
			if (crewMember.getAiSkillLevel() != AiSkillLevel.NOVICE)
			{
				crewMember.setAiSkillLevel(AiSkillLevel.COMMON);
			}
		}
		
        // Veteran crewMember skill
        if ((numCrewMemberVictories >= CrewMemberSkillVeteranAirVictories) || 
            (numCrewMemberVictories > CrewMemberSkillVeteranMinAirVictories && numMissions > CrewMemberSkillVeteranMinAirMissions) ||
            (numCrewMemberGroundVictoryPoints > CrewMemberSkillVeteranGroundVictoryPoints && numMissions > CrewMemberSkillVeteranMinAirMissions))
        {
            if (crewMember.getAiSkillLevel().lessThan(AiSkillLevel.VETERAN))
            {
                crewMember.setAiSkillLevel(AiSkillLevel.VETERAN);
            }
        }
 
        if (numCrewMemberVictories >= CrewMemberSkillAceAirVictories || numCrewMemberGroundVictoryPoints > CrewMemberSkillAceGroundVictoryPoints)
        {
            if (crewMember.getAiSkillLevel().lessThan(AiSkillLevel.ACE))
            {
                crewMember.setAiSkillLevel(AiSkillLevel.ACE);
            }
        }
    }
	
    private void adjustCrewMemberSkillForCompanySkill(CrewMember crewMember, Company company) throws PWCGException
    {
        // Adjust for company skill
        int companyAdjustmentRoll = RandomNumberGenerator.getRandom(100);

        // Adjustments for elite companys
        int companyQuality = company.determineCompanySkill(campaign.getDate());
        if (companyQuality >= 80)
        {
            // No novices in elite companys
            if (crewMember.getAiSkillLevel() == AiSkillLevel.NOVICE)
            {
                crewMember.setAiSkillLevel(AiSkillLevel.COMMON);
            }           
            // Mostly veterans
            else if (crewMember.getAiSkillLevel() == AiSkillLevel.COMMON)
            {
                if  (companyAdjustmentRoll > 50)
                {
                    crewMember.setAiSkillLevel(AiSkillLevel.VETERAN);
                }
            }
        }
        // Adjustments for quality companys
        else if (companyQuality >= 60)
        {
            // Very good companys will not have many novices
            if (crewMember.getAiSkillLevel() == AiSkillLevel.NOVICE)
            {
                if (companyAdjustmentRoll > 30)
                {
                    crewMember.setAiSkillLevel(AiSkillLevel.COMMON);
                }
            }
            // and will tend to have more veterans
            else if (crewMember.getAiSkillLevel() == AiSkillLevel.COMMON)
            {
                if (companyAdjustmentRoll > 70)
                {
                    crewMember.setAiSkillLevel(AiSkillLevel.VETERAN);
                }
            }
        }
        // Adjustments for common companys - no adjustments
        else if (companyQuality >= 45)
        {
        }
        // Adjustments for poor companys
        else 
        {
            // Very good companys will not have many veterans
            if (crewMember.getAiSkillLevel() == AiSkillLevel.VETERAN)
            {
                if (companyAdjustmentRoll < 60)
                {
                    crewMember.setAiSkillLevel(AiSkillLevel.COMMON);
                }
            }
            // and will tend to have more novices
            else if (crewMember.getAiSkillLevel() == AiSkillLevel.COMMON)
            {
                if (companyAdjustmentRoll < 40)
                {
                    crewMember.setAiSkillLevel(AiSkillLevel.NOVICE);
                }
            }
        }
    }
}
