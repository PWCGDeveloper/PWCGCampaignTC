package pwcg.campaign.personnel;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberFactory;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.RankFactory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;

public class InitialCompanyStaffer 
{
    private Campaign campaign;
    private Company company;
    private CrewMemberFactory companyMemberFactory;
    private CompanyPersonnel companyPersonnel;
    
	public InitialCompanyStaffer(Campaign campaign, Company company) 
	{
        this.campaign = campaign;
        this.company = company;
        
        companyPersonnel = new CompanyPersonnel(campaign, company);
        companyMemberFactory = new CrewMemberFactory(campaign, company, companyPersonnel);
	}

    public CompanyPersonnel generatePersonnel() throws PWCGException 
    {
        generateAICrewMembers();        
        return companyPersonnel;
    }
    
    public void addPlayerToCampaign(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        CrewMember player =  companyMemberFactory.createPlayer(generatorModel);
        addCrewMember(player);
    }

    private void addCrewMember(CrewMember crewMember) throws PWCGException
    {
        if ((companyPersonnel.getCrewMembersWithAces().getCrewMemberList().size()) < Company.COMPANY_STAFF_SIZE)
        {
            companyPersonnel.addCrewMember(crewMember);
        }
    }

	private void generateAICrewMembers() throws PWCGException 
	{	 	
		addAiCrewMembers();
		validateMissionsCompletedForInitialCrewMembers();
		setAiSkillLevel();
	}

    private void addAiCrewMembers() throws PWCGException
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        List<String> ranks = rankObj.getRanksByService(company.determineServiceForCompany(campaign.getDate()));
        
        addNumAiCrewMembersAtRank(1, 0);
        
        addNumAiCrewMembersAtRank(2, 1);
        
        if (ranks.size() == 4)
        {
            addNumAiCrewMembersAtRank(5, 2);
            addNumAiCrewMembersAtRank(8, 3);
        }
        else if (ranks.size() == 5)
        {
            addNumAiCrewMembersAtRank(4, 2);
            addNumAiCrewMembersAtRank(5, 3);
            addNumAiCrewMembersAtRank(4, 4);
        }
        else
        {
            throw new PWCGException ("Unexpected number of ranks in service: " + ranks.size());
        }
    }
    
    private void addNumAiCrewMembersAtRank(int initialNumCrewMembers, int rankPos) throws PWCGException
    {
        int refinedNumCrewMembers = refineNumCrewMembersAtRank(initialNumCrewMembers, rankPos);
        
        IRankHelper rankObj = RankFactory.createRankHelper();
        List<String> ranks = rankObj.getRanksByService(company.determineServiceForCompany(campaign.getDate()));

        for (int i = 0; i < refinedNumCrewMembers; ++i)
        {
            CrewMember crewMember =  companyMemberFactory.createInitialAICrewMember (ranks.get(rankPos));
            addCrewMember(crewMember);
        }
    }

    private int refineNumCrewMembersAtRank(int numCrewMembers, int rankPos) throws PWCGException
    {
        CrewMembers crewMembersAlreadyWithCompany = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : crewMembersAlreadyWithCompany.getCrewMemberCollection().values())
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            int companyMemberRankPos = rankObj.getRankPosByService(crewMember.getRank(), company.determineServiceForCompany(campaign.getDate()));
            if (rankPos == companyMemberRankPos)
            {
                --numCrewMembers;
            }
        }
        
        if (numCrewMembers < 0)
        {
            numCrewMembers = 0;
        }
        
        return numCrewMembers;
    }

    private void validateMissionsCompletedForInitialCrewMembers() throws PWCGException
    {
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAINoWounded(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : crewMembers.getCrewMemberList())
        {
            int minimumMissions = 1 + (crewMember.getCrewMemberVictories().getTankVictoryCount() * 3);
            if (!crewMember.isPlayer())
            {
                if (crewMember.getBattlesFought() < minimumMissions)
                {
                    crewMember.setBattlesFought(minimumMissions);
                }
            }
        }
    }

    private void setAiSkillLevel() throws PWCGException
    {
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAINoWounded(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : crewMembers.getCrewMemberList())
        {
            AiCrewMemberSkillGenerator aiCrewMemberSkillGenerator = new AiCrewMemberSkillGenerator();
            AiSkillLevel aiSkillLevel = aiCrewMemberSkillGenerator.calculateCrewMemberQualityByRankAndService(campaign, company, crewMember.getRank());
            crewMember.setAiSkillLevel(aiSkillLevel);
        }
    }

}
