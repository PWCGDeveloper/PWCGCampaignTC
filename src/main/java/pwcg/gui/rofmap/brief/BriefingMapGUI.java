package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.mission.MissionGeneratorHelper;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingPlatoonParameters;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.mission.Mission;
import pwcg.mission.mcu.McuWaypoint;

public class BriefingMapGUI extends MapGUI implements ActionListener, IPlatoonChanged
{
    private static final long serialVersionUID = 1L;

    private CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper;
    private Mission mission;
    private BriefingData briefingData;
    private BriefingMapCompanyChooser briefingMapPlatoonChooser;
    private BriefingMapPanel mapPanel;

    public BriefingMapGUI(Campaign campaign, CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper) throws PWCGException
    {
        super(campaign.getDate());

        this.campaignHomeGuiBriefingWrapper =  campaignHomeGuiBriefingWrapper;
        this.briefingData =  BriefingContext.getInstance().getBriefingData();
        this.mission = briefingData.getMission();

        setLayout(new BorderLayout());
    }

    public void makePanels()
    {
        try
        {
            briefingMapPlatoonChooser = new BriefingMapCompanyChooser(mission, this);
            briefingMapPlatoonChooser.createBriefingCompanySelectPanel();

            Color bg = ColorMap.MAP_BACKGROUND;
            setOpaque(false);
            setBackground(bg);

            this.add(BorderLayout.WEST, makeNavPanel());
            this.add(BorderLayout.CENTER, createCenterPanel());
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
        briefingMapCenterPanel.add(mapPanel, BorderLayout.CENTER);

        return briefingMapCenterPanel;
    }

    private void createMapPanel() throws PWCGException
    {
        if (mapPanel != null)
        {
            this.remove(mapPanel);
        }

        mapPanel = new BriefingMapPanel(this);
        mapPanel.drawDisplayMap();
    }

    private JPanel makeNavPanel() throws PWCGException
    {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        JPanel buttonPanel = makeButtonPanel();
        leftPanel.add(buttonPanel, BorderLayout.NORTH);
        leftPanel.add(briefingMapPlatoonChooser.getFlightChooserPanel(), BorderLayout.CENTER);
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
    public void platoonChanged(int companyId) throws PWCGException
    {
        briefingData.setSelectedMapEditPlatoon(companyId);
        refreshMapScreen();

    }

    public void waypointRemovedNotification(long waypointID) throws PWCGException
    {
        if (waypointID != McuWaypoint.NO_WAYPOINT_ID)
        {
            BriefingPlatoonParameters briefingPlatoonParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingMapPlatoon().getBriefingPlatoonParameters();
            briefingPlatoonParameters.removeBriefingMapMapPointsAtPosition();

            refreshMapScreen();
        }
    }

    public void waypointAddedNotification(long waypointID) throws PWCGException
    {
        if (waypointID != McuWaypoint.NO_WAYPOINT_ID)
        {
            BriefingPlatoonParameters briefingPlatoonParameters = BriefingContext.getInstance().getBriefingData().getActiveBriefingMapPlatoon().getBriefingPlatoonParameters();
            briefingPlatoonParameters.addBriefingMapMapPointsAtPosition();

            refreshMapScreen();
        }
    }

    private void refreshMapScreen() throws PWCGException
    {
        this.mapPanel.drawDisplayMap();
        this.revalidate();
        this.repaint();
    }
}
