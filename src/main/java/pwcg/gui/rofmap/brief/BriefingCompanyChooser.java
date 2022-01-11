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

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.mission.Mission;
import pwcg.mission.unit.ITankUnit;

public class BriefingCompanyChooser implements ActionListener
{
    private Mission mission;
    private IUnitChanged flightChanged;
    private JPanel flightChooserPanel;

    private ButtonGroup flightChooserButtonGroup = new ButtonGroup();
    private Map<Integer, ButtonModel> flightChooserButtonModels = new HashMap<>();

    public BriefingCompanyChooser(Mission mission, IUnitChanged flightChanged)
    {
        this.mission = mission;
        this.flightChanged = flightChanged;
    }
    
    public void createBriefingCompanySelectPanel() throws PWCGException
    {        
        JPanel flightChooserButtonPanelGrid = new JPanel(new GridLayout(0,1));
        flightChooserButtonPanelGrid.setOpaque(false);

        JLabel spacerLabel1 = PWCGLabelFactory.makeDummyLabel();        
        flightChooserButtonPanelGrid.add(spacerLabel1);

        JLabel spacerLabel2 = PWCGLabelFactory.makeDummyLabel();        
        flightChooserButtonPanelGrid.add(spacerLabel2);

        JLabel spacerLabel3 = PWCGLabelFactory.makeDummyLabel();        
        flightChooserButtonPanelGrid.add(spacerLabel3);

        Map<Integer, Company> playerCompanysInMission = new HashMap<>();
        for (ITankUnit playerUnit : mission.getUnits().getPlayerUnits())
        {
            Company company = playerUnit.getCompany();
            playerCompanysInMission.put(company.getCompanyId(), company);
        }

        for (Company company : playerCompanysInMission.values())
        {
            JRadioButton airLowDensity = PWCGButtonFactory.makeRadioButton(
                    company.determineDisplayName(mission.getCampaign().getDate()), 
                    "FlightChanged:" + company.getCompanyId(),
                    "Select company to change context", 
                    null, 
                    ColorMap.CHALK_FOREGROUND,
                    false, this);       
            flightChooserButtonPanelGrid.add(airLowDensity);
            ButtonModel model = airLowDensity.getModel();
            flightChooserButtonGroup.add(airLowDensity);
            flightChooserButtonModels.put(company.getCompanyId(), model);
        }

        flightChooserPanel = new JPanel(new BorderLayout());
        flightChooserPanel.setOpaque(false);
        flightChooserPanel.add(flightChooserButtonPanelGrid, BorderLayout.SOUTH);

        JPanel shapePanel = new JPanel(new BorderLayout());
        shapePanel.setOpaque(false);

        shapePanel.add(flightChooserButtonPanelGrid, BorderLayout.NORTH);
        flightChooserPanel.add(shapePanel, BorderLayout.CENTER);
    }

    public void setSelectedButton(int companyId)
    {
        ButtonModel model = flightChooserButtonModels.get(companyId);
        flightChooserButtonGroup.setSelected(model, true);
    }

    public JPanel getFlightChooserPanel()
    {
        return flightChooserPanel;
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
            Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
            
            setSelectedButton(companyId);

            flightChanged.unitChanged(company);
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }

    }
}
