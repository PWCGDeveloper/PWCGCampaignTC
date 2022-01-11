package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class CampaignPersonnelIOJson 
{
    public static  void writeJson(Campaign campaign) throws PWCGException
    {
        makePersonnelDir(campaign);
        writeCompanys(campaign);
        writeReplacements(campaign);
    }
    
    private static void makePersonnelDir(Campaign campaign)
    {
        String campaignPersonnelDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\";
        FileUtils.createDirIfNeeded(campaignPersonnelDir);
        
        String campaignReplacementsDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\Replacements\\";
        FileUtils.createDirIfNeeded(campaignReplacementsDir);
    }

    private static void writeCompanys(Campaign campaign) throws PWCGException
    {
        for (CompanyPersonnel companyPersonnel : campaign.getPersonnelManager().getAllCompanyPersonnel())
        {
            writeCompany(campaign, companyPersonnel.getCompany().getCompanyId());
        }
    }

    public static void writeCompany(Campaign campaign, int companyId) throws PWCGException
    {
        CompanyPersonnel companyPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(companyId);
        CrewMembers crewMembersToWrite = companyPersonnel.getCrewMembers();

        String campaignPersonnelDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\";
        PwcgJsonWriter<CrewMembers> jsonWriterCompanys = new PwcgJsonWriter<>();
        jsonWriterCompanys.writeAsJson(crewMembersToWrite, campaignPersonnelDir, companyPersonnel.getCompany().getCompanyId() + ".json");
    }

    private static void writeReplacements(Campaign campaign) throws PWCGException
    {
        String campaignReplacementDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\Replacements\\";
        PwcgJsonWriter<PersonnelReplacementsService> jsonWriterReplacements = new PwcgJsonWriter<>();
        for (PersonnelReplacementsService replacements : campaign.getPersonnelManager().getAllPersonnelReplacements())
        {
            jsonWriterReplacements.writeAsJson(replacements, campaignReplacementDir, replacements.getServiceId() + ".json");
        }
    }

    public static void readJson(Campaign campaign) throws PWCGException
    {
        readCompanys(campaign);
        readReplacements(campaign);
    }

    private static void readCompanys(Campaign campaign) throws PWCGException
    {
        String campaignPersonnelDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\";
        List<File> jsonFiles = FileUtils.getFilesWithFilter(campaignPersonnelDir, ".json");
        for (File jsonFile : jsonFiles)
        {
            readCompany(campaign, campaignPersonnelDir, jsonFile);
        }
    }

    private static void readCompany(Campaign campaign, String campaignPersonnelDir, File jsonFile) throws PWCGException
    {
        JsonObjectReader<CrewMembers> jsoReader = new JsonObjectReader<>(CrewMembers.class);
        CrewMembers crewMembers = jsoReader.readJsonFile(campaignPersonnelDir, jsonFile.getName());
        
        int companyId = Integer.valueOf(FileUtils.stripFileExtension(jsonFile.getName()));
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(companyId);
        if (company != null)
        {
            CompanyPersonnel companyPersonnel = new CompanyPersonnel(campaign, company);
            companyPersonnel.setCrewMembers(crewMembers);
            campaign.getPersonnelManager().addPersonnelForCompany(companyPersonnel);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "No company found for company id " + companyId + " in campaign " + campaign.getName());
        }
    }

    private static void readReplacements(Campaign campaign) throws PWCGException
    {
        String campaignReplacementDir = PWCGDirectoryUserManager.getInstance().getPwcgCampaignsDir() + campaign.getCampaignData().getName() + "\\Personnel\\Replacements\\";
        List<File> jsonFiles = FileUtils.getFilesWithFilter(campaignReplacementDir, ".json");
        for (File jsonFile : jsonFiles)
        {
            JsonObjectReader<PersonnelReplacementsService> jsoReader = new JsonObjectReader<>(PersonnelReplacementsService.class);
            PersonnelReplacementsService replacements = jsoReader.readJsonFile(campaignReplacementDir, jsonFile.getName());
            
            int serviceId = Integer.valueOf(FileUtils.stripFileExtension(jsonFile.getName()));
            campaign.getPersonnelManager().addPersonnelReplacementsService(serviceId, replacements);
        }
    }
}
