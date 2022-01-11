package pwcg.gui.campaign.intel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankSorter;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageToDisplaySizer;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignIntelligenceCompanyDetailsPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    private JTextArea companyIntelHeaderText;
    private JTextArea companyIntelPersonnelText;
    private JTextArea companyIntelEquipmentText;

    public CampaignIntelligenceCompanyDetailsPanel(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public void makePanel() throws PWCGException
    {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

        JPanel companyDetailsPanel = makeCompanyDetailsPanel();
        this.add(companyDetailsPanel, BorderLayout.CENTER);

        ImageToDisplaySizer.setDocumentSize(this);
    }

    public void setCompanyIntelText(int companyId) throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
        
        String companyIntelHeaderDesc = formCompanyHeaderDesc(company);
        companyIntelHeaderText.setText(companyIntelHeaderDesc);
        
        String companyPersonnelDesc = formCompanyPersonnelDesc(company);
        companyIntelPersonnelText.setText(companyPersonnelDesc);

        String companyEquipmentDesc = formCompanyEquipmentDesc(company);
        companyIntelEquipmentText.setText(companyEquipmentDesc);
    }

    private JPanel makeCompanyDetailsPanel() throws PWCGException
    {
        ImageResizingPanel companyDetailsPanel = new ImageResizingPanel("");
        companyDetailsPanel.setOpaque(false);
        companyDetailsPanel.setLayout(new BorderLayout());
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        companyDetailsPanel.setImageFromName(imagePath);
        companyDetailsPanel.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

        JPanel companyDetailsHeaderPanel = formCompanyIntelHeader();
        companyDetailsPanel.add(companyDetailsHeaderPanel,BorderLayout.NORTH);

        JPanel companyDetailsInfoPanel = formCompanyDetails();
        companyDetailsPanel.add(companyDetailsInfoPanel,BorderLayout.CENTER);

        return companyDetailsPanel;
    }

    private JPanel formCompanyDetails() throws PWCGException
    {
        JPanel companyDetailsPanel = new JPanel(new GridLayout(0, 2));
        companyDetailsPanel.setOpaque(false);

        JPanel companyPersonnelDetails = formCompanyIntelPersonnel();
        companyDetailsPanel.add(companyPersonnelDetails);

        JPanel companyEquipmentDetails = formCompanyIntelEquipment();
        companyDetailsPanel.add(companyEquipmentDetails);
        return companyDetailsPanel;
    }

    private JPanel formCompanyIntelPersonnel() throws PWCGException
    {
        companyIntelPersonnelText = makeCompanyInfoTextArea();
        return WrapTextArea(companyIntelPersonnelText);
    }
    
    private JPanel formCompanyIntelEquipment() throws PWCGException
    {
        companyIntelEquipmentText = makeCompanyInfoTextArea();
        return WrapTextArea(companyIntelEquipmentText);
    }
    
    private JPanel formCompanyIntelHeader() throws PWCGException
    {
        companyIntelHeaderText = makeCompanyInfoTextArea();
        return WrapTextArea(companyIntelHeaderText);
    }

    private JPanel WrapTextArea(JTextArea textArea)
    {
        JPanel intelTextPanel = new JPanel(new BorderLayout());
        intelTextPanel.setOpaque(false);
        intelTextPanel.add(textArea);
        return intelTextPanel;
    }

    private JTextArea makeCompanyInfoTextArea() throws PWCGException
    {
        JTextArea companyIntelText;
        
        Font font = PWCGMonitorFonts.getTypewriterFont();
        companyIntelText = new JTextArea();
        companyIntelText.setFont(font);
        companyIntelText.setOpaque(false);
        companyIntelText.setLineWrap(false);
        companyIntelText.setWrapStyleWord(true);
        companyIntelText.setText("");
        
        return companyIntelText;
    }

    private String formCompanyHeaderDesc(Company company) throws PWCGException
    {
        String stationedAtText = InternationalizationManager.getTranslation("Stationed At");
        String callSignText = InternationalizationManager.getTranslation("Call Sign");

        StringBuffer intelBuffer = new StringBuffer("");
        intelBuffer.append("\n");
        intelBuffer.append("        "  + company.determineDisplayName(campaign.getDate()));          
        intelBuffer.append("\n");
        intelBuffer.append("        " + stationedAtText + ": " + company.determineCurrentAirfieldName(campaign.getDate()));          
        intelBuffer.append("\n");
        intelBuffer.append("        " + callSignText + ": " + company.determineCurrentCallsign(campaign.getDate()));
        intelBuffer.append("\n");
        return intelBuffer.toString();
    }

    private String formCompanyPersonnelDesc(Company company) throws PWCGException
    {
        StringBuffer intelBuffer = new StringBuffer("");
        formPersonnel(company.getCompanyId(), intelBuffer);
        return intelBuffer.toString();
    }

    private String formCompanyEquipmentDesc(Company company) throws PWCGException
    {
        StringBuffer intelBuffer = new StringBuffer("");
        formAircraftInventory(company, intelBuffer);
        intelBuffer.append("\n");
        return intelBuffer.toString();
    }
    
    private void formPersonnel(int companyId, StringBuffer intelBuffer) throws PWCGException
    {
        String personnelText = InternationalizationManager.getTranslation("Personnel");

        intelBuffer.append("\n        " + personnelText + "\n");        
        intelBuffer.append("        ----------------------------------------\n");          

        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(companyId);
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(companyPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        List<CrewMember> sortedCrewMembers = activeCrewMembers.sortCrewMembers(campaign.getDate());
        for (CrewMember crewMember : sortedCrewMembers)
        {
            intelBuffer.append("            " + crewMember.getNameAndRank());          
            intelBuffer.append("\n");          
        }
        
        for(int i = sortedCrewMembers.size(); i < 35; ++i)
        {
            intelBuffer.append("                              \n");          
        }
    }

    private void formAircraftInventory(Company company, StringBuffer intelBuffer) throws PWCGException
    {
        String aircraftInventoryText = InternationalizationManager.getTranslation("Aircraft On Inventory");

        intelBuffer.append("\n        " + aircraftInventoryText + "\n");        
        intelBuffer.append("        ----------------------------------------\n");          
        Map<Integer, EquippedTank> aircraftOnInventory = campaign.getEquipmentManager().getEquipmentForCompany(company.getCompanyId()).getActiveEquippedTanks();
        List<EquippedTank> sortedAircraftOnInventory = TankSorter.sortEquippedTanksByGoodness(new ArrayList<EquippedTank>(aircraftOnInventory.values()));
        for (int i = 0; i < sortedAircraftOnInventory.size(); ++i)
        {
            EquippedTank plane = sortedAircraftOnInventory.get(i);
            intelBuffer.append("            " + plane.getDisplayName());
            intelBuffer.append(".\n");          
        }
    }
}
