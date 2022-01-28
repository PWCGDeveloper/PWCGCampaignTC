package pwcg.campaign.io.json;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skin.SkinSet;
import pwcg.campaign.skin.SkinsForTank;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.FileUtils;

public class SkinIOJson 
{	
    public static void writeJson(Map<String, SkinsForTank> skinsForTanks) throws PWCGException
	{
		for (String planeType : skinsForTanks.keySet())
		{
			SkinsForTank skinsForPlane = skinsForTanks.get(planeType);
			writeJson(planeType, skinsForPlane.getConfiguredSkins());
			writeJson(planeType, skinsForPlane.getDoNotUse());
		}
	}

    public static void writeJson(String planeType, SkinSet skinSet) throws PWCGException
	{
		PwcgJsonWriter<SkinSet> jsonWriter = new PwcgJsonWriter<>();
		String skinDir = PWCGContext.getInstance().getDirectoryManager().getPwcgSkinsDir() + skinSet.getSkinSetType().getSkinSetName() + "\\";
		jsonWriter.writeAsJson(skinSet, skinDir, planeType + ".json");
	}

	public static Map<String, SkinSet> readSkinSet(String skinSetName) throws PWCGException
	{
		Map<String, SkinSet>  skinSets = new HashMap<>();
		
		String skinDir = PWCGContext.getInstance().getDirectoryManager().getPwcgSkinsDir() + skinSetName + "\\";
		List<File> jsonFiles = FileUtils.getFilesWithFilter(skinDir, ".json");
		for (File jsonFile : jsonFiles)
		{
			JsonObjectReader<SkinSet> jsonReader = new JsonObjectReader<>(SkinSet.class);
			SkinSet skinSet = jsonReader.readJsonFile(skinDir, jsonFile.getName()); 
			
			String planeType = FileUtils.stripFileExtension(jsonFile.getName());
			skinSets.put(planeType, skinSet);
		}
		
		return skinSets;
	}
}
