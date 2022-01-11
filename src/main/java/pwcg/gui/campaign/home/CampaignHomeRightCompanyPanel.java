package pwcg.gui.campaign.home;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.gui.campaign.crewmember.CampaignHomeCrewMemberPanel;

public class CampaignHomeRightCompanyPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private ActionListener campaignHome;
    private Campaign campaign;
    
    public CampaignHomeRightCompanyPanel(Campaign campaign, ActionListener campaignHome)  
    {
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        
        this.campaign = campaign;
        this.campaignHome = campaignHome;
    }

    public void makePanel(List<CrewMember>sortedCrewMembers, int companyId) throws PWCGException
    {
        JPanel crewMemberListPanel = makeCrewMemberPanel(sortedCrewMembers);
        this.add(crewMemberListPanel, BorderLayout.NORTH);

        JPanel plaquePanel = makePlaquePanel(companyId);
        this.add(plaquePanel, BorderLayout.SOUTH);
    }

    private JPanel makeCrewMemberPanel(List<CrewMember>sortedCrewMembers) throws PWCGException
    {
        CampaignHomeCrewMemberPanel crewMemberList = new CampaignHomeCrewMemberPanel(campaignHome);
        crewMemberList.makePanel(sortedCrewMembers, "  Roster", "CampFlowCrewMember:");
        return crewMemberList;
    }

    private JPanel makePlaquePanel(int companyId) throws PWCGException 
    {
        CampaignHomeCompanyPlaque companyPlaque = new CampaignHomeCompanyPlaque(campaign);
        companyPlaque.makeDescPanel(companyId);
        return companyPlaque;
    }
}
