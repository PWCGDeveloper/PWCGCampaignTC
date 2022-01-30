package pwcg.aar.outofmission.phase4.ElapsedTIme;

import java.util.Date;

import pwcg.aar.ui.events.model.CompanyMoveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;

public class CompanyMoveHandler
{
    private Campaign campaign = null;

    public CompanyMoveHandler (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public CompanyMoveEvent companyMoves(Date newDate, Company company) throws PWCGException 
    {
        CompanyMoveEvent companyMoveEvent = null;
        
        String airfieldNameNow = company.determineBaseName(campaign.getDate());
        String airfieldNameNext = company.determineBaseName(newDate);
        
        if (!airfieldNameNext.equalsIgnoreCase(airfieldNameNow))
        {
            String lastAirfield = company.determineCurrentBaseAnyMap(campaign.getDate()).getName();
            String newAirfield = company.determineCurrentBaseAnyMap(newDate).getName();
            boolean isNewsworthy = true;
            companyMoveEvent = new CompanyMoveEvent(lastAirfield, newAirfield, company.getCompanyId(), newDate, isNewsworthy);
        }
        
        return companyMoveEvent;
    }
}
