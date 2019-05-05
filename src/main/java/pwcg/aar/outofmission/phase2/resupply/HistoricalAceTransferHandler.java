package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.resupply.personnel.SquadronTransferData;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;

public class HistoricalAceTransferHandler
{
    private Campaign campaign;
    private Date newDate;

    private SquadronTransferData acesTransferred = new SquadronTransferData();

    public HistoricalAceTransferHandler(Campaign campaign, Date newDate)
    {
        this.campaign = campaign;
        this.newDate = newDate;
    }

    public SquadronTransferData determineAceTransfers() throws PWCGException
    {
        CampaignPersonnelManager campaignPersonnelManager = campaign.getPersonnelManager();            
        List<Ace> acesBefore = PWCGContextManager.getInstance().getAceManager().getActiveAcesForCampaign(campaignPersonnelManager.getCampaignAces(), campaign.getDate());
        for (Ace aceBefore : acesBefore)
        {
            if (aceBefore.getActiveStatus() > SquadronMemberStatus.STATUS_CAPTURED)
            {
                HistoricalAce ha = PWCGContextManager.getInstance().getAceManager().getHistoricalAceBySerialNumber(aceBefore.getSerialNumber());
                Ace aceAfter = ha.getAtDate(newDate);

                if (!(aceBefore.getSquadronId() == aceAfter.getSquadronId()))
                {
                    TransferRecord aceTransferRecord = new TransferRecord(aceAfter, aceBefore.getSquadronId(), aceAfter.getSquadronId());
                    acesTransferred.addTransferRecord(aceTransferRecord);
                }
            }
        }

        return acesTransferred;
    }
}
