package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.VictoryEvent;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;

public class AARMissionVictoryPanel extends AARDocumentPanel
{
    private static final long serialVersionUID = 1L;
    private AARCoordinator aarCoordinator;
    private Campaign campaign;

    public AARMissionVictoryPanel(Campaign campaign)
	{
        super();

        this.aarCoordinator = AARCoordinator.getInstance();
        this.campaign = campaign;
	}

	public void makePanel() throws PWCGException  
	{
        try
        {
            JTabbedPane eventTabPane = createCrewMemberVictoriesTab();
            createPostCombatReportTabs(eventTabPane);
            this.add(eventTabPane, BorderLayout.WEST);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }
    
    private void createPostCombatReportTabs(JTabbedPane eventTabPane)
    {
        JPanel postCombatPanel = new JPanel(new BorderLayout());
        postCombatPanel.setOpaque(false);

        postCombatPanel.add(eventTabPane, BorderLayout.CENTER);
        this.add(postCombatPanel, BorderLayout.CENTER);
    }

    private JTabbedPane createCrewMemberVictoriesTab() throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        JTabbedPane eventTabPane = new JTabbedPane();
        eventTabPane.setBackground(bgColor);
        eventTabPane.setOpaque(false);
       
        HashMap<String, CampaignReportVictoryGUI> equipmentGuiList = createCrewMemberLostSubTabs() ;
        for (String tabName : equipmentGuiList.keySet())
        {
            eventTabPane.addTab(tabName, equipmentGuiList.get(tabName));
            this.shouldDisplay = true;
        }


        return eventTabPane;
    }

	private HashMap<String, CampaignReportVictoryGUI> createCrewMemberLostSubTabs() throws PWCGException 
	{
	    HashMap<String, List<VictoryEvent>> victoriesByCrewMembersInMyCompany = collateVictoriesByCrewMemberInMyCompany();
        HashMap<String, CampaignReportVictoryGUI> crewMemberVictoryPanelData = createVictoryTabs(victoriesByCrewMembersInMyCompany);
        return crewMemberVictoryPanelData;
	}

    private HashMap<String, List<VictoryEvent>> collateVictoriesByCrewMemberInMyCompany() throws PWCGException
    {
        HashMap<String, List<VictoryEvent>> victoriesByCrewMembersInMyCompany = new HashMap<>();
        List<VictoryEvent>  outOfMissionVictories = aarCoordinator.getAarContext().getUiDebriefData().getOutOfMissionVictoryPanelData().getOutOfMissionVictoryEvents();
        for (VictoryEvent victoryEvent : outOfMissionVictories)
		{
            if (victoryEvent.getCompanyId() == campaign.findReferencePlayer().getCompanyId())
            {
                if (!victoriesByCrewMembersInMyCompany.containsKey(victoryEvent.getCrewMemberName()))
                {
                    List<VictoryEvent> victoriesForCrewMember = new ArrayList<>();
                    victoriesByCrewMembersInMyCompany.put(victoryEvent.getCrewMemberName(), victoriesForCrewMember);
                }
                List<VictoryEvent> victoriesForCrewMember = victoriesByCrewMembersInMyCompany.get(victoryEvent.getCrewMemberName());
                victoriesForCrewMember.add(victoryEvent);
            }
		}
        return victoriesByCrewMembersInMyCompany;
    }

    private HashMap<String, CampaignReportVictoryGUI> createVictoryTabs(HashMap<String, List<VictoryEvent>> victoriesByCrewMembersInMyCompany) throws PWCGException
    {
        HashMap<String, CampaignReportVictoryGUI> crewMemberVictoryPanelData = new HashMap<>();
        for (String crewMemberName : victoriesByCrewMembersInMyCompany.keySet())
        {
            List<VictoryEvent> victoriesForCrewMember = victoriesByCrewMembersInMyCompany.get(crewMemberName);
            CampaignReportVictoryGUI victoryGUI = new CampaignReportVictoryGUI(campaign, victoriesForCrewMember);
            String tabName = "Notification of Victory: " + crewMemberName;
            crewMemberVictoryPanelData.put(tabName, victoryGUI);
        }
        return crewMemberVictoryPanelData;
    }
}
