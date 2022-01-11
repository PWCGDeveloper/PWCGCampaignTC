package pwcg.aar.outofmission.phase4.ElapsedTIme;

import java.util.Date;
import java.util.List;

import pwcg.aar.ui.events.model.CompanyMoveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;

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
        
        String airfieldNameNow = company.determineCurrentAirfieldName(campaign.getDate());
        String airfieldNameNext = company.determineCurrentAirfieldName(newDate);
        
        if (!airfieldNameNext.equalsIgnoreCase(airfieldNameNow))
        {
            String lastAirfield = company.determineCurrentAirfieldAnyMap(campaign.getDate()).getName();
            String newAirfield = company.determineCurrentAirfieldAnyMap(newDate).getName();
            boolean needsFerry = needsFerryMission(airfieldNameNow, airfieldNameNext);
            boolean isNewsworthy = true;
            companyMoveEvent = new CompanyMoveEvent(lastAirfield, newAirfield, company.getCompanyId(), needsFerry, newDate, isNewsworthy);
        }
        
        return companyMoveEvent;
    }

    private boolean needsFerryMission(String airfieldNameNow, String airfieldNameNext) throws PWCGException 
    {
        boolean needsFerry = false;
        
        boolean airfieldsOnSameMap = areAirfieldsOnTheSameMap(airfieldNameNow, airfieldNameNext);
        if (airfieldsOnSameMap)
        {
            double distance = calculateDistanceBetweenAirfields(airfieldNameNow, airfieldNameNext);
            if (distance < 50000.0)
            {
                needsFerry = true;
            }
        }
        
        return needsFerry;
    }

    private boolean areAirfieldsOnTheSameMap(String airfieldNameNow, String airfieldNameNext) throws PWCGException
    {
        List<FrontMapIdentifier> airfieldNowMaps = AirfieldManager.getMapIdForAirfield(airfieldNameNow);
        List<FrontMapIdentifier> airfieldNextMaps = AirfieldManager.getMapIdForAirfield(airfieldNameNext);
       
        boolean airfieldsOnSameMap = false;
        for (FrontMapIdentifier airfieldNowMap : airfieldNowMaps)
        {
            for (FrontMapIdentifier airfieldNextMap : airfieldNextMaps)
            {
                if (airfieldNowMap == airfieldNextMap)
                {
                    airfieldsOnSameMap = true;
                }
            }
        }
        return airfieldsOnSameMap;
    }

    private double calculateDistanceBetweenAirfields(String airfieldNameNow, String airfieldNameNext)
    {
        double distance = 1000000.0;
        Airfield airfieldNow = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldNameNow);
        Airfield airfieldNext = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldNameNext);
        if (airfieldNow != null && airfieldNext != null)
        {
            distance = MathUtils.calcDist(airfieldNow.getPosition(), airfieldNext.getPosition());
        }
        return distance;
    }
}
