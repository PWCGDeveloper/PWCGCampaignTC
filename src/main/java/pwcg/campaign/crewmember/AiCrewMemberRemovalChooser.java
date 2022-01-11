package pwcg.campaign.crewmember;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class AiCrewMemberRemovalChooser
{
    private Campaign campaign;
    
    public AiCrewMemberRemovalChooser(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public CrewMember findAiCrewMemberToRemove(String humanCrewMemberRank, int companyId) throws PWCGException
    {

        CrewMember companyMemberToRemove = removeSameRank(humanCrewMemberRank, companyId);
        if (companyMemberToRemove == null)
        {
            companyMemberToRemove = removeSimilarRank(humanCrewMemberRank, companyId);
            if (companyMemberToRemove == null)
            {
                companyMemberToRemove = removeAnyRank(humanCrewMemberRank, companyId);
            }
        }
        
        return companyMemberToRemove;
    }

    private CrewMember removeSameRank(String humanCrewMemberRank, int companyId) throws PWCGException
    {
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(companyId);

        CrewMembers crewMembers = companyPersonnel.getCrewMembers();
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAI(crewMembers.getCrewMemberCollection(), campaign.getDate());

        List<CrewMember> crewMembersOfSameRank = new ArrayList<>();
        for (CrewMember crewMember : activeCrewMembers.getCrewMemberList())
        {
            if (crewMember.getRank().equals(humanCrewMemberRank))
            {
                crewMembersOfSameRank.add(crewMember);
            }
        }
        
        if (crewMembersOfSameRank.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(crewMembersOfSameRank.size());
            return (crewMembersOfSameRank.get(index));
        }
        
        return null;
    }

    private CrewMember removeSimilarRank(String humanCrewMemberRank, int companyId) throws PWCGException
    {
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(companyId);
        CrewMembers crewMembers = companyPersonnel.getCrewMembers();
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAI(crewMembers.getCrewMemberCollection(), campaign.getDate());
        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
        ArmedService service = company.determineServiceForCompany(campaign.getDate());

        List<CrewMember> crewMembersOfSimilarRank = new ArrayList<>();
        for (CrewMember crewMember : activeCrewMembers.getCrewMemberList())
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            int rankPosOfHumanCrewMember = rankObj.getRankPosByService(humanCrewMemberRank, service);
            int rankPosOfAiCrewMember = rankObj.getRankPosByService(crewMember.getRank(), service);

            if (rankPosOfAiCrewMember > IRankHelper.COMMAND_RANK)
            {
                if (Math.abs(rankPosOfHumanCrewMember - rankPosOfAiCrewMember) < 2)
                {
                    crewMembersOfSimilarRank.add(crewMember);
                }
            }
        }
        
        if (crewMembersOfSimilarRank.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(crewMembersOfSimilarRank.size());
            return (crewMembersOfSimilarRank.get(index));
        }

        return null;
    }

    private CrewMember removeAnyRank(String humanCrewMemberRank, int companyId) throws PWCGException
    {
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(companyId);
        CrewMembers crewMembers = companyPersonnel.getCrewMembers();
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAI(crewMembers.getCrewMemberCollection(), campaign.getDate());
        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
        ArmedService service = company.determineServiceForCompany(campaign.getDate());

        List<CrewMember> crewMembersOfAnyNonCommandRank = new ArrayList<>();
        for (CrewMember crewMember : activeCrewMembers.getCrewMemberList())
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            int rankPosOfAiCrewMember = rankObj.getRankPosByService(crewMember.getRank(), service);

            if (rankPosOfAiCrewMember > IRankHelper.COMMAND_RANK)
            {
                crewMembersOfAnyNonCommandRank.add(crewMember);
            }
        }
        
        if (crewMembersOfAnyNonCommandRank.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(crewMembersOfAnyNonCommandRank.size());
            return (crewMembersOfAnyNonCommandRank.get(index));
        }

        return null;
    }
}
