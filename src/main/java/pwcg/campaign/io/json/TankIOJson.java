package pwcg.campaign.io.json;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class TankIOJson 
{
	public static Map<String, TankTypeInformation> readJson() throws PWCGException
	{
	    Map<String, TankTypeInformation> planeMap = new TreeMap<>();

		List<File> jsonFiles = FileUtils.getFilesWithFilter(PWCGContext.getInstance().getDirectoryManager().getPwcgTankInfoDir(), ".json");

		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<TankTypeInformation> jsoReader = new JsonObjectReader<>(TankTypeInformation.class);
			TankTypeInformation planeType = jsoReader.readJsonFile(PWCGContext.getInstance().getDirectoryManager().getPwcgTankInfoDir(), jsonFile.getName());
			planeMap.put(planeType.getType(), planeType);
		}
		
		return planeMap;
	}
}
