package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;

public class MissionCompanyBuilder
{
    public static List<ICompanyMission> getCompaniesInMissionForSide(Mission mission, Side side) throws PWCGException
    {
        List<Company> playerCompaniesInMission = mission.getParticipatingPlayers().getParticipatingCompanyIdsForSide(side);
        List <ICompanyMission> companiesInMission = new ArrayList<>();
        for(ICompanyMission playerCompany : playerCompaniesInMission)
        {
            companiesInMission.add(playerCompany);
        }

        int numAiPlatoons = MissionPlatoonSize.getNumAiPlatoonsForSide(mission, side, playerCompaniesInMission.size());

        MissionAiCompanyBuilder aiCompanyBuilder = new MissionAiCompanyBuilder();
        ICountry country = determineCountry(playerCompaniesInMission, side);
        List <ICompanyMission> aiCompaniesForMission = aiCompanyBuilder.buildAiCompanies(country);
        if (numAiPlatoons > 0)
        {
            for (int i = 0; i < numAiPlatoons; ++i)
            {
                companiesInMission.add(aiCompaniesForMission.get(i));
            }
        }
        return companiesInMission;
    }
    
    private static ICountry determineCountry(List<Company> playerCompaniesInMission, Side side)
    {
        for (Company playerCompany : playerCompaniesInMission)
        {
            if (playerCompany.getCountry().getCountry() == Country.BRITAIN)
            {
                return CountryFactory.makeCountryByCountry(Country.BRITAIN);
            }
        }
        
        return PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(side);
    }
}
