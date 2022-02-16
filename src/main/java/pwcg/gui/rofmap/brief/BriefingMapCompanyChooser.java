package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.rofmap.brief.model.IBriefingPlatoon;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.mission.ICompanyMission;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingMapCompanyChooser implements ActionListener
{
    private Mission mission;
    private IPlatoonChanged platoonChanged;
    private JPanel platoonChooserPanel;

    private ButtonGroup platoonChooserButtonGroup = new ButtonGroup();
    private Map<Integer, ButtonModel> platoonChooserButtonModels = new HashMap<>();

    public BriefingMapCompanyChooser(Mission mission, IPlatoonChanged platoonChanged)
    {
        this.mission = mission;
        this.platoonChanged = platoonChanged;
    }

    public void createBriefingCompanySelectPanel() throws PWCGException
    {
        JPanel platoonChooserButtonPanelGrid = new JPanel(new GridLayout(0,1));
        platoonChooserButtonPanelGrid.setOpaque(false);

        JLabel spacerLabel1 = PWCGLabelFactory.makeDummyLabel();
        platoonChooserButtonPanelGrid.add(spacerLabel1);

        JLabel spacerLabel2 = PWCGLabelFactory.makeDummyLabel();
        platoonChooserButtonPanelGrid.add(spacerLabel2);

        JLabel spacerLabel3 = PWCGLabelFactory.makeChalkBoardLabel("Selected Map Company");
        platoonChooserButtonPanelGrid.add(spacerLabel3);

        IBriefingPlatoon playerBriefingPlatoon = BriefingContext.getInstance().getBriefingData().getActiveBriefingPlayerPlatoon();
        for (ITankPlatoon platoon : mission.getPlatoons().getPlatoons())
        {
            if (platoon.getCompany().determineSide() == playerBriefingPlatoon.getSide())
            {
                ICompanyMission company = platoon.getCompany();
                addPlatoonChoiceButton(platoonChooserButtonPanelGrid, company);
            }
        }

        platoonChooserPanel = new JPanel(new BorderLayout());
        platoonChooserPanel.setOpaque(false);
        platoonChooserPanel.add(platoonChooserButtonPanelGrid, BorderLayout.SOUTH);

        JPanel shapePanel = new JPanel(new BorderLayout());
        shapePanel.setOpaque(false);

        shapePanel.add(platoonChooserButtonPanelGrid, BorderLayout.NORTH);
        platoonChooserPanel.add(shapePanel, BorderLayout.CENTER);

        setSelectedButton(playerBriefingPlatoon.getCompanyId());
    }

    private void addPlatoonChoiceButton(JPanel platoonChooserButtonPanelGrid, ICompanyMission company) throws PWCGException
    {
        JRadioButton platoonChoice = PWCGButtonFactory.makeRadioButton(
                company.determineDisplayName(mission.getCampaign().getDate()),
                "CompanyChanged:" + company.getCompanyId(),
                "Select company to change context",
                null,
                ColorMap.CHALK_FOREGROUND,
                false,
                this);
        platoonChooserButtonPanelGrid.add(platoonChoice);
        ButtonModel model = platoonChoice.getModel();
        platoonChooserButtonGroup.add(platoonChoice);
        platoonChooserButtonModels.put(company.getCompanyId(), model);
    }

    public void setSelectedButton(int companyId)
    {
        ButtonModel model = platoonChooserButtonModels.get(companyId);
        platoonChooserButtonGroup.setSelected(model, true);
    }

    public JPanel getFlightChooserPanel()
    {
        return platoonChooserPanel;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            int index = action.indexOf(":");
            String selectedCompanyId = action.substring(index + 1);
            int companyId = Integer.valueOf(selectedCompanyId);

            setSelectedButton(companyId);
            platoonChanged.platoonChanged(companyId);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }

    }
}
