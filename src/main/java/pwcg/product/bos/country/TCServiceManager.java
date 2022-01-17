package pwcg.product.bos.country;

import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class TCServiceManager extends ArmedServiceManager
{
    public static int WEHRMACHT = 20111;
    public static int SSV = 10112;
    public static int US_ARMY = 10113;
    public static int BRITISH_ARMY = 10114;


    private static TCServiceManager instance;
    
    public static TCServiceManager getInstance()
    {
        if (instance == null)
        {
            instance = new TCServiceManager();
            instance.initialize();

        }
        return instance;
    }
    
    private TCServiceManager ()
    {
    }

    protected void initialize() 
    {
        try
        {    
            createRussianServices();
            createGermanServices();
            createAmericanServices();
            createBritishServices();
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
    }
    
    private void createRussianServices() throws PWCGException
    {
        armedServicesByCountry.put(BoSCountry.RUSSIA_CODE, RussianServiceBuilder.createServices());
    }
    
    private void createGermanServices() throws PWCGException
    {
        armedServicesByCountry.put(BoSCountry.GERMANY_CODE, GermanServiceBuilder.createServices());
    }

    private void createAmericanServices() throws PWCGException
    {
        armedServicesByCountry.put(BoSCountry.USA_CODE, AmericanServiceBuilder.createServices());
    }

    private void createBritishServices() throws PWCGException
    {
        armedServicesByCountry.put(BoSCountry.BRITAIN_CODE, BritishServiceBuilder.createServices());
    }

    public ArmedService getArmedServiceById(int serviceId) throws PWCGException 
	{
		for(List <ArmedService> serviceList : armedServicesByCountry.values())
		{
			for (ArmedService service : serviceList)
			{
				if (service.getServiceId() == serviceId)
				{
					return service;
				}
			}
		}
		
		throw new PWCGException ("No service found for id = " + serviceId);
	}

    public ArmedService getArmedServiceByName(String serviceName) throws PWCGException 
	{
		for(List <ArmedService> serviceList : armedServicesByCountry.values())
		{
			for (ArmedService service : serviceList)
			{
				if (service.getName().equals(serviceName))
				{
					return service;
				}
			}
		}
		
        throw new PWCGException ("No service found for name = " + serviceName);
	}

    public ArmedService getPrimaryServiceForNation(Country country) throws PWCGException
    {
        if (country == Country.GERMANY)
        {
            return(getArmedServiceById(WEHRMACHT));
        }
        else if (country == Country.RUSSIA)
        {
            return(getArmedServiceById(SSV));
        }
        else if (country == Country.USA)
        {
            return(getArmedServiceById(US_ARMY));
        }
        else if (country == Country.BRITAIN)
        {
            return(getArmedServiceById(BRITISH_ARMY));
        }
        
        throw new PWCGException("Unexpected country for getPrimaryServiceForNation " + country);
    }

    public ArmedService determineServiceByParsingCompanyId(int companyId, Date date) throws PWCGException
    {
        String companyIdString = "" + companyId;
        if (companyIdString.length() >= 3)
        {
            String countryCodeString = companyIdString.substring(0,3);
            Integer countryCode = Integer.valueOf(countryCodeString);
            ICountry country = CountryFactory.makeCountryByCode(countryCode);
    
            return getPrimaryServiceForNation(country.getCountry());
        }
        else
        {
            throw new PWCGException("");
        }
    }
}
