package pwcg.campaign.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.io.json.CompanyIOJson;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class CompanyManager 
{
	private TreeMap<Integer, Company> companyMap = new TreeMap<>();

	public CompanyManager ()
	{
	}

	public void initialize() throws PWCGException 
	{
        companyMap.clear();
        		
        List<Company> companies = CompanyIOJson.readJson(); 
		for (Company company : companies)
		{
			companyMap.put(company.getCompanyId(), company);
		}
	}

	public Company getCompany(int id) throws PWCGException
	{
		if(companyMap.containsKey(id))
		{
			Company company = companyMap.get(id);
			return company;
		}
		
    	return null;
	}

    public Company getCompanyByName(String squadName, Date campaignDate) throws PWCGException 
    {
        Company squadReturn = null;
        for (Company company : companyMap.values())
        {
            if (squadName.equalsIgnoreCase(company.determineDisplayName(campaignDate)))
            {
                squadReturn = company;
            }
            
            if (squadName.equalsIgnoreCase(company.determineDisplayName(campaignDate)))
            {
                squadReturn = company;
            }
            
            String displayName = company.determineDisplayName(campaignDate);           
            if (displayName != null)
            {
                if (squadName.equalsIgnoreCase(displayName))
                {
                    squadReturn = company;
                }               
            }
            
            if (squadReturn != null)
            {
                 break;
            }
        }
        
        if (squadReturn == null)
        {
             throw new PWCGException ("Squad not found for name " + squadName);
        }
        
        return squadReturn;
    }

    public List<Company> getAllCompanies()
    {
        ArrayList<Company> list = new ArrayList<Company>();
        list.addAll(companyMap.values());
        return list;
        
    }

    public List<Company> getActiveCompanies(Date date) throws PWCGException 
    {
        ArrayList<Company> returnSquadList = new ArrayList<Company>();
        for (Company company : getAllCompanies())
        {
            if (CompanyViability.isCompanyActive(company, date))
            {
                returnSquadList.add(company);
            }
        }

        return returnSquadList;
    }

    public List<Company> getActiveCompaniesForCurrentMap(Date date) throws PWCGException 
    {
        List<Company> activeCompanysForCurrentMap = CompanyReducer.reduceToCurrentMap(getActiveCompanies(date), date);
        return activeCompanysForCurrentMap;
        
    }

    public List<Company> getActiveCompaniesForSide(Date date, Side side) throws PWCGException 
    {
        List<Company> activeCompanysForSide = CompanyReducer.reduceToSide(getActiveCompanies(date), side);
        return activeCompanysForSide;
    }

    public List<Company> getActiveCompaniesForService(Date date, ArmedService service) throws PWCGException 
    {
        List<Company> activeCompanysForService = CompanyReducer.reduceToService( getActiveCompanies(date), date, service);
        return activeCompanysForService;
    }

    public List<Company> getActiveCompaniesBySideAndProximity(Side side, Date date, Coordinate referencePosition, double radius) throws PWCGException
    {
        List<Company> activeCompanysForSide = CompanyReducer.reduceToSide( getActiveCompanies(date), side);
        List<Company> activeCompanysForSideInRange = new ArrayList<>();
        while (activeCompanysForSideInRange.isEmpty())
        {
            activeCompanysForSideInRange = CompanyReducer.reduceToProximityOnCurrentMap(activeCompanysForSide, date, referencePosition, radius);
            radius += 5000;
            if (radius > 500000)
            {
                break;
            }
        }
        return activeCompanysForSideInRange;        
    }

	public List<Company> getPlayerCompaniesByService(ArmedService service, Date date) throws PWCGException 
	{
		List<Company> squadList = getActiveCompaniesForService(date, service);
        List<Company> list = new ArrayList<Company>();
	        
		for (Company company : squadList)
		{
            list.add(company);
		}
		
		return list;
	}

    public Company getAnyActiveCompanyForAirfield(Airfield airfield, Date date) throws PWCGException 
    {
        for (Company company : companyMap.values())
        {
            String currentFieldNameForSquad = company.determineCurrentAirfieldName(date);
            if (currentFieldNameForSquad != null)
            {
                Airfield currentFieldForSquad =  PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(currentFieldNameForSquad);
                
                if (currentFieldForSquad != null)
                {
                    String squadAF = currentFieldForSquad.getName();
                    if (airfield.getName().equals(squadAF))
                    {
                        if (CompanyViability.isCompanyActive(company, date))
                        {
                            return company;
                        }
                    }
                }
            }
        }
        
        return null;
    }

    public ArrayList<Company> getViableCompanies(Campaign campaign) throws PWCGException 
    {
        ArrayList<Company> returnSquadList = new ArrayList<Company>();
        for (Company company : getAllCompanies())
        {
            if (CompanyViability.isCompanyViable(company, campaign))
            {
                returnSquadList.add(company);
            }
        }

         return returnSquadList;
    }

    public List<Company> getViableAiCompaniesForCurrentMapAndSide(Campaign campaign, Side side) throws PWCGException 
    {
        List<Company> viableCompanysForSide = CompanyReducer.reduceToSide(getViableCompanies(campaign), side);
        List<Company> selectedCompanysNoPlayer = CompanyReducer.reduceToAIOnly(viableCompanysForSide, campaign);
        List<Company> selectedCompanysForCurrentMap = CompanyReducer.reduceToCurrentMap(selectedCompanysNoPlayer, campaign.getDate());
        
        return selectedCompanysForCurrentMap;
    }

    public List<Company> getViableAiCompaniesForCurrentMapAndSideAndRole(Campaign campaign, List <PwcgRole> acceptableRoles, Side side) throws PWCGException 
    {
        List<Company> viableCompanysForSide = CompanyReducer.reduceToSide(getViableCompanies(campaign), side);
        List<Company> selectedCompanysByRole = CompanyReducer.reduceToRole(viableCompanysForSide, acceptableRoles, campaign.getDate());
        List<Company> selectedCompanysNoPlayer = CompanyReducer.reduceToAIOnly(selectedCompanysByRole, campaign);
        List<Company> selectedCompanysForCurrentMap = CompanyReducer.reduceToCurrentMap(selectedCompanysNoPlayer, campaign.getDate());
        
        return selectedCompanysForCurrentMap;
    }

    public Company getClosestCompany(Coordinate position, Date date) throws PWCGException 
    {
        Airfield airfield = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().findClosestAirfield(position);
        if (airfield != null)
        {
            Company company = getAnyActiveCompanyForAirfield(airfield, date);
            return company;
        }

        return null;
    }
}
