package pwcg.mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.product.bos.country.TCServiceManager;

public class MissionAiCompanyBuilder
{
    private Campaign campaign;
    private List<ICompanyMission> companies = new ArrayList<>();

    public MissionAiCompanyBuilder(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<ICompanyMission> buildAiCompanies(ICountry country) throws PWCGException
    {
        if(country.getCountry() == Country.USA)
        {
            buildAmericanCompanies(country);
        }
        else if(country.getCountry() == Country.BRITAIN)
        {
            buildBritishCompanies(country);
        }
        else if(country.getCountry() == Country.RUSSIA)
        {
            buildRussianCompanies(country);
        }
        else if(country.getCountry() == Country.GERMANY)
        {
            buildGermanCompanies(country);
        }
        
        Collections.shuffle(companies);
        return companies;
    }
    
    private void buildAmericanCompanies(ICountry country) throws PWCGException
    {
        List<String> divisionNames = Arrays.asList("1st", "2nd", "3rd", "4th");
        List<String> companyNames = Arrays.asList("Able", "Baker", "Charlie", "Dog", "Easy", "Fox");
        
        int startingCompanyId = TCServiceManager.US_ARMY;
        // TODO TC change hard coded coordinates to town close to battle.
        Coordinate companyPosition = PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownLocations().getLocationsWithinRadiusBySide(
                country.getSide(), campaign.getDate(), new Coordinate(75000, 0, 75000), 1000000).get(0).getPosition();
        addAiCOmpaniesForCountry(country, divisionNames, companyNames, startingCompanyId, companyPosition);
    }
    
    private void buildBritishCompanies(ICountry country) throws PWCGException
    {
        List<String> divisionNames = Arrays.asList("8th", "11th", "14th", "17th");
        List<String> companyNames = Arrays.asList("1st", "2nd", "3rd", "4th", "5th", "6th");
        
        int startingCompanyId = TCServiceManager.BRITISH_ARMY;
        Coordinate companyPosition = PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownLocations().getLocationsWithinRadiusBySide(
                country.getSide(), campaign.getDate(), new Coordinate(75000, 0, 75000), 1000000).get(0).getPosition();
        addAiCOmpaniesForCountry(country, divisionNames, companyNames, startingCompanyId, companyPosition);
    }
    
    private void buildRussianCompanies(ICountry country) throws PWCGException
    {
        List<String> divisionNames = Arrays.asList("143rd", "167th", "322nd", "46th");
        List<String> companyNames = Arrays.asList("1st", "2nd", "3rd", "4th", "5th", "6th");
        
        int startingCompanyId = TCServiceManager.SSV;
        Coordinate companyPosition = PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownLocations().getLocationsWithinRadiusBySide(
                country.getSide(), campaign.getDate(), new Coordinate(75000, 0, 75000), 1000000).get(0).getPosition();
        addAiCOmpaniesForCountry(country, divisionNames, companyNames, startingCompanyId, companyPosition);
    }
    
    private void buildGermanCompanies(ICountry country) throws PWCGException
    {
        List<String> divisionNames = Arrays.asList("17th", "151st", "10th", "9th");
        List<String> companyNames = Arrays.asList("1st", "2nd", "3rd", "4th", "5th", "6th");
        
        int startingCompanyId = TCServiceManager.WEHRMACHT;
        Coordinate companyPosition = PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownLocations().getLocationsWithinRadiusBySide(
                country.getSide(), campaign.getDate(), new Coordinate(75000, 0, 75000), 1000000).get(0).getPosition();
        addAiCOmpaniesForCountry(country, divisionNames, companyNames, startingCompanyId, companyPosition);
    }

    private void addAiCOmpaniesForCountry(ICountry country, List<String> divisionNames, List<String> companyNames, int startingCompanyId,
            Coordinate companyPosition)
    {
        for(String division : divisionNames)
        {
            for(String company : companyNames)
            {
                String companyName = division + " Division " + company + " Company";
                AiCompany aiCompany = new AiCompany(companyName, "base", companyPosition, country, startingCompanyId);
                companies.add(aiCompany);
                
                ++startingCompanyId;
            }
        }
    }
}
