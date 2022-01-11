package pwcg.aar.campaign.update;

import java.util.Date;

import pwcg.aar.campaigndate.WoundRecovery;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;

public class PersonnelUpdater 
{
	private Campaign campaign;
    private CampaignUpdateData campaignUpdateData;

	public PersonnelUpdater (Campaign campaign, CampaignUpdateData campaignUpdateData) 
	{
        this.campaign = campaign;
        this.campaignUpdateData = campaignUpdateData;
	}
	
    public void personnelUpdates() throws PWCGException 
    {
        personnelAceRemovals();
        personnelCrewMemberLosses();        
        personnelAceAdditions();
        personnelHealWoundedCrewMembers();
    }

    private void personnelAceRemovals() throws PWCGException
    {
        acesKilled();
        acesTransferredOut();
    }

    private void acesTransferredOut()
	{
	}

	private void acesKilled() throws PWCGException
    {
        for (Integer serialNumber : campaignUpdateData.getPersonnelLosses().getAcesKilled(campaign).keySet())
        {
            setAceKilledInCampaign(serialNumber);
        }
    }

    private void setAceKilledInCampaign(Integer serialNumber) throws PWCGException
    {
        CrewMember ace = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
        ace.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
    }

    private void personnelCrewMemberLosses() throws PWCGException 
    {        
        crewMembersKilled();
        crewMembersCaptured();
        crewMembersMaimed();
        crewMembersWounded();
        crewMembersTransferredHome();
        crewMembersTransfers();
    }

    private void crewMembersKilled()
    {
        for (CrewMember crewMember : campaignUpdateData.getPersonnelLosses().getPersonnelKilled().values())
        {
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        }
    }

    private void crewMembersCaptured()
    {
        for (CrewMember crewMember : campaignUpdateData.getPersonnelLosses().getPersonnelCaptured().values())
        {
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        }
    }

    private void crewMembersWounded() throws PWCGException
    {
        for (CrewMember crewMember : campaignUpdateData.getPersonnelLosses().getPersonnelWounded().values())
        {
            Date woundrecoveryDate = determinePlayerWoundedTime(CrewMemberStatus.STATUS_WOUNDED);
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_WOUNDED, campaign.getDate(), woundrecoveryDate);
        }
    }

    private void crewMembersMaimed() throws PWCGException
    {
        for (CrewMember crewMember : campaignUpdateData.getPersonnelLosses().getPersonnelMaimed().values())
        {
            Date woundrecoveryDate = determinePlayerWoundedTime(CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), woundrecoveryDate);
        }
    }

    private void crewMembersTransferredHome() throws PWCGException
    {
        for (CrewMember crewMember : campaignUpdateData.getPersonnelLosses().getPersonnelTransferredHome().values())
        {
            crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_TRANSFERRED, campaign.getDate(), null);
        }
    }

    private void crewMembersTransfers() throws PWCGException
    {
        for (TransferRecord transferRecord : campaignUpdateData.getResupplyData().getCompanyTransferData().getCrewMembersTransferred())
        {
            addCrewMemberToCompany(transferRecord);
            removeFromReplacementPool(transferRecord);
        }
    }

    private void addCrewMemberToCompany(TransferRecord transferRecord) throws PWCGException
    {
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(transferRecord.getTransferTo());
        transferRecord.getCrewMember().setCompanyId(transferRecord.getTransferTo());
        companyPersonnel.addCrewMember(transferRecord.getCrewMember());
    }

    private void removeFromReplacementPool(TransferRecord transferRecord) throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(transferRecord.getTransferTo());
        ArmedService service = company.determineServiceForCompany(campaign.getDate());
        PersonnelReplacementsService replacementService = campaign.getPersonnelManager().getPersonnelReplacementsService(service.getServiceId());
        replacementService.transferFromReservesToActive(transferRecord.getCrewMember().getSerialNumber());
    }

    private void personnelAceAdditions() throws PWCGException
    {
        for (TransferRecord transferRecord : campaignUpdateData.getResupplyData().getAcesTransferred().getCrewMembersTransferred())
        {
            transferRecord.getCrewMember().setCompanyId(transferRecord.getTransferTo());
        }
    }
    
    private void personnelHealWoundedCrewMembers() throws PWCGException
    {
        CampaignWoundUpdater woundUpdater = new CampaignWoundUpdater(campaign);
        woundUpdater.healWoundedCrewMembers(campaignUpdateData.getNewDate());
    }
    
    private Date determinePlayerWoundedTime(int crewMemberStatus) throws PWCGException
    {
        WoundRecovery woundTimeCalculator = new WoundRecovery(campaign);
        Date woundedDate = woundTimeCalculator.calcDateOfRecovery(crewMemberStatus);
        return woundedDate;
    }
 }
