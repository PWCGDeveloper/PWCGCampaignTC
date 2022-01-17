package integration.campaign.io.json;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.CompanyHistory;
import pwcg.campaign.CompanyHistoryEntry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CompanyIOJson;
import pwcg.campaign.io.json.PwcgJsonWriter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class CompanyIOJsonTest
{
    @Test
    public void readJsonRoFCompanysTest() throws PWCGException
    {
        
        List<Company> companys = CompanyIOJson.readJson();
        Assertions.assertTrue (companys.size() > 0);

        for (Company company : companys)
        {
            Assertions.assertTrue (company.getCompanyRoles().getCompanyRolePeriods().size() > 0);
            Assertions.assertTrue (company.getService() > 0);
            
            verifyLafayetteEsc(company);
            verifyRFCToRAF(company);
            verifyRNASToRAF(company);
        }
    }

    private void verifyLafayetteEsc(Company company) throws PWCGException
    {
        if (company.getCompanyId() == 101124)
        {
            CompanyHistory companyHistory = company.getSquadHistory();
            Assertions.assertTrue (companyHistory != null);
            
            CompanyHistoryEntry  squadHistoryEntry = companyHistory.getSquadHistoryEntry(DateUtils.getDateYYYYMMDD("19180219"));
            Assertions.assertTrue (squadHistoryEntry != null);
            Assertions.assertTrue (squadHistoryEntry.getArmedServiceName().equals("United States Air Service"));
            Assertions.assertTrue (squadHistoryEntry.getSkill() == CompanyHistoryEntry.NO_Company_SKILL_CHANGE);
        }
    }

    private void verifyRFCToRAF(Company company) throws PWCGException
    {
        if (company.getCompanyId() == 102020)
        {
            CompanyHistory companyHistory = company.getSquadHistory();
            Assertions.assertTrue (companyHistory != null);
            
            CompanyHistoryEntry  squadHistoryEntry = companyHistory.getSquadHistoryEntry(DateUtils.getDateYYYYMMDD("19180401"));
            Assertions.assertTrue (squadHistoryEntry != null);
            Assertions.assertTrue (squadHistoryEntry.getArmedServiceName().equals("Royal Air Force"));
            Assertions.assertTrue (squadHistoryEntry.getSquadName().equals("No 20 Company RAF"));
            Assertions.assertTrue (squadHistoryEntry.getSkill() == CompanyHistoryEntry.NO_Company_SKILL_CHANGE);
        }
    }

    private void verifyRNASToRAF(Company company) throws PWCGException
    {
        if (company.getCompanyId() == 102209)
        {
            CompanyHistory companyHistory = company.getSquadHistory();
            Assertions.assertTrue (companyHistory != null);
            
            CompanyHistoryEntry  squadHistoryEntry = companyHistory.getSquadHistoryEntry(DateUtils.getDateYYYYMMDD("19180401"));
            Assertions.assertTrue (squadHistoryEntry != null);
            Assertions.assertTrue (squadHistoryEntry.getArmedServiceName().equals("Royal Air Force"));
            Assertions.assertTrue (squadHistoryEntry.getSquadName().equals("No 209 Company RAF"));
            Assertions.assertTrue (squadHistoryEntry.getSkill() == CompanyHistoryEntry.NO_Company_SKILL_CHANGE);
        }
    }

    @Test
    public void readJsonBoSCompanysTest() throws PWCGException
    {
        
        List<Company> companys = CompanyIOJson.readJson();
        Assertions.assertTrue (companys.size() > 0);
        
        boolean success = true;
        for (Company company : companys)
        {
            Assertions.assertTrue (company.getCompanyRoles().getCompanyRolePeriods().size() > 0);
            Assertions.assertTrue (company.getService() > 0);
        }
        
        assert(success);
    }

    @Test
    public void writeJsonBoSCompanysTest() throws PWCGException
    {
        
        List<Company> companys = CompanyIOJson.readJson();
        
        PwcgJsonWriter<Company> jsonWriter = new PwcgJsonWriter<>();
        String companyDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCompanyDir();
        jsonWriter.writeAsJson(companys.get(0), companyDir, "TestCompany");
    }
}
