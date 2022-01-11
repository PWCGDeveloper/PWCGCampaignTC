package pwcg.campaign;

import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyTransferFinder;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;


public class TransferHandler 
{
    private Campaign campaign = null;
    private CrewMember player = null;
    
    public TransferHandler (Campaign campaign, CrewMember player)
    {
        this.campaign = campaign;
        this.player = player;
    }

	public TransferEvent transferPlayer(Company oldCompany, Company newCompany) throws PWCGException 
	{
	    int leaveTimeForTransfer = transferleaveTime();	
        changeInRankForServiceTransfer(oldCompany.determineServiceForCompany(campaign.getDate()), newCompany.determineServiceForCompany(campaign.getDate()));
        movePlayerToNewCompany(newCompany);
        TransferEvent transferEvent = createTransferEvent(leaveTimeForTransfer, oldCompany, newCompany);
		
        return transferEvent;
	}

	public void transferAI(CrewMember crewMember) throws PWCGException 
    {
        CompanyTransferFinder companyTransferFinder = new CompanyTransferFinder(campaign, crewMember);
        int newCompanyId = companyTransferFinder.chooseCompanyForTransfer();
		CompanyPersonnel oldCompanyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(crewMember.getCompanyId());
		CompanyPersonnel newCompanyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(newCompanyId);

		oldCompanyPersonnel.removeCrewMember(crewMember);
        crewMember.setCompanyId(newCompanyId);
		newCompanyPersonnel.addCrewMember(crewMember);
    }

	private TransferEvent createTransferEvent(int leaveTimeForTransfer, Company oldSquad, Company newSquad) throws PWCGException
	{
        boolean isNewsWorthy = true;
		TransferEvent transferEvent = new TransferEvent(campaign, oldSquad.getCompanyId(), newSquad.getCompanyId(), leaveTimeForTransfer, player.getSerialNumber(), campaign.getDate(), isNewsWorthy);
		return transferEvent;
	}

	private void movePlayerToNewCompany(Company newCompany) throws PWCGException
	{        
		CompanyPersonnel oldCompanyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(player.getCompanyId());
		CompanyPersonnel newCompanyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(newCompany.getCompanyId());

		oldCompanyPersonnel.removeCrewMember(player);
		player.setCompanyId(newCompany.getCompanyId());
		newCompanyPersonnel.addCrewMember(player);
	}

	private int transferleaveTime() throws PWCGException 
	{
        int leaveTimeForTransfer = 5 + RandomNumberGenerator.getRandom(10);
        return leaveTimeForTransfer;
	}

    private void changeInRankForServiceTransfer(ArmedService oldService, ArmedService newService) throws PWCGException 
    {
        IRankHelper iRank = RankFactory.createRankHelper();        
        int rankPos = iRank.getRankPosByService(player.getRank(), oldService);
        
        int lowestRankPos = iRank.getLowestRankPosForService(newService);
        if (rankPos > lowestRankPos)
        {
            rankPos = lowestRankPos;
        }
        
        String newRank = iRank.getRankByService(rankPos, newService);
        player.setRank(newRank);
    }
}
