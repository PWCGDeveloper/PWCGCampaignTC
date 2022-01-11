package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.CompanyMoveEvent;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignReportCompanyMoveGUI extends AARDocumentIconPanel
{
	private static final long serialVersionUID = 1L;
	private CompanyMoveEvent companyMoveEvent = null;

	public CampaignReportCompanyMoveGUI(CompanyMoveEvent companyMoveEvent) throws PWCGException
	{
		super();

        this.companyMoveEvent = companyMoveEvent;
		makePanel();		
	}

    protected String getHeaderText() throws PWCGException
    {
        String transferHeaderText = "Notification of Company Relocation \n\n";
        return transferHeaderText;
    }

    protected String getBodyText() throws PWCGException
    {
        String companyMoveText = "Company: " + companyMoveEvent.getCompanyName() + "\n";
        companyMoveText += "Date: " + DateUtils.getDateStringPretty(companyMoveEvent.getDate()) + "\n";
        companyMoveText += companyMoveEvent.getCompanyName() + 
                        " has been moved to " + companyMoveEvent.getNewAirfield() + ".\n";   
        
        
        return companyMoveText;
	}

    @Override
    public void finished()
    {
    }

    @Override
    protected String getFooterImagePath() throws PWCGException
    {
        return "";
    }
}
