package pwcg.campaign;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.company.CompanyViability;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberNames;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.medals.MedalManager;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class CampaignCleaner
{
    private Campaign campaign;
    
    public CampaignCleaner(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public void cleanDataFiles() throws PWCGException
    {
        generateMissingDepos();
        removeUnwantedCompanyFiles();
        removeDuplicateCrewMembers();
        checkDuplicateCrewMembers();
        checkCrewMemberKeys();
        cleanMedals();
    }

    private void removeUnwantedCompanyFiles() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        List<Company> companysToStaff = companyManager.getActiveCompanies(campaign.getDate());
        for (Company company : companysToStaff)
        {
            if (!CompanyViability.isCompanyActive(company, campaign.getDate()))
            {
                if (campaign.getPersonnelManager().getCompanyPersonnel(company.getCompanyId()) == null)
                {
                    String campaignPersonnelDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\";
                    File companyPersonnelFile = new File(campaignPersonnelDir + company.getCompanyId() + ".json");
                    if (companyPersonnelFile.exists())
                    {
                        companyPersonnelFile.delete();
                    }
                    
                    String campaignEquipmentDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Equipment\\";
                    File campaignEquipmentFile = new File(campaignEquipmentDir + company.getCompanyId() + ".json");
                    if (campaignEquipmentFile.exists())
                    {
                        campaignEquipmentFile.delete();
                    }
                }
            }
       }
    }

    private void generateMissingDepos() throws PWCGException
    {
        generatePersonnelDepos();
        generateEquipmentDepos();
    }

    private void generatePersonnelDepos() throws PWCGException
    {
        for (ArmedService armedService : ArmedServiceFinder.getArmedServicesForDate(campaign))
        {
            CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
            if (!personnelManager.hasPersonnelReplacements(armedService.getServiceId()))
            {
                personnelManager.createPersonnelReplacements(armedService);
            }
        }
    }
    
    private void generateEquipmentDepos() throws PWCGException
    {
        for (ArmedService armedService : ArmedServiceFinder.getArmedServicesForDate(campaign))
        {
            CampaignEquipmentManager equipmentManager = campaign.getEquipmentManager();
            if (!equipmentManager.hasEquipmentDepo(armedService.getServiceId()))
            {
                equipmentManager.createEquipmentDepot(armedService);
            }
        }
    }
    
    private void removeDuplicateCrewMembers() throws PWCGException
    {
        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, CompanyPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        Map<Integer, List<Integer>> crewMemberSerialNumbers = new HashMap<>();
        for (CompanyPersonnel personnel : allPersonnel.values())
        {
            for (CrewMember crewMember : personnel.getCrewMembers().getCrewMemberList())
            {
                if (!crewMember.isPlayer() && !(crewMember instanceof TankAce))
                {
                    if (!crewMemberSerialNumbers.containsKey(crewMember.getSerialNumber()))
                    {
                        crewMemberSerialNumbers.put(crewMember.getSerialNumber(), new ArrayList<Integer>());
                    }
                    List<Integer> companysForSerialNumber = crewMemberSerialNumbers.get(crewMember.getSerialNumber());
                    companysForSerialNumber.add(personnel.getCompany().getCompanyId());
                }
            }
        }
        
        boolean changesMade = false;
        for (Integer serialNumber : crewMemberSerialNumbers.keySet())
        {
            List<Integer> companysForSerialNumber = crewMemberSerialNumbers.get(serialNumber);
            if (companysForSerialNumber.size() > 1)
            {
                boolean firstTime = true;
                for (Integer companyId : companysForSerialNumber)
                {
                    CompanyPersonnel campaignPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(companyId);
                    CrewMember  crewMember = campaignPersonnel.getCrewMember(serialNumber);
                    PWCGLogger.log(LogLevel.DEBUG, "Replace " + crewMember.getName() + " company member : " + serialNumber + " flying for " + companyId);
                    
                    if (!firstTime)
                    {
                        crewMember.setSerialNumber(campaign.getCampaignData().getSerialNumber().getNextCrewMemberSerialNumber());
                        
                        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(crewMember.getCompanyId());
                        String squaddieName = CrewMemberNames.getInstance().getName(company.determineServiceForCompany(campaign.getDate()), new HashMap<>());
                        crewMember.setName(squaddieName);
                    }
                    
                    firstTime = false;
                    changesMade = true;
                }
            }
        }
        
        if (changesMade)
        {
            campaign.write();
        }
    }
    
    private void checkDuplicateCrewMembers() throws PWCGException
    {
        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, CompanyPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        Map<Integer, List<Integer>> crewMemberSerialNumbers = new HashMap<>();
        for (CompanyPersonnel personnel : allPersonnel.values())
        {
            for (CrewMember crewMember : personnel.getCrewMembers().getCrewMemberList())
            {
                if (!crewMember.isPlayer() && !(crewMember instanceof TankAce))
                {
                    if (!crewMemberSerialNumbers.containsKey(crewMember.getSerialNumber()))
                    {
                        crewMemberSerialNumbers.put(crewMember.getSerialNumber(), new ArrayList<Integer>());
                    }
                    List<Integer> companysForSerialNumber = crewMemberSerialNumbers.get(crewMember.getSerialNumber());
                    companysForSerialNumber.add(personnel.getCompany().getCompanyId());
                }
            }
        }
        
        for (Integer serialNumber : crewMemberSerialNumbers.keySet())
        {
            List<Integer> companysForSerialNumber = crewMemberSerialNumbers.get(serialNumber);
            if (companysForSerialNumber.size() > 1)
            {
                for (Integer companyId : companysForSerialNumber)
                {
                    PWCGLogger.log(LogLevel.DEBUG, "Duplicate company member : " + serialNumber + " flying for " + companyId);
                }
            }
        }
    }
    
    
    private void checkCrewMemberKeys() throws PWCGException
    {
        boolean changesMade = false;

        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, CompanyPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        for (CompanyPersonnel personnel : allPersonnel.values())
        {
            boolean fixesNeeded = true;
            while (fixesNeeded)
            {
                fixesNeeded = fixOneCrewMember(personnel);
                if (fixesNeeded)
                {
                    changesMade = true;
                }
            }
        }
        
        if (changesMade)
        {
            campaign.write();
        }
    }

    private boolean fixOneCrewMember(CompanyPersonnel personnel) throws PWCGException
    {
        boolean fixesNeeded = false;
        for (Integer key : personnel.getCrewMembers().getCrewMemberCollection().keySet())
        {
            CrewMember crewMember = personnel.getCrewMembers().getCrewMemberCollection().get(key);
            if (key != crewMember.getSerialNumber())
            {
                personnel.getCrewMembers().removeCrewMember(key);
                personnel.getCrewMembers().addToCrewMemberCollection(crewMember);
                fixesNeeded = true;
                break;
            }
        }
        return fixesNeeded;
    }

    
    private void cleanMedals() throws PWCGException
    {
        CampaignPersonnelManager personnelManager = campaign.getPersonnelManager();
        Map<Integer, CompanyPersonnel> allPersonnel = personnelManager.getCampaignPersonnel();
        
        for (CompanyPersonnel personnel : allPersonnel.values())
        {
            for (CrewMember crewMember : personnel.getCrewMembers().getCrewMemberList())
            {
                for (Medal medal : crewMember.getMedals())
                {
                    if (medal.getMedalName().contains("Wound Badge ("))
                    {
                        int afterIndex = medal.getMedalName().indexOf("(");
                        String medalName = "Wound Badge" + medal.getMedalName().substring(afterIndex);
                        medal.setMedalName(medalName);
                    }
                    convertMedal(crewMember, crewMember.determineCountry(medal.getMedalDate()), medal);
                }
            }
        }
        
        for (TankAce ace : campaign.getPersonnelManager().getCampaignAces().getAllCampaignAces().values())
        {
            for (Medal medal : ace.getMedals())
            {
                convertMedal(ace, CountryFactory.makeCountryByCountry(ace.getCountry()), medal);
            }
        }
    }

    private void convertMedal(CrewMember crewMember, ICountry country, Medal medal) throws PWCGException
    {
        Medal newMedal = MedalManager.getMedalFromAnyManager(country, campaign, medal.getMedalName());
        if (newMedal != null)
        {
            medal.setMedalImage(newMedal.getMedalImage());
        }
        else
        {
            PWCGLogger.log(LogLevel.DEBUG, "Could not find medal for " + medal.getMedalName());
        }
    }
}
