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

import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.mission.ICompanyMission;
import pwcg.mission.Mission;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingCompanyChooser implements ActionListener
{
    private Mission mission;
    private IUnitChanged platoonChanged;
    private JPanel platoonChooserPanel;

    private ButtonGroup platoonChooserButtonGroup = new ButtonGroup();
    private Map<Integer, ButtonModel> platoonChooserButtonModels = new HashMap<>();

    public BriefingCompanyChooser(Mission mission, IUnitChanged platoonChanged)
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

        JLabel spacerLabel3 = PWCGLabelFactory.makeDummyLabel();        
        platoonChooserButtonPanelGrid.add(spacerLabel3);

        Map<Integer, ICompanyMission> playerCompanysInMission = new HashMap<>();
        for (ITankPlatoon playerPlatoon : mission.getPlatoons().getPlayerPlatoons())
        {
            ICompanyMission company = playerPlatoon.getCompany();
            playerCompanysInMission.put(company.getCompanyId(), company);
        }

        for (ICompanyMission company : playerCompanysInMission.values())
        {
            JRadioButton airLowDensity = PWCGButtonFactory.makeRadioButton(
                    company.determineDisplayName(mission.getCampaign().getDate()), 
                    "CompanyChanged:" + company.getCompanyId(),
                    "Select company to change context", 
                    null, 
                    ColorMap.CHALK_FOREGROUND,
                    false, this);       
            platoonChooserButtonPanelGrid.add(airLowDensity);
            ButtonModel model = airLowDensity.getModel();
            platoonChooserButtonGroup.add(airLowDensity);
            platoonChooserButtonModels.put(company.getCompanyId(), model);
        }

        platoonChooserPanel = new JPanel(new BorderLayout());
        platoonChooserPanel.setOpaque(false);
        platoonChooserPanel.add(platoonChooserButtonPanelGrid, BorderLayout.SOUTH);

        JPanel shapePanel = new JPanel(new BorderLayout());
        shapePanel.setOpaque(false);

        shapePanel.add(platoonChooserButtonPanelGrid, BorderLayout.NORTH);
        platoonChooserPanel.add(shapePanel, BorderLayout.CENTER);
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
            ICompanyMission company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
            
            setSelectedButton(companyId);

            platoonChanged.unitChanged(company);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }

    }
}
