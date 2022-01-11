package pwcg.campaign.personnel;

import java.util.Date;

public class CrewMemberFilterFactory
{

    public static CrewMemberFilterSpecification buildActiveAIFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifyCompany(CrewMemberFilterSpecification.NO_Company_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIAndPlayerFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifyCompany(CrewMemberFilterSpecification.NO_Company_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIAndAcesFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifyCompany(CrewMemberFilterSpecification.NO_Company_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIAndPlayerAndAcesFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifyCompany(CrewMemberFilterSpecification.NO_Company_FILTER);
        return filterSpecification;
    }
    
    public static CrewMemberFilterSpecification buildInactiveAIAndPlayerAndAcesFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(false);
        filterSpecification.setIncludeInactive(true);
        filterSpecification.setSpecifyCompany(CrewMemberFilterSpecification.NO_Company_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActivePlayerFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(false);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(false);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifyCompany(CrewMemberFilterSpecification.NO_Company_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIByForCompanyFilter(Date date, int companyId)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifyCompany(companyId);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIAndPlayerForCompanyFilter(Date date, int companyId)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifyCompany(companyId);
        return filterSpecification;
    }
    
    public static CrewMemberFilterSpecification buildActiveAIAndPlayerAndAcesForCompanyFilter(Date date, int companyId)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(true);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifyCompany(companyId);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAINoWoundedFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(false);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(false);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifyCompany(CrewMemberFilterSpecification.NO_Company_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIAndPlayerAndAcesNoWoundedFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(true);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(false);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifyCompany(CrewMemberFilterSpecification.NO_Company_FILTER);
        return filterSpecification;
    }

    public static CrewMemberFilterSpecification buildActiveAIAndAcesNoWoundedFilter(Date date)
    {
        CrewMemberFilterSpecification filterSpecification = new CrewMemberFilterSpecification();
        filterSpecification.setDate(date);
        filterSpecification.setIncludePlayer(false);        
        filterSpecification.setIncludeAces(true);
        filterSpecification.setIncludeAI(true);
        filterSpecification.setIncludeWounded(false);
        filterSpecification.setIncludeActive(true);
        filterSpecification.setIncludeInactive(false);
        filterSpecification.setSpecifyCompany(CrewMemberFilterSpecification.NO_Company_FILTER);
        return filterSpecification;
    }

}
