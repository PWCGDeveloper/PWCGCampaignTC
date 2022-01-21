package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.mission.MissionGeneratorHelper;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingUnit;
import pwcg.gui.rofmap.brief.model.BriefingUnitParameters;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.mission.ICompanyMission;
import pwcg.mission.Mission;
import pwcg.mission.mcu.McuWaypoint;

public class BriefingMapGUI extends MapGUI implements ActionListener, IUnitChanged, IBriefingCompanySelectedCallback
{
	private static final long serialVersionUID = 1L;

    private CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper;
    private Mission mission;
    private BriefingData briefingData;
    private BriefingCompanyChooser briefingFlightChooser;
    private BriefingMapPanel mapPanel;

	public BriefingMapGUI(Campaign campaign, CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper) throws PWCGException  
	{
		super(campaign.getDate());
		
        this.campaignHomeGuiBriefingWrapper =  campaignHomeGuiBriefingWrapper;
        this.briefingData =  BriefingContext.getInstance().getBriefingData();
        this.mission =  briefingData.getMission();

		setLayout(new BorderLayout());		
	}    

	public void makePanels() 
	{
		try
		{
            briefingFlightChooser = new BriefingCompanyChooser(mission, this);
            briefingFlightChooser.createBriefingCompanySelectPanel();

			Color bg = ColorMap.MAP_BACKGROUND;
			setOpaque(false);
			setBackground(bg);
			
            this.add(BorderLayout.WEST, makeNavPanel());           
            this.add(BorderLayout.CENTER, createCenterPanel());
            
            Point initialPosition = findCenterPosition();
            centerMapAt(initialPosition);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private JPanel createCenterPanel() throws PWCGException
    {
        JPanel briefingMapCenterPanel = new JPanel(new BorderLayout());
        createMapPanel();
        briefingMapCenterPanel.add(mapScroll.getMapScrollPane(), BorderLayout.CENTER);

        return briefingMapCenterPanel;
    }

    private void createMapPanel() throws PWCGException
    {
        BriefingUnit activeMissionHandler = briefingData.getActiveBriefingUnit();
        
        if (mapPanel != null)
        {
            this.remove(mapPanel);
        }
        
        mapPanel = new BriefingMapPanel(this);
        mapScroll = new MapScroll(mapPanel);  
        mapPanel.setMapBackground(100);

        BriefingMapFlightMapper flightMapper = new BriefingMapFlightMapper(activeMissionHandler, mapPanel);
        flightMapper.mapRequestedFlights();
    }

    private Point findCenterPosition()
    {
        Coordinate initialPosition = briefingData.getActiveBriefingUnit().getBriefingUnitParameters().getBriefingMapMapPoints().get(0).getPosition();
        Point mapPoint = mapPanel.coordinateToPoint(initialPosition);
        return mapPoint;
    }

    private JPanel makeNavPanel() throws PWCGException 
    {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        JPanel buttonPanel = makeButtonPanel();
        leftPanel.add(buttonPanel, BorderLayout.NORTH);
        leftPanel.add(briefingFlightChooser.getFlightChooserPanel(), BorderLayout.CENTER);
        return leftPanel;
    }
    
	private JPanel makeButtonPanel() throws PWCGException 
	{
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setOpaque(false);

		JPanel buttonGrid = new JPanel();
		buttonGrid.setLayout(new GridLayout(0,1));
		buttonGrid.setOpaque(false);
	    
        if (mission.getFinalizer().isFinalized())
        {
            buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
            JButton backToCampaignButton = makeButton("Back to Campaign", "Back to Campaign", "Return to campaign home screen");
            buttonGrid.add(backToCampaignButton);
        }

		buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
        JButton scrubMissionButton = makeButton("Scrub Mission", "Scrub Mission", "Scrub this mission and return to campaign home screen");
        buttonGrid.add(scrubMissionButton);

        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
        JButton goBackToBriefingDescriptionButton = makeButton("Back: Briefing", "Back: Briefing", "Go back to briefing description screen");
        buttonGrid.add(goBackToBriefingDescriptionButton);

        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
        JButton goToCrewMemberSelectionButton = makeButton("Next: CrewMembers", "Next: CrewMembers", "Progress to crewMember selection screen");
        buttonGrid.add(goToCrewMemberSelectionButton);

        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());
        buttonGrid.add(PWCGLabelFactory.makeDummyLabel());

		buttonPanel.add(buttonGrid, BorderLayout.NORTH);
		
        BriefingMapCompanySelector companySelector = new BriefingMapCompanySelector(mission, this, briefingData);
        JPanel friendlyCompanySelectorPanel = companySelector.makeComboBox();
		buttonPanel.add(friendlyCompanySelectorPanel, BorderLayout.CENTER);
		
		return buttonPanel;
	}

    private JButton makeButton(String buttonText, String command, String toolTipText) throws PWCGException
    {
        return PWCGButtonFactory.makeTranslucentMenuButton(buttonText, command, toolTipText, this);
    }

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{		
		try
		{
			String action = arg0.getActionCommand();
			
            if (action.equals("Back: Briefing"))
            {
                backToBriefingDescription();
            }
            else if (action.equals("Next: CrewMembers"))
            {
                forwardToCrewSelection();
            }
            else if (action.equals("Back to Campaign"))
            {
                backToCampaign();
            }
            else if (action.equals("Scrub Mission"))
            {
                MissionGeneratorHelper.scrubMission(mission.getCampaign(), campaignHomeGuiBriefingWrapper);
            }
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
    
    private void backToCampaign() throws PWCGException
    {
        campaignHomeGuiBriefingWrapper.refreshCampaignPage();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private void backToBriefingDescription() throws PWCGException
    {
        CampaignGuiContextManager.getInstance().popFromContextStack();
        return;
    }

    private void forwardToCrewSelection() throws PWCGException 
    {
        BriefingCrewMemberSelectionScreen crewMemberSelection = new BriefingCrewMemberSelectionScreen(campaignHomeGuiBriefingWrapper);
        crewMemberSelection.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(crewMemberSelection);
    }

    @Override
    public void unitChanged(ICompanyMission company) throws PWCGException
    {
        if (!isChangedCompanySameSide(briefingData.getSelectedUnit().getCompany(), company))
        {
            briefingData.clearAiFlightsToDisplay();
        }
        
        briefingData.changeSelectedUnit(company.getCompanyId());
        refreshAllPanels();           

    }

    private void refreshAllPanels() throws PWCGException
    {
        this.add(BorderLayout.CENTER, createCenterPanel());
        this.add(BorderLayout.WEST, makeNavPanel());
    }
    
    private boolean isChangedCompanySameSide(ICompanyMission before, ICompanyMission after) throws PWCGException
    {
        if (before.determineSide() == after.determineSide())
        {
            return true;
        }
        return false;
    }

    @Override
    public void companiesSelectedChanged(Map<Integer, String> aiFlightsToDisplay) throws PWCGException
    {
        briefingData.setAiFlightsToDisplay(aiFlightsToDisplay);
        this.add(BorderLayout.CENTER, createCenterPanel());
        
        refreshMapScreen();
    }

    public void waypointRemovedNotification(long waypointID) throws PWCGException
    {
        if (waypointID != McuWaypoint.NO_WAYPOINT_ID)
        {
            BriefingUnitParameters briefingFlightParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingUnit().getBriefingUnitParameters();
            briefingFlightParameters.removeBriefingMapMapPointsAtPosition();
            
            refreshMapScreen();
        }
    }

    public void waypointAddedNotification(long waypointID) throws PWCGException
    {
        if (waypointID != McuWaypoint.NO_WAYPOINT_ID)
        {
            BriefingUnitParameters briefingFlightParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingUnit().getBriefingUnitParameters();
            briefingFlightParameters.addBriefingMapMapPointsAtPosition();
            
            refreshMapScreen();
        }
    }

    private void refreshMapScreen()
    {
        this.revalidate();
        this.repaint();
    }
}
