package pwcg.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.coop.CoopUserManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;

public class CrewMemberReplacer  implements ICrewMemberReplacer
{
    protected Campaign campaign;
    
    public CrewMemberReplacer(Campaign campaign)
    {
        this.campaign = campaign;
    }
	
    public CrewMember createPersona(String playerCrewMemberName, String rank, String companyName, String coopUser) throws PWCGUserException, Exception
    {        
        Company newPlayerCompany = getNewPlayerCompany(companyName);
    	CompanyPersonnel newPlayerCompanyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(newPlayerCompany.getCompanyId());

        CrewMember newCompanyMewmber = addnewCrewMemberToCampaign(playerCrewMemberName, rank, newPlayerCompany, newPlayerCompanyPersonnel);        
        removeAiCrewMember(rank, newPlayerCompany, newPlayerCompanyPersonnel);
        
        if (campaign.isCoop())
        {
            CoopUserManager.getIntance().createCoopPersona(campaign.getName(), newCompanyMewmber, coopUser);
        }
        
        return newCompanyMewmber;
    }

	private Company getNewPlayerCompany(String companyName) throws PWCGException 
	{
		CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        Company newPlayerCompany = companyManager.getCompanyByName(companyName, campaign.getDate());
		return newPlayerCompany;
	}

	private CrewMember addnewCrewMemberToCampaign(String playerCrewMemberName, String rank, Company newPlayerCompany,
			CompanyPersonnel newPlayerCompanyPersonnel) throws PWCGException 
	{
		CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setPlayerRank(rank);
        generatorModel.setPlayerName(playerCrewMemberName);
        generatorModel.setService(newPlayerCompany.determineServiceForCompany(campaign.getDate()));
        generatorModel.setCampaignName(campaign.getCampaignData().getName());
        generatorModel.setCampaignDate(campaign.getDate());
        generatorModel.setCompanyName(newPlayerCompany.determineDisplayName(campaign.getDate()));
        generatorModel.validateCampaignInputs();
        
        CrewMemberFactory companyMemberFactory = new CrewMemberFactory(campaign, newPlayerCompany, newPlayerCompanyPersonnel);
        CrewMember newPlayer = companyMemberFactory.createPlayer(generatorModel);

        newPlayerCompanyPersonnel.addCrewMember(newPlayer);
		return newPlayer;
	}

	private void removeAiCrewMember(String rank, Company newPlayerCompany, CompanyPersonnel newPlayerCompanyPersonnel) throws PWCGException 
	{
		AiCrewMemberRemovalChooser crewMemberRemovalChooser = new AiCrewMemberRemovalChooser(campaign);
        CrewMember companyMemberToReplace = crewMemberRemovalChooser.findAiCrewMemberToRemove(rank, newPlayerCompany.getCompanyId());
        newPlayerCompanyPersonnel.removeCrewMember(companyMemberToReplace);
	}
}
