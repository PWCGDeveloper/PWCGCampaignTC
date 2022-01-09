package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CompanyHistory
{
    private List<CompanyHistoryEntry> squadHistoryEntries = new ArrayList<>();

	public CompanyHistoryEntry getSquadHistoryEntry(Date date) throws PWCGException
	{
	    CompanyHistoryEntry selectedSquadHistoryEntry = null;
	    for (CompanyHistoryEntry squadHistoryEntry :squadHistoryEntries)
	    {
	        Date squadHistoryDate = DateUtils.getDateYYYYMMDD(squadHistoryEntry.getDate());
	        if (!date.before(squadHistoryDate))
	        {
	            selectedSquadHistoryEntry = squadHistoryEntry;
	        }
	    }
		return selectedSquadHistoryEntry;
	}

    public List<CompanyHistoryEntry> getSquadHistoryEntries()
    {
        return squadHistoryEntries;
    }
}
