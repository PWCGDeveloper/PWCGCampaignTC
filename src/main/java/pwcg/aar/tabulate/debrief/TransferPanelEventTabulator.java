package pwcg.aar.tabulate.debrief;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.TransferPanelData;
import pwcg.aar.ui.events.TransferEventGenerator;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;

public class TransferPanelEventTabulator
{
    private Campaign campaign;
    private AARContext aarContext;

    private TransferPanelData transferPanelData = new TransferPanelData();

    public TransferPanelEventTabulator (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
        
    public TransferPanelData tabulateForAARTransferPanel() throws PWCGException
    {
        createTransferEvents();        
        return transferPanelData;
    }
    
    private void createTransferEvents() throws PWCGException
    {
        TransferEventGenerator transferEventGenerator = new TransferEventGenerator(campaign);
        List<TransferEvent> allTransferInEvents = new ArrayList<>();

        List<TransferEvent> transferEventsForCompanyAces = transferAces(transferEventGenerator);
        allTransferInEvents.addAll(transferEventsForCompanyAces);
        
        List<TransferEvent> transferEventsForCrewMembers = transferCrewMembers(transferEventGenerator);
        allTransferInEvents.addAll(transferEventsForCrewMembers);
        
        transferPanelData.setTransfers(allTransferInEvents);
    }

    private List<TransferEvent> transferAces(TransferEventGenerator transferEventGenerator) throws PWCGException
    {
        List <TransferRecord> acesTransferredIn = aarContext.getResupplyData().getAcesTransferred().getCrewMembersTransferred();
        List<TransferEvent> transferEventsForAces = transferEventGenerator.createCrewMemberTransferEvents(acesTransferredIn);
        return transferEventsForAces;
    }

    private List<TransferEvent> transferCrewMembers(TransferEventGenerator transferEventGenerator) throws PWCGException
    {
        List <TransferRecord> crewMembersTransferredIn = aarContext.getResupplyData().getCompanyTransferData().getCrewMembersTransferred();
        List<TransferEvent> transferEventsForCrewMembers = transferEventGenerator.createCrewMemberTransferEvents(crewMembersTransferredIn);
        return transferEventsForCrewMembers;
    }
}
