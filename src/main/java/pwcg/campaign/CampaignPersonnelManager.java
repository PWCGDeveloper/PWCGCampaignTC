package pwcg.campaign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.InitialReplacementStaffer;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.core.exception.PWCGException;

public class CampaignPersonnelManager 
{
	private Campaign campaign = null;
    private Map<Integer, CompanyPersonnel> companyPersonnelAllCompanys = new HashMap<>();
    private Map<Integer, PersonnelReplacementsService> personnelReplacementsServices = new HashMap<>();
	private CampaignAces campaignAces = new CampaignAces();

	public CampaignPersonnelManager(Campaign campaign) 
	{
		this.campaign = campaign;
	}

	public Map<Integer, CompanyPersonnel> getCampaignPersonnel()
	{
		return new HashMap<Integer, CompanyPersonnel>(companyPersonnelAllCompanys);
	}

    public CompanyPersonnel getCompanyPersonnel(Integer companyId)
    {
        return companyPersonnelAllCompanys.get(companyId);
    }

	public void addPersonnelForCompany(CompanyPersonnel campaignPersonnel)
	{
	    companyPersonnelAllCompanys.put(campaignPersonnel.getCompany().getCompanyId(), campaignPersonnel);
	}

	public CampaignAces getCampaignAces()
	{
		return campaignAces;
	}

	public void setCampaignAces(CampaignAces campaignAces) throws PWCGException
	{
	    for (TankAce campaignAce : campaignAces.getAllCampaignAces().values())
	    {
            campaignAce.mergeWithHistorical(campaign);
	    }
	    
		this.campaignAces = campaignAces;
	}

    public List<CompanyPersonnel> getAllCompanyPersonnel()
    {
        return new ArrayList<CompanyPersonnel>(companyPersonnelAllCompanys.values());
    }    

    public Map<Integer, CrewMember> getAllCampaignMembers() throws PWCGException
    {
        Map<Integer, CrewMember> allCrewMembers = getAllNonAceCampaignMembers();
        allCrewMembers.putAll(campaignAces.getAllCampaignAces());
        return allCrewMembers;
    }

    public Map<Integer, CrewMember> getActiveCampaignMembers() throws PWCGException
    {
        Map<Integer, CrewMember> allCrewMembers = getAllActiveNonAceCampaignMembers();
        allCrewMembers.putAll(campaignAces.getActiveCampaignAces());
        return allCrewMembers;
    }

    public CrewMembers getAllActivePlayers() throws PWCGException
    {
        return getPlayersForStatus(CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
    }

    public CrewMembers getAllPlayers() throws PWCGException
    {
        CrewMembers allPlayers =  new CrewMembers();
        for (CompanyPersonnel companyPersonnel : campaign.getPersonnelManager().getAllCompanyPersonnel())
        {
            CrewMembers playersInCompany = companyPersonnel.getPlayers();
            allPlayers.addCrewMembers(playersInCompany);
        }
        return allPlayers;
    }

    public CrewMembers getPlayersInMission() throws PWCGException
    {
        return getPlayersForStatus(CrewMemberStatus.STATUS_ACTIVE);
    }

    public CrewMembers getDeadPlayers() throws PWCGException
    {
        return getPlayersForStatus(CrewMemberStatus.STATUS_KIA);
    }    

    public CrewMembers getRetiredPlayers() throws PWCGException
    {
        return getPlayersForStatus(CrewMemberStatus.STATUS_RETIRED);
    }    

    private CrewMembers getPlayersForStatus(int status) throws PWCGException
    {
    	CrewMembers allPlayers =  new CrewMembers();
        for (CompanyPersonnel companyPersonnel : campaign.getPersonnelManager().getAllCompanyPersonnel())
        {
        	CrewMembers playersInCompany = companyPersonnel.getPlayersByStatus(status);
        	allPlayers.addCrewMembers(playersInCompany);
        }
        return allPlayers;
    }
    
    public boolean companyHasActivePlayers(int companyId) throws PWCGException
    {
        CrewMembers allActivePlayers = getPlayersForStatus(CrewMemberStatus.STATUS_ACTIVE);
        for (CrewMember player : allActivePlayers.getCrewMemberList())
        {
            if (player.getCompanyId() == companyId)
            {
                return true;
            }
        }
        return false;
    }    

    public Map<Integer, CrewMember> getAllActiveNonAceCampaignMembers() throws PWCGException
    {
        Map<Integer, CrewMember> allNonAceCampaignMembers =  new HashMap<>();
        for (CompanyPersonnel companyPersonnel : campaign.getPersonnelManager().getAllCompanyPersonnel())
        {
            allNonAceCampaignMembers.putAll(companyPersonnel.getActiveCrewMembers().getCrewMemberCollection());
            allNonAceCampaignMembers.putAll(companyPersonnel.getPlayersByStatus(CrewMemberStatus.STATUS_ACTIVE).getCrewMemberCollection());
        }
        return allNonAceCampaignMembers;
    }    

    public Map<Integer, CrewMember> getAllNonAceCampaignMembers() throws PWCGException
    {
        Map<Integer, CrewMember> allNonAceCampaignMembers =  new HashMap<>();
        for (CompanyPersonnel companyPersonnel : campaign.getPersonnelManager().getAllCompanyPersonnel())
        {
            allNonAceCampaignMembers.putAll(companyPersonnel.getCrewMembers().getCrewMemberCollection());
        }
        return allNonAceCampaignMembers;
    }    

    public CrewMember getCampaignAce(Integer serialNumber) throws PWCGException
    {
        CrewMember crewMember = campaignAces.retrieveAceBySerialNumber(serialNumber);
        return crewMember;
    }    

    public CrewMember getAnyCampaignMember(Integer serialNumber) throws PWCGException
    {
        CrewMember crewMember = campaignAces.retrieveAceBySerialNumber(serialNumber);
        if (crewMember != null)
        {
            return crewMember;
        }        

        for (CompanyPersonnel companyPersonnel : companyPersonnelAllCompanys.values())
        {
            crewMember = companyPersonnel.getCrewMember(serialNumber);
            if (crewMember != null)
            {
                return crewMember;
            }        
        }

        for (PersonnelReplacementsService personnelReplacements : personnelReplacementsServices.values())
        {
            crewMember =  personnelReplacements.getReplacement(serialNumber);
            if (crewMember != null)
            {
                return crewMember;
            }
        }
        
        throw new PWCGException ("Unable to locate company member for serial number " + serialNumber);
    }

    public List<PersonnelReplacementsService> getAllPersonnelReplacementsServices()
    {
        return new ArrayList<PersonnelReplacementsService>(personnelReplacementsServices.values());
    }
    
    public boolean hasPersonnelReplacements (int serviceId)
    {
        return personnelReplacementsServices.containsKey(serviceId);
    }
    
    public PersonnelReplacementsService getPersonnelReplacementsService(Integer serviceId) throws PWCGException
    {
        if (!personnelReplacementsServices.containsKey(serviceId))
        {
            ArmedService armedService = ArmedServiceFactory.createServiceManager().getArmedServiceById(serviceId);
            createPersonnelReplacements(armedService);
        }
        return personnelReplacementsServices.get(serviceId);
    }

    public void addPersonnelReplacementsService(Integer serviceId, PersonnelReplacementsService personnelReplacementsService)
    {
        this.personnelReplacementsServices.put(serviceId, personnelReplacementsService);
    }

    public List<PersonnelReplacementsService> getAllPersonnelReplacements()
    {
        return new ArrayList<PersonnelReplacementsService>(personnelReplacementsServices.values());
    }

    public int getReplacementCount() throws PWCGException
    {
    	int replacementCount = 0;
    	for (PersonnelReplacementsService replacementService : personnelReplacementsServices.values())
    	{
    		replacementCount += replacementService.getReplacements().getActiveCount(campaign.getDate());
    	}
    	
        return replacementCount;
    }

    public void createPersonnelReplacements(ArmedService armedService) throws PWCGException
    {
        InitialReplacementStaffer initialReplacementStaffer = new InitialReplacementStaffer(campaign, armedService);
        CrewMembers crewMembers = initialReplacementStaffer.staffReplacementsForService();
        
        PersonnelReplacementsService replacementsForService = new PersonnelReplacementsService();
        replacementsForService.setReplacements(crewMembers);
        replacementsForService.setServiceId(armedService.getServiceId());
        replacementsForService.setDailyReplacementRate(armedService.getDailyPersonnelReplacementRate(campaign.getDate()));
        replacementsForService.setLastReplacementDate(campaign.getDate());
        campaign.getPersonnelManager().addPersonnelReplacementsService(armedService.getServiceId(), replacementsForService);
    }
}
