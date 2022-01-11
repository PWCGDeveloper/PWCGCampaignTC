package pwcg.gui.rofmap.event;

import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CampaignReportTransferPanel extends AARDocumentIconPanel
{
    private Campaign campaign;
    private static final long serialVersionUID = 1L;
    private TransferEvent transferEvent;

    public CampaignReportTransferPanel(Campaign campaign, TransferEvent transferEvent) throws PWCGException
    {
        super();

        this.campaign = campaign;        
        this.transferEvent = transferEvent;        
        this.shouldDisplay = true;
        makePanel();
    }

    protected String getHeaderText() throws PWCGException
    {
        String transferHeaderText = "Notification of Transfer \n\n";
        return transferHeaderText;
    }

    protected String getBodyText() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        Company fromCompany = companyManager.getCompany(transferEvent.getTransferFrom());
        Company toCompany = companyManager.getCompany(transferEvent.getTransferTo());

        String transferMessage = transferEvent.getCrewMemberName() + " has been transferred" + "\n";
                        
        if (fromCompany != null && toCompany != null)
        {
            transferMessage = transferEvent.getCrewMemberName() + " has been transferred from\n" + 
                            fromCompany.determineDisplayName(campaign.getDate()) + 
                            " to " + toCompany.determineDisplayName(campaign.getDate())+ "\n";
        }
        else if (toCompany != null)
        {
            transferMessage = transferEvent.getCrewMemberName() + 
                            " has been transferred to " + toCompany.determineDisplayName(campaign.getDate()) + "\n";
        }
        else if (fromCompany != null)
        {
            transferMessage = transferEvent.getCrewMemberName() + 
                            " has been transferred from " + fromCompany.determineDisplayName(campaign.getDate()) + "\n";
        }
        
        transferMessage += "Date: " + DateUtils.getDateStringPretty(transferEvent.getDate())+ "\n";


        return transferMessage;
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
