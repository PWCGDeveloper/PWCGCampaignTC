package pwcg.gui.campaign.home;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public class CampaignHomeRightPanelFactory
{
    public static JPanel makeCampaignHomeCompanyRightPanel(Campaign campaign, ActionListener actionListener, List<CrewMember> crewMembers, int companyId) throws PWCGException
    {
        CampaignHomeRightCompanyPanel companyPanel = new CampaignHomeRightCompanyPanel(campaign, actionListener);
        companyPanel.makePanel(crewMembers, companyId);
        return companyPanel;
    }
    
    public static JPanel makeCampaignHomeCrewMembersRightPanel(ActionListener actionListener, List<CrewMember> crewMembers) throws PWCGException
    {
        CampaignHomeRightCompanyNoPlaquePanel companyPanel = new CampaignHomeRightCompanyNoPlaquePanel(actionListener);
        companyPanel.makePanel(crewMembers);
        return companyPanel;
    }
    
    public static JPanel makeCampaignHomeAcesRightPanel(CampaignHomeScreen campaignHome, List<CrewMember> aces) throws PWCGException
    {
        CampaignHomeRightAcesPanel acesPanel = new CampaignHomeRightAcesPanel(campaignHome);
        acesPanel.makePanel(aces);
        return acesPanel;
    }
}
