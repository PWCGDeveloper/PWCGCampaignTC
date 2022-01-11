package pwcg.campaign.io.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class CompanyIOJson 
{

    public static void writeJson(Company company) throws PWCGException
    {
        PwcgJsonWriter<Company> jsonWriter = new PwcgJsonWriter<>();
        String companyDir = PWCGContext.getInstance().getDirectoryManager().getPwcgCompanyDir();
        jsonWriter.writeAsJson(company, companyDir, company.getFileName());
    }

	public static List<Company> readJson() throws PWCGException
	{
	    List<Company> companys = new ArrayList<>();
	    
		List<File> jsonFiles = FileUtils.getFilesWithFilter(PWCGContext.getInstance().getDirectoryManager().getPwcgCompanyDir(), ".json");

		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<Company> jsonReader = new JsonObjectReader<>(Company.class);
			Company company = jsonReader.readJsonFile(PWCGContext.getInstance().getDirectoryManager().getPwcgCompanyDir(), jsonFile.getName()); 
			company.setFileName(jsonFile.getName());			
			companys.add(company);
		}

		return companys;
	}
}
