package pwcg.aar.tabulate.campaignupdate;

import java.util.ArrayList;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARLogEvents;
import pwcg.aar.data.ui.UIDebriefData;
import pwcg.aar.outofmission.phase4.ElapsedTIme.ElapsedTimeEventGenerator;
import pwcg.aar.outofmission.phase4.ElapsedTIme.ElapsedTimeEvents;
import pwcg.aar.ui.display.model.CampaignUpdateEvents;
import pwcg.aar.ui.events.model.CompanyMoveEvent;
import pwcg.aar.ui.events.model.CrewMemberStatusEvent;
import pwcg.aar.ui.events.model.TankStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class CampaignLogGenerator
{
	private Campaign campaign;
    private AARContext aarContext;
    private AARLogEvents campaignLogEvents;
    private CampaignUpdateEvents elapsedTimeCombatResultsData;
	
	public CampaignLogGenerator(Campaign campaign, AARContext aarContext, CampaignUpdateEvents elapsedTimeCombatResultsData)
	{
		this.campaign = campaign;
        this.aarContext = aarContext;
        this.elapsedTimeCombatResultsData = elapsedTimeCombatResultsData;
        this.campaignLogEvents = new AARLogEvents(campaign);
	}
	
	public AARLogEvents createCampaignLogEventsForCampaignUpdate() throws PWCGException
	{
		createCrewMemberVictoryEvents();
		createCrewMemberMedalEvents();
		createCrewMemberPromotionEvents();
        createCrewMemberLossEvents();
        createEquipmentLossEvents();
		createTransfersEvents();
		createElapsedTimeEvents();
		
		return campaignLogEvents;
	}

	private void createCrewMemberVictoryEvents() throws PWCGException
    {
        campaignLogEvents.addEvents(elapsedTimeCombatResultsData.getVictories());          
    }

    private void createCrewMemberLossEvents() throws PWCGException
    {
        campaignLogEvents.addEvents(new ArrayList<CrewMemberStatusEvent>(elapsedTimeCombatResultsData.getCrewMembersLost()));
    }
    
    private void createEquipmentLossEvents()
    {
        campaignLogEvents.addEvents(new ArrayList<TankStatusEvent>(elapsedTimeCombatResultsData.getTanksLost()));
    }
    
    private void createCrewMemberMedalEvents() throws PWCGException
    {
        UIDebriefData debriefData = aarContext.getUiDebriefData();
        campaignLogEvents.addEvents(debriefData.getMedalPanelData().getMedalsAwarded());
    }
    
    private void createCrewMemberPromotionEvents() throws PWCGException
    {
        UIDebriefData debriefData = aarContext.getUiDebriefData();
        campaignLogEvents.addEvents(debriefData.getPromotionPanelData().getPromotionEventsDuringElapsedTime());
    }

    private void createTransfersEvents() throws PWCGException
    {
        UIDebriefData debriefData = aarContext.getUiDebriefData();
        campaignLogEvents.addEvents(debriefData.getTransferPanelData().getTransfers());
    }

    private void createElapsedTimeEvents() throws PWCGException
    {
        if (aarContext.getNewDate() != null)
        {
            ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);  
            ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
            for (CompanyMoveEvent companyMoveEvent : elapsedTimeEvents.getCompanyMoveEvents())
            {
                campaignLogEvents.addEvent(companyMoveEvent);
            }

            if (elapsedTimeEvents.getEndOfWarEvent() != null)
            {
                campaignLogEvents.addEvent(elapsedTimeEvents.getEndOfWarEvent());
            }
        }
    }
}
