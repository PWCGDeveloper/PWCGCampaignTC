package pwcg.aar.outofmission.phase4.ElapsedTIme;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.ui.events.model.CompanyMoveEvent;
import pwcg.aar.ui.events.model.EndOfWarEvent;

public class ElapsedTimeEvents
{
    private List<CompanyMoveEvent> companyMoveEvents = new ArrayList<>();
    private EndOfWarEvent endOfWarEvent = null;

    public EndOfWarEvent getEndOfWarEvent()
    {
        return endOfWarEvent;
    }

    public void setEndOfWarEvent(EndOfWarEvent endOfWarEvent)
    {
        this.endOfWarEvent = endOfWarEvent;
    }

    public List<CompanyMoveEvent> getCompanyMoveEvents()
    {
        return companyMoveEvents;
    }

    public void addCompanyMoveEvent(CompanyMoveEvent companyMoveEvent)
    {
        this.companyMoveEvents.add(companyMoveEvent);
    }
    
    public void merge(ElapsedTimeEvents elapsedTimeEvents)
    {
        companyMoveEvents.addAll(elapsedTimeEvents.getCompanyMoveEvents());
        if (endOfWarEvent == null)
        {
            endOfWarEvent = elapsedTimeEvents.getEndOfWarEvent();
        }
    }
}
