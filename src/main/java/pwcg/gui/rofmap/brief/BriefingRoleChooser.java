package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyRoleWeight;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.mission.MissionGeneratorHelper;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;
import pwcg.mission.MissionHumanParticipants;

public class BriefingRoleChooser extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;
    
    private Campaign campaign;
    private CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper;
    private MissionHumanParticipants participatingPlayers;
    private Map<Integer, JComboBox<String>> companyToRoleMapping = new HashMap<>();

    public BriefingRoleChooser(
            Campaign campaign, 
            CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper, 
            MissionHumanParticipants participatingPlayers)
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.campaign = campaign;
        this.campaignHomeGuiBriefingWrapper = campaignHomeGuiBriefingWrapper;
        this.participatingPlayers = participatingPlayers;
    }
    
    public void makePanels() 
    {
        try
        {
            String imagePath = UiImageResolver.getImage(ScreenIdentifier.BriefingRoleChooser);
            this.setImageFromName(imagePath);

            this.add(BorderLayout.WEST, makeNavigatePanel());
            this.add(BorderLayout.CENTER, makeCenterPanel());
            this.add(BorderLayout.EAST, SpacerPanelFactory.makeDocumentSpacerPanel(1400));
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

	public JPanel makeNavigatePanel() throws PWCGException  
    {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);

        JButton missionButton = makeMenuButton("Generate Mission", "CreateMission", "Create a mission");
        buttonPanel.add(missionButton);

        JButton scrubButton = makeMenuButton("Scrub Mission", "ScrubMission", "Scrub this mission");
        buttonPanel.add(scrubButton);

        navPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return navPanel;
    }

    private JPanel makeCenterPanel() throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel roleSelectionPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        roleSelectionPanel.setBorder(PwcgBorderFactory.createDocumentBorderWithExtraSpaceFromTop());

        roleSelectionPanel.setLayout(new BorderLayout());
        roleSelectionPanel.setOpaque(false);
        
        JPanel roleSelectionGrid = new JPanel();
        roleSelectionGrid.setOpaque(false);
        roleSelectionGrid.setLayout(new GridLayout(0, 2));

        for (CrewMember participatingPlayer : participatingPlayers.getAllParticipatingPlayers())
        {
            int companyId = participatingPlayer.getCompanyId();
            
            JLabel companyNameLabel = makeCompanyNameLabel(companyId);
            JComboBox<String> roleSelector = makeRoleSelectorForCompany(companyId);
            roleSelectionGrid.add(companyNameLabel);
            roleSelectionGrid.add(roleSelector);

            companyToRoleMapping.put(companyId, roleSelector);
        }
        
        roleSelectionPanel.add(roleSelectionGrid, BorderLayout.NORTH);
        return roleSelectionPanel;
    }

    private JLabel makeCompanyNameLabel(int companyId) throws PWCGException
    {        
        Font font = PWCGMonitorFonts.getPrimaryFont();
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
        JLabel companyNameLabel = PWCGLabelFactory.makeTransparentLabel(
                company.determineDisplayName(campaign.getDate()), ColorMap.PAPER_FOREGROUND, font, SwingConstants.LEFT);
        return companyNameLabel;
    }

    private JComboBox<String> makeRoleSelectorForCompany(int companyId) throws PWCGException
    {
        JComboBox<String> roleSelector = new JComboBox<String>();
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);

        roleSelector.addItem(PwcgRole.ROLE_NONE.getRoleDescription());

        for (CompanyRoleWeight weightedRoles : company.getCompanyRoles().selectRoleSetByDate(campaign.getDate()).getWeightedRoles())
        {
            roleSelector.addItem(weightedRoles.getRole().getRoleDescription());
        }
        
        roleSelector.setSelectedIndex(0);
        return roleSelector;
    }

    private JButton makeMenuButton(String buttonText, String command, String toolTipText) throws PWCGException
    {
        return PWCGButtonFactory.makeTranslucentMenuButton(buttonText, command, toolTipText, this);
    }

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            if (action.equalsIgnoreCase("CreateMission"))
            {
                Map<Integer, PwcgRole> companyRoleOverride = buildRoleOverrideMap();
                MissionGeneratorHelper.showBriefingMap(campaign, campaignHomeGuiBriefingWrapper, participatingPlayers, companyRoleOverride);
            }
            else if (action.equalsIgnoreCase("ScrubMission"))
            {
                MissionGeneratorHelper.scrubMission(campaign, campaignHomeGuiBriefingWrapper);
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private Map<Integer, PwcgRole> buildRoleOverrideMap()
    {
        Map<Integer, PwcgRole> companyRoleOverride = new HashMap<>();
        for (int companyId : companyToRoleMapping.keySet())
        {
            JComboBox<String> roleSelector = companyToRoleMapping.get(companyId);
            String roleDescription = (String) roleSelector.getSelectedItem();
            PwcgRole role = PwcgRole.getRoleFromDescription(roleDescription);
            if (role != null && role != PwcgRole.ROLE_NONE)
            {
                companyRoleOverride.put(companyId, role);
            }
        }
        return companyRoleOverride;
    }
}


