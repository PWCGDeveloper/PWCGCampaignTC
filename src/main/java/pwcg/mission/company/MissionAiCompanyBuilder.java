package pwcg.mission.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.AiCompany;
import pwcg.mission.ICompanyMission;

public class MissionAiCompanyBuilder
{
    private Campaign campaign;
    private List<ICompanyMission> companies = new ArrayList<>();
    private IAiCompanyParameters companyParameters = null;
    private int companyId = 0;

    public MissionAiCompanyBuilder(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public List<ICompanyMission> buildAiCompanies(ICountry country) throws PWCGException
    {
        if(country.getCountry() == Country.USA)
        {
            companyParameters = new AmericanCompanyParameters(campaign);
        }
        else if(country.getCountry() == Country.BRITAIN)
        {
            companyParameters = new BritishCompanyParameters(campaign);
        }
        else if(country.getCountry() == Country.RUSSIA)
        {
            companyParameters = new RussianCompanyParameters(campaign);
        }
        else if(country.getCountry() == Country.GERMANY)
        {
            companyParameters = new GermanCompanyParameters(campaign);
        }

        companyId = companyParameters.getStartingCompanyId();
        addAiTankCompaniesForCountry(country);
        addAiInfantryCompaniesForCountry(country);
        addAiTankDestroyerCompaniesForCountry(country);

        Collections.shuffle(companies);
        return companies;
    }

    private void addAiTankCompaniesForCountry(ICountry country)
    {
        int numAdded = 0;
        for(String division : companyParameters.getArmoredDivisionNames())
        {
            for(String company : companyParameters.getCompanyNames())
            {
                String companyName = division + ", " + company + " Co.";
                AiCompany aiCompany = new AiCompany(
                        companyName, "base", companyParameters.getCompanyPosition(), country,  companyId, PwcgRoleCategory.MAIN_BATTLE_TANK);
                companies.add(aiCompany);

                ++companyId;
                ++numAdded;
                if(numAdded >= companyParameters.getNumberOfTank())
                {
                    break;
                }
            }
        }
    }

    private void addAiInfantryCompaniesForCountry(ICountry country)
    {
        int numAdded = 0;
        for(String division : companyParameters.getInfantryDivisionNames())
        {
            for(String company : companyParameters.getCompanyNames())
            {
                String companyName = division + ", " + company + " Co.";
                AiCompany aiCompany = new AiCompany(
                        companyName, "base", companyParameters.getCompanyPosition(), country,  companyId, PwcgRoleCategory.SELF_PROPELLED_GUN);
                companies.add(aiCompany);

                ++companyId;
                ++numAdded;
                if(numAdded >= companyParameters.getNumberOfInfantry())
                {
                    break;
                }
            }
        }
    }

    private void addAiTankDestroyerCompaniesForCountry(ICountry country)
    {
        int numAdded = 0;
        for(String division : companyParameters.getTankDestroyerDivisionNames())
        {
            for(String company : companyParameters.getCompanyNames())
            {
                String companyName = division + ", " + company + " Co.";
                AiCompany aiCompany = new AiCompany(
                        companyName, "base", companyParameters.getCompanyPosition(), country,  companyId, PwcgRoleCategory.TANK_DESTROYER);
                companies.add(aiCompany);

                ++companyId;
                ++numAdded;
                if(numAdded >= companyParameters.getNumberOfTankDestroyer())
                {
                    break;
                }
            }
        }
    }
}
