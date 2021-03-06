package pwcg.aar.campaign.update;

import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;

public class ServiceChangeHandler
{
    private Campaign campaign = null;
        
    public ServiceChangeHandler (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public void handleChangeOfService(Date newDate) throws PWCGException 
    {
        for (Company company : PWCGContext.getInstance().getCompanyManager().getActiveCompanies(campaign.getDate()))
        {
            handleChangeOfServiceForCompany(company, newDate);
        }
    }
    
    private void handleChangeOfServiceForCompany(Company company, Date newDate) throws PWCGException 
    {
        ArmedService serviceNow = company.determineServiceForCompany(campaign.getDate());
        ArmedService serviceAfter = company.determineServiceForCompany(newDate);

        if (serviceNow.getServiceId() != serviceAfter.getServiceId())
        {
            changeService(company, serviceNow, serviceAfter);
        }
    }

    private void changeService(Company company, ArmedService serviceNow, ArmedService serviceAfter) throws PWCGException
    {
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(company.getCompanyId());
        CrewMembers crewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : crewMembers.getCrewMemberList())
        {
            setCrewMemberRanksForNewService(crewMember, serviceNow, serviceAfter);
            setCrewMemberCountryForNewService(crewMember, serviceAfter);
        }
    }


    private void setCrewMemberRanksForNewService(CrewMember crewMember, ArmedService serviceNow, ArmedService serviceAfter) throws PWCGException
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        List<String> ranksNow = rankObj.getRanksByService(serviceNow);
        List<String> ranksAfter = rankObj.getRanksByService(serviceAfter);

        String rankAfter = getRankAfterChangeOfService(ranksNow, ranksAfter, crewMember);
        crewMember.setRank(rankAfter);
        setRankOfHistoricalAce(crewMember);
    }



    private String getRankAfterChangeOfService(List<String> ranksNow, List<String> ranksAfter, CrewMember crewMember)
    {
        String rankAfter = ranksAfter.get(ranksAfter.size() - 1);
        String crewMemberRank = crewMember.getRank();
        for (int i = 0; i < ranksNow.size(); ++i)
        {
            String rankNow = ranksNow.get(i);
            if (rankNow.equals(crewMemberRank))
            {
                if (i < ranksAfter.size())
                {
                    rankAfter = ranksAfter.get(i);
                    break;
                }
            }
        }
        return rankAfter;
    }

    private void setRankOfHistoricalAce(CrewMember crewMember)
    {
        if (crewMember instanceof TankAce)
        {
        	TankAce ace = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(crewMember.getSerialNumber());
            if (ace != null)
            {
                ace.setRank(crewMember.getRank());
            }
        }
    }

    private void setCrewMemberCountryForNewService(CrewMember crewMember, ArmedService serviceAfter) throws PWCGException
    {
        ICountry country = CountryFactory.makeCountryByService(serviceAfter);
        crewMember.setCountry(country.getCountry());
    }
}
