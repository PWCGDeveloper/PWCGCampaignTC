package pwcg.aar.ui.events.model;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class PlaneStatusEvent extends AARCrewMemberEvent
{
    private int crewMemberSerialNumber;
    private int tankSerialNumber;
    private String companyName;
	private int planeStatus;
	
    public PlaneStatusEvent(Campaign campaign, LogTank lostPlane, int planeStatus, boolean isNewsWorthy)
    {
        super(campaign, lostPlane.getCompanyId(), lostPlane.getCrewMemberSerialNumber(), campaign.getDate(), isNewsWorthy);
        this.crewMemberSerialNumber = lostPlane.getCrewMemberSerialNumber();
        this.tankSerialNumber = lostPlane.getTankSerialNumber();
        this.planeStatus = planeStatus;
        
        try
        {
            CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
            Company company = companyManager.getCompany(super.getCompanyId());
            if (company != null)
            {
                this.companyName = company.determineDisplayName(campaign.getDate());
            }
        }
        catch (Exception e)
        {
            this.companyName = "";
            e.printStackTrace();
        }
    }

    public String getPlaneLostText(Campaign campaign) throws PWCGException
    {
        CampaignEquipmentManager campaignEquipmentManager = campaign.getEquipmentManager();
        EquippedTank shotDownPlane = campaignEquipmentManager.destroyTank(tankSerialNumber, campaign.getDate());

        CampaignPersonnelManager campaignPersonnelManager = campaign.getPersonnelManager();
        CrewMember shotDownCrewMember = campaignPersonnelManager.getAnyCampaignMember(super.getCrewMemberSerialNumber());

        String prettyDate = DateUtils.getDateStringPretty(campaign.getDate());
        String planeEventText = 
                "A " + shotDownPlane.getDisplayName() +
                ",  serial number " + tankSerialNumber + 
                ",  operated by " + shotDownCrewMember.getNameAndRank() + 
                " has been lost in combat on " + prettyDate + ".\n";    ;                

        return planeEventText;
    }

    public String getPlaneAddedToDepotText(Campaign campaign) throws PWCGException
    {
        CampaignEquipmentManager campaignEquipmentManager = campaign.getEquipmentManager();
        EquippedTank shotDownPlane = campaignEquipmentManager.destroyTank(tankSerialNumber, campaign.getDate());

        String prettyDate = DateUtils.getDateStringPretty(campaign.getDate());
        String planeEventText = 
                "A " + shotDownPlane.getDisplayName() +
                ",  serial number " + tankSerialNumber + 
                " has been provided to the depot for distribution to front line units on " + prettyDate + ".\n";               

        return planeEventText;
    }

    public String getPlaneWithdrawnFromServiceText(Campaign campaign) throws PWCGException
    {
        CampaignEquipmentManager campaignEquipmentManager = campaign.getEquipmentManager();
        EquippedTank shotDownPlane = campaignEquipmentManager.destroyTank(tankSerialNumber, campaign.getDate());

        String prettyDate = DateUtils.getDateStringPretty(campaign.getDate());
        String planeEventText = 
                "A " + shotDownPlane.getDisplayName() +
                ",  serial number " + tankSerialNumber + 
                " has been withdrawn from service on " + prettyDate + ".\n";                

        return planeEventText;
    }

    public int getPlaneStatus()
    {
        return planeStatus;
    }

    public int getPlaneSerialNumber()
    {
        return tankSerialNumber;
    }
    
    public int getCrewMemberSerialNumber()
    {
        return crewMemberSerialNumber;
    }

    public String getCompanyName()
    {
        return companyName;
    }
}
