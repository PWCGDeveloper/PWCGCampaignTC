package pwcg.campaign;

import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.MapForBaseFinder;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.PWCGLocation;

public class CampaignGenerator 
{
    private CampaignGeneratorModel generatorModel;
    private Campaign campaign;
    
    public CampaignGenerator(CampaignGeneratorModel generatorModel)
    {
        this.generatorModel = generatorModel;
    }

    public Campaign generate() throws PWCGException
    {
        generatorModel.validateCampaignInputs();
        createCampaignBasis();
        staffCompanies();
        createPersonnelReplacements();
        createEquipmentReplacements();
        return campaign;
    }

    private void createCampaignBasis() throws PWCGException
    {
        setMapForNewCampaign();
        createCampaign();
        setCampaignAces();
    }

    private void createCampaign() throws PWCGException
	{
		campaign = new Campaign();
        campaign.initializeCampaignConfigs();
        campaign.setDate(generatorModel.getCampaignDate());
        campaign.getCampaignData().setName(generatorModel.getCampaignName());
        campaign.getCampaignData().setCampaignMode(generatorModel.getCampaignMode());
	}

    private void staffCompanies() throws PWCGException
    {
        List<Company> activeCompanysOnCampaignStartDate = PWCGContext.getInstance().getCompanyManager().getActiveCompanies(generatorModel.getCampaignDate());
        for (Company company : activeCompanysOnCampaignStartDate)
        {
            CampaignCompanyGenerator companyGenerator = new CampaignCompanyGenerator(campaign, company);
            companyGenerator.createCompany(generatorModel);
        }
        
        useCampaignPlayerToSetReferencePlayer();
    }

    private void useCampaignPlayerToSetReferencePlayer() throws PWCGException
    {
        CrewMember referencePlayer = campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList().get(0);
        campaign.getCampaignData().setReferencePlayerSerialNumber(referencePlayer.getSerialNumber());
    }

    private void createPersonnelReplacements() throws PWCGException
    {
        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllActiveArmedServices(campaign.getDate());
        for (ArmedService armedService : armedServices)
        {
            CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
            personnelManager.createPersonnelReplacements(armedService);
        }
    }

    private void createEquipmentReplacements() throws PWCGException
    {
        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllActiveArmedServices(campaign.getDate());
        for (ArmedService armedService : armedServices)
        {
            CampaignEquipmentManager equipmentGenerator = campaign.getEquipmentManager();
            equipmentGenerator.createEquipmentDepot(armedService);
        }
    }

    private void setMapForNewCampaign() throws PWCGException
    {
        Company company = PWCGContext.getInstance().getCompanyManager().getCompanyByName(generatorModel.getCompanyName(), generatorModel.getCampaignDate());
        PWCGLocation base = company.determineCurrentBaseAnyMap(generatorModel.getCampaignDate());
        List<FrontMapIdentifier> airfieldMaps = MapForBaseFinder.getMapForBase(base.getName());
        FrontMapIdentifier initialAirfieldMap = airfieldMaps.get(0);

        PWCGContext.getInstance().changeContext(initialAirfieldMap);
    }

	private void setCampaignAces() throws PWCGException
	{
		CampaignAces aces =  PWCGContext.getInstance().getAceManager().loadFromHistoricalAces(generatorModel.getCampaignDate());
        campaign.getPersonnelManager().setCampaignAces(aces);
	}
}
