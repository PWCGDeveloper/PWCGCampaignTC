package integration.campaign.io.json;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CompanyIOJson;
import pwcg.campaign.io.json.PwcgJsonWriter;
import pwcg.core.exception.PWCGException;

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
