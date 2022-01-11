package pwcg.campaign.personnel;

import java.util.Date;
import java.util.Map;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.core.exception.PWCGException;

public class CrewMemberFilter
{
    public static CrewMembers filterActivePlayers(Map<Integer, CrewMember> crewMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActivePlayerFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(crewMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAI(Map<Integer, CrewMember> crewMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(crewMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAIAndPlayer(Map<Integer, CrewMember> crewMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndPlayerFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(crewMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAIAndAces(Map<Integer, CrewMember> crewMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndAcesFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(crewMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAIAndPlayerAndAces(Map<Integer, CrewMember> crewMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndPlayerAndAcesFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(crewMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterInactiveAIAndPlayerAndAces(Map<Integer, CrewMember> crewMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildInactiveAIAndPlayerAndAcesFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(crewMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }
    
    public static CrewMembers filterActiveAIForCompany(Map<Integer, CrewMember> crewMembersToFilter, Date date, int companyId) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIByForCompanyFilter(date, companyId);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(crewMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAIAndPlayerForCompany(Map<Integer, CrewMember> crewMembersToFilter, Date date, int companyId) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndPlayerForCompanyFilter(date, companyId);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(crewMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAIAndPlayerAndAcesForCompany(Map<Integer, CrewMember> crewMembersToFilter, Date date, int companyId) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndPlayerAndAcesForCompanyFilter(date, companyId);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(crewMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAINoWounded(Map<Integer, CrewMember> crewMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAINoWoundedFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(crewMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }    

    private static CrewMembers mapToCrewMembers(Map<Integer, CrewMember> map)
    {
        CrewMembers crewMembers = new CrewMembers();
        for (CrewMember crewMember : map.values())
        {
            crewMembers.addToCrewMemberCollection(crewMember);
        }
        return crewMembers;
    }

    public static CrewMembers filterActiveAIAndPlayerAndAcesNoWounded(Map<Integer, CrewMember> crewMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndPlayerAndAcesNoWoundedFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(crewMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }

    public static CrewMembers filterActiveAIAndAcesNoWounded(Map<Integer, CrewMember> crewMembersToFilter, Date date) throws PWCGException
    {
        CrewMemberFilterSpecification filterSpecification = CrewMemberFilterFactory.buildActiveAIAndAcesNoWoundedFilter(date);
        CampaignPersonnelFilter filter = new CampaignPersonnelFilter(crewMembersToFilter);     
        Map<Integer, CrewMember> filteredCrewMembers = filter.getFilteredCrewMembers(filterSpecification);
        return mapToCrewMembers(filteredCrewMembers);
    }
}
