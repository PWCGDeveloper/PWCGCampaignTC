package pwcg.campaign;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;

public class CampaignHumanCrewMemberHandler
{
    private Campaign campaign;
    
    public CampaignHumanCrewMemberHandler(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public int addNewCrewMember(String humanCrewMemberName, String humanCrewMemberRank, int crewMemberToReplaceSerialNumber, int companyId) throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
        CompanyPersonnel companyCompanyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(companyId);

        CrewMember newPlayer = addHumanCrewMember(humanCrewMemberName, humanCrewMemberRank, company, companyCompanyPersonnel);
        removeAiCrewMember(crewMemberToReplaceSerialNumber, companyCompanyPersonnel);
        
        return newPlayer.getSerialNumber();
    }

    private CrewMember addHumanCrewMember(String humanCrewMemberName, String humanCrewMemberRank, Company company, CompanyPersonnel playerCompanyPersonnel) throws PWCGException
    {
        CampaignGeneratorModel generatorModel = new CampaignGeneratorModel();
        generatorModel.setPlayerRank(humanCrewMemberRank);
        generatorModel.setPlayerName(humanCrewMemberName);
        generatorModel.setService(company.determineServiceForCompany(campaign.getDate()));

        CrewMemberFactory companyMemberFactory = new CrewMemberFactory(campaign, company, playerCompanyPersonnel);
        CrewMember newPlayer = companyMemberFactory.createPlayer(generatorModel);
        playerCompanyPersonnel.addCrewMember(newPlayer);
        return newPlayer;
    }

    private void removeAiCrewMember(int crewMemberToReplaceSerialNumber, CompanyPersonnel playerCompanyPersonnel) throws PWCGException
    {
        CrewMember aiToRemove = playerCompanyPersonnel.getCrewMember(crewMemberToReplaceSerialNumber);
        if (aiToRemove != null)
        {
            playerCompanyPersonnel.removeCrewMember(aiToRemove);
        }
    }
}
