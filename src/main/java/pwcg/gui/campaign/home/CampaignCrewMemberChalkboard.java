package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignCrewMemberChalkboard extends ImageResizingPanel
{    
    private static final long serialVersionUID = 1L;

    public CampaignCrewMemberChalkboard()
    {
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
    }
    
    public void makePanels(List<CrewMember> sortedCrewMembers) throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignCrewMemberChalkboard);
        this.setImageFromName(imagePath);
        this.setBorder(PwcgBorderFactory.createCampaignHomeChalkboardBoxBorder());        

        JPanel equipmentPanel = createCrewMemberListPanel(sortedCrewMembers);
        this.add(equipmentPanel, BorderLayout.CENTER);
    }

    private JPanel createCrewMemberListPanel(List<CrewMember> sortedCrewMembers) throws PWCGException
    {
        Font font = PWCGMonitorFonts.getChalkboardFont();

        GridBagConstraints constraints = initializeGridbagConstraints();
        JPanel companyPanel = createChalkboardHeader(constraints, font);
        addCrewMembersToChalkBoard(sortedCrewMembers, companyPanel, constraints, font);

        return companyPanel;
    }

    private GridBagConstraints initializeGridbagConstraints()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipadx = 3;
        constraints.ipady = 3;
        constraints.anchor = GridBagConstraints.NORTH;
        return constraints;
    }

    private JPanel createChalkboardHeader(GridBagConstraints constraints, Font font) throws PWCGException
    {
        JPanel companyPanel = new JPanel();
        companyPanel.setOpaque(false);
        GridBagLayout companyLayout = new GridBagLayout();                
        companyPanel.setLayout(companyLayout);

        JLabel lLeftSpace = PWCGLabelFactory.makeTransparentLabel("   ", ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.15;
        constraints.gridx = 0;
        constraints.gridy = 0;
        companyPanel.add(lLeftSpace, constraints);

        String lCrewMemberLabelText = InternationalizationManager.getTranslation("CrewMember");
        JLabel lCrewMemberLabel = PWCGLabelFactory.makeTransparentLabel(lCrewMemberLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
        constraints.weightx = 0.20;
        constraints.gridx = 1;
        constraints.gridy = 0;
        companyPanel.add(lCrewMemberLabel, constraints);

        String lMissionLabelText = InternationalizationManager.getTranslation("Missions");
        JLabel lMissionLabel = PWCGLabelFactory.makeTransparentLabel(lMissionLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
        constraints.weightx = 0.1;
        constraints.gridx = 2;
        constraints.gridy = 0;
        companyPanel.add(lMissionLabel, constraints);

        String lAirLabelText = InternationalizationManager.getTranslation("Air");
        JLabel lAirLabel = PWCGLabelFactory.makeTransparentLabel(lAirLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
        constraints.weightx = 0.1;
        constraints.gridx = 3;
        constraints.gridy = 0;
        companyPanel.add(lAirLabel, constraints);

        String lTankLabelText = InternationalizationManager.getTranslation("Tank");
        JLabel lTankLabel = PWCGLabelFactory.makeTransparentLabel(lTankLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
        constraints.weightx = 0.1;
        constraints.gridx = 4;
        constraints.gridy = 0;
        companyPanel.add(lTankLabel, constraints);

        String lTrainLabelText = InternationalizationManager.getTranslation("Train");
        JLabel lTrainLabel = PWCGLabelFactory.makeTransparentLabel(lTrainLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
        constraints.weightx = 0.1;
        constraints.gridx = 5;
        constraints.gridy = 0;
        companyPanel.add(lTrainLabel, constraints);

        String lGroundLabelText = InternationalizationManager.getTranslation("Ground");
        JLabel lGroundLabel = PWCGLabelFactory.makeTransparentLabel(lGroundLabelText, ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
        constraints.weightx = 0.1;
        constraints.gridx = 6;
        constraints.gridy = 0;
        companyPanel.add(lGroundLabel, constraints);
        
        JLabel lRightSpace = PWCGLabelFactory.makeTransparentLabel("   ", ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
        constraints.weightx = 0.15;
        constraints.gridx = 7;
        constraints.gridy = 0;
        companyPanel.add(lRightSpace, constraints);

        return companyPanel;
    }

    private void addCrewMembersToChalkBoard(List<CrewMember> sortedCrewMembers, JPanel companyPanel, GridBagConstraints constraints, Font font) throws PWCGException
    {
        int gridbagRow = 1;
        for (CrewMember crewMember : sortedCrewMembers)
        {

            JLabel lLeftSpace = PWCGLabelFactory.makeTransparentLabel("   ", ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
            constraints.weightx = 0.15;
            constraints.gridx = 0;
            constraints.gridy = gridbagRow;
            companyPanel.add(lLeftSpace, constraints);
            
            JLabel crewMemberLabel = PWCGLabelFactory.makeTransparentLabel(
                    crewMember.getNameAndRank(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.LEFT);
            constraints.weightx = 0.20;
            constraints.gridx = 1;
            constraints.gridy = gridbagRow;
            companyPanel.add(crewMemberLabel, constraints);

            JLabel missionsCompletedLabel = PWCGLabelFactory.makeTransparentLabel(
                    "" + crewMember.getBattlesFought(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 2;
            constraints.gridy = gridbagRow;
            companyPanel.add(missionsCompletedLabel, constraints);
            
            JLabel airToAirVictoryLabel = PWCGLabelFactory.makeTransparentLabel(
                    "" + crewMember.getCrewMemberVictories().getAirToAirVictoryCount(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 3;
            constraints.gridy = gridbagRow;
            companyPanel.add(airToAirVictoryLabel, constraints);
            
            JLabel tankVictoryLabel = PWCGLabelFactory.makeTransparentLabel(
                    "" + crewMember.getCrewMemberVictories().getTankVictoryCount(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 4;
            constraints.gridy = gridbagRow;
            companyPanel.add(tankVictoryLabel, constraints);
            
            JLabel trainVictoryLabel = PWCGLabelFactory.makeTransparentLabel(
                    "" + crewMember.getCrewMemberVictories().getTrainVictoryCount(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 5;
            constraints.gridy = gridbagRow;
            companyPanel.add(trainVictoryLabel, constraints);
            
            JLabel groundVictoryLabel = PWCGLabelFactory.makeTransparentLabel(
                    "" + crewMember.getCrewMemberVictories().getGroundVictoryCount(), ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 6;
            constraints.gridy = gridbagRow;
            companyPanel.add(groundVictoryLabel, constraints);
            
            JLabel lRightSpace = PWCGLabelFactory.makeTransparentLabel("   ", ColorMap.CHALK_FOREGROUND, font, SwingConstants.RIGHT);
            constraints.weightx = 0.1;
            constraints.gridx = 7;
            constraints.gridy = 0;
            companyPanel.add(lRightSpace, constraints);

            ++gridbagRow;
        }
    }
}
