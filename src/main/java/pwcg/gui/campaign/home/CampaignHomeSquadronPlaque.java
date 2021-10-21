package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.InternationalizationManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGLabelFactory;
import pwcg.gui.utils.PwcgBorderFactory;

public class CampaignHomeSquadronPlaque extends JPanel
{
    private static final long serialVersionUID = 1L;

    private Campaign campaign;
    
    public CampaignHomeSquadronPlaque(Campaign campaign)  
    {
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaign = campaign;
    }


    public void makeDescPanel(int squadronId) throws PWCGException 
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignHomeSquadronPlaque);
        ImageResizingPanel plaquePanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        plaquePanel.setLayout(new BorderLayout());
        plaquePanel.setBorder(PwcgBorderFactory.createPlaqueBackgroundBorder());

        JPanel descGridPanel = new JPanel(new GridLayout(0, 1));
        descGridPanel.setOpaque(false);
        
        Font font = PWCGMonitorFonts.getPrimaryFont();
                
        String spacing = "         ";
                
        descGridPanel.add(PWCGLabelFactory.makeDummyLabel());
        descGridPanel.add(PWCGLabelFactory.makeDummyLabel());

        Squadron squadron =  PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);

        String assignedToString = InternationalizationManager.getTranslation("Assigned to");
        JLabel lAssignedTo = PWCGLabelFactory.makeLabel(assignedToString, ColorMap.CHALK_BACKGROUND, ColorMap.PLAQUE_GOLD, font, SwingConstants.LEFT);
        descGridPanel.add(lAssignedTo);

        String squadString = spacing + squadron.determineDisplayName(campaign.getDate());
        JLabel lSquad = PWCGLabelFactory.makeLabel(squadString, ColorMap.CHALK_BACKGROUND, ColorMap.PLAQUE_GOLD, font, SwingConstants.LEFT);
        descGridPanel.add(lSquad);

        String airfieldAtString = InternationalizationManager.getTranslation("Stationed at");
        airfieldAtString += " " + squadron.determineCurrentAirfieldName(campaign.getDate());
        JLabel lAirfieldAt = PWCGLabelFactory.makeLabel(airfieldAtString, ColorMap.CHALK_BACKGROUND, ColorMap.PLAQUE_GOLD, font, SwingConstants.LEFT);
        descGridPanel.add(lAirfieldAt);

        String airfieldString = spacing + squadron.determineCurrentAirfieldName(campaign.getDate());
        JLabel lAirfield = PWCGLabelFactory.makeLabel(airfieldString, ColorMap.CHALK_BACKGROUND, ColorMap.PLAQUE_GOLD, font, SwingConstants.LEFT);
        descGridPanel.add(lAirfield);
        

        String dateString = spacing + DateUtils.getDateString(campaign.getDate());
        JLabel lDate = PWCGLabelFactory.makeLabel(dateString, ColorMap.CHALK_BACKGROUND, ColorMap.PLAQUE_GOLD, font, SwingConstants.LEFT);
        descGridPanel.add(lDate);


        PlaneType aircraftType = squadron.determineBestPlane(campaign.getDate());
        if (aircraftType != null)
        {
            String aircraftString = spacing + InternationalizationManager.getTranslation("Flying the");
            aircraftString += " " + aircraftType.getDisplayName();
            JLabel lAircraft = PWCGLabelFactory.makeLabel(aircraftString, ColorMap.CHALK_BACKGROUND, ColorMap.PLAQUE_GOLD, font, SwingConstants.LEFT);

            descGridPanel.add(lAircraft);
        }
        
        descGridPanel.add(PWCGLabelFactory.makeDummyLabel());
        descGridPanel.add(PWCGLabelFactory.makeDummyLabel());
        
        plaquePanel.add(descGridPanel, BorderLayout.NORTH);
        
        this.add(plaquePanel, BorderLayout.CENTER);
    }

}
