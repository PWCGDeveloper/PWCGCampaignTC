package pwcg.aar.campaign.update;

import pwcg.aar.data.AARContext;
import pwcg.aar.outofmission.phase2.resupply.TransferRecord;
import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;

public class CampaignPersonnelUpdater 
{
	private Campaign campaign;
    private AARContext aarContext;

	public CampaignPersonnelUpdater (Campaign campaign, AARContext aarContext) 
	{
        this.campaign = campaign;
        this.aarContext = aarContext;
	}
	
    public void personnelUpdates() throws PWCGException 
    {
        personnelAceRemovals();
        personnelPilotLosses();        
        personnelAceAdditions();
    }


    private void personnelAceRemovals()
    {
        acesKilled();
        acesTransferredOut();
    }

    private void acesTransferredOut()
	{
	}

	private void acesKilled()
    {
        for (Integer serialNumber : aarContext.getCampaignUpdateData().getPersonnelLosses().getAcesKilled().keySet())
        {
            setAceKilledInCampaign(serialNumber);
        }
    }

    private void setAceKilledInCampaign(Integer serialNumber)
    {
        Ace ace = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(serialNumber);
        ace.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
    }


    private void personnelPilotLosses() throws PWCGException 
    {        
        squadronMembersKilled();
        squadronMembersCaptured();
        squadronMembersMaimed();
        squadronMembersTransfers();
    }

    private void squadronMembersKilled()
    {
        for (SquadronMember pilot : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelKilled().values())
        {
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        }
    }

    private void squadronMembersCaptured()
    {
        for (SquadronMember pilot : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelCaptured().values())
        {
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        }
    }

    private void squadronMembersMaimed()
    {
        for (SquadronMember pilot : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelMaimed().values())
        {
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), pilot.getRecoveryDate());
        }
    }

    private void squadronMembersTransfers() throws PWCGException
    {
        for (TransferRecord transferRecord : aarContext.getCampaignUpdateData().getResupplyData().getSquadronTransferData().getSquadronMembersTransferred())
        {
            SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(transferRecord.getTransferTo());
            transferRecord.getSquadronMember().setSquadronId(transferRecord.getTransferTo());
            squadronPersonnel.addSquadronMember(transferRecord.getSquadronMember());
        }
    }

    private void personnelAceAdditions() throws PWCGException
    {
        for (TransferRecord transferRecord : aarContext.getCampaignUpdateData().getResupplyData().getAcesTransferred().getSquadronMembersTransferred())
        {
            transferRecord.getSquadronMember().setSquadronId(transferRecord.getTransferTo());
        }
    }
 }
