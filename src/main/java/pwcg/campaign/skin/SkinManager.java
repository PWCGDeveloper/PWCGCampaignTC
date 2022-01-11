package pwcg.campaign.skin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class SkinManager 
{
    private Map<String, SkinsForPlane> skinsForPlanes = new HashMap<>();
		
    public SkinManager ()
	{
	}

	public void initialize()
    {
        SkinLoader skinLoader = new SkinLoader();
        skinsForPlanes = skinLoader.loadPwcgSkins();
    }

    public List<Skin> getCompanySkinsByPlaneCompanyDate(String planeName, int companyId, Date date)
    {
    	List<Skin> skinsForCompany = new ArrayList<Skin>();

        if (skinsForPlanes.containsKey(planeName))
        {
        	SkinSet companySkinSet = skinsForPlanes.get(planeName).getCompanySkins();
        	skinsForCompany = new ArrayList<>(companySkinSet.getSkins().values());
            skinsForCompany = SkinFilter.skinFilterCompany(skinsForCompany, companyId);
            skinsForCompany = SkinFilter.skinFilterDate(skinsForCompany, date);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsByCompanyPlaneDateInUse: Invalid plane " + planeName);
        }
        
        return skinsForCompany;
    }

    public List<Skin> getSkinsByCompany(int companyId)
    {
        List<Skin> configuredSkinsForCompany = new ArrayList<Skin>();

        for (SkinsForPlane skinsForPlane : skinsForPlanes.values())
        {
            SkinSet configuredSkins = skinsForPlane.getConfiguredSkins();
        	configuredSkinsForCompany = new ArrayList<>(configuredSkins.getSkins().values());
            List<Skin> skinsForCompanyAndPlane = SkinFilter.skinFilterCompany(configuredSkinsForCompany, companyId);
            
            configuredSkinsForCompany.addAll(skinsForCompanyAndPlane);
        }

        
        return configuredSkinsForCompany;
    }

	public List<Skin> getSkinsByPlaneCompanyDateInUse(String planeName, int companyId, Date date)
	{
        List<Skin> skinsForCompanyPlaneDate = new ArrayList<Skin>();

        if (skinsForPlanes.containsKey(planeName))
        {
            SkinSet configuredSkins = skinsForPlanes.get(planeName).getConfiguredSkins();
            skinsForCompanyPlaneDate = new ArrayList<>(configuredSkins.getSkins().values());
            skinsForCompanyPlaneDate = SkinFilter.skinFilterCompany(skinsForCompanyPlaneDate, companyId);
            skinsForCompanyPlaneDate = SkinFilter.skinFilterDate(skinsForCompanyPlaneDate, date);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsByCompanyPlaneDateInUse: Invalid plane " + planeName);
        }
		
		return skinsForCompanyPlaneDate;
	}

    public List<Skin> getPersonalSkinsByPlaneCountryDateInUse(String planeName, String countryName, Date date)
    {
        List<Skin> skinsForCountryPlaneDate = new ArrayList<Skin>();

        if (skinsForPlanes.containsKey(planeName))
        {
        	SkinSet configuredSkins = skinsForPlanes.get(planeName).getConfiguredSkins();
        	skinsForCountryPlaneDate = new ArrayList<>(configuredSkins.getSkins().values());
        	skinsForCountryPlaneDate = SkinFilter.skinFilterCompany(skinsForCountryPlaneDate, Skin.PERSONAL_SKIN);
        	skinsForCountryPlaneDate = SkinFilter.skinFilterDate(skinsForCountryPlaneDate, date);
        	skinsForCountryPlaneDate = SkinFilter.skinFilterCountry(skinsForCountryPlaneDate, countryName);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getPersonalSkinsByPlaneCountryDateInUse: Invalid plane " + planeName);
        }
        
        return skinsForCountryPlaneDate;
    }

    public List<Skin> getSkinsByCompanyPlaneDate(String planeName, int companyId, Date date)
    {
        List<Skin> skinsForCompanyPlaneDate = new ArrayList<Skin>();

        if (skinsForPlanes.containsKey(planeName))
        {
        	SkinSet configuredSkins = skinsForPlanes.get(planeName).getConfiguredSkins();
        	skinsForCompanyPlaneDate = new ArrayList<>(configuredSkins.getSkins().values());
            skinsForCompanyPlaneDate = SkinFilter.skinFilterCompany(skinsForCompanyPlaneDate, companyId);
            skinsForCompanyPlaneDate = SkinFilter.skinFilterDate(skinsForCompanyPlaneDate, date);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsByCompanyPlaneDate: Invalid plane " + planeName);
        }
        
        return skinsForCompanyPlaneDate;
    }

    public List<Skin> getSkinsByPlaneCompany(String planeName, int companyId)
    {
        List<Skin> skinsForCompanyPlane = new ArrayList<Skin>();

        if (skinsForPlanes.containsKey(planeName))
        {
        	SkinSet configuredSkins = skinsForPlanes.get(planeName).getConfiguredSkins();
        	skinsForCompanyPlane = new ArrayList<>(configuredSkins.getSkins().values());
            skinsForCompanyPlane = SkinFilter.skinFilterCompany(skinsForCompanyPlane, companyId);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsByPlaneCompany: Invalid plane " + planeName);
        }
        
        return skinsForCompanyPlane;
    }

    public List<Skin> getSkinsByPlaneCountry(String planeName,  String countryName)
    {
        List<Skin> skinsForPlaneCountry = new ArrayList<Skin>();

        if (skinsForPlanes.containsKey(planeName))
        {
        	SkinSet configuredSkins = skinsForPlanes.get(planeName).getConfiguredSkins();
        	skinsForPlaneCountry = new ArrayList<>(configuredSkins.getSkins().values());
            skinsForPlaneCountry = SkinFilter.skinFilterCountry(skinsForPlaneCountry, countryName);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsByPlaneCountry: Invalid plane " + planeName);
        }
        
        return skinsForPlaneCountry;
    }

    public Skin getConfiguredSkinByName(String planeName, String skinName)
    {
        Skin returnSkin = null;

        if (skinsForPlanes.containsKey(planeName))
        {
        	SkinsForPlane skinsForPlane = skinsForPlanes.get(planeName);
            SkinSet configuredSkins = skinsForPlane.getConfiguredSkins();
            List<Skin>  skinsByName = new ArrayList<>(configuredSkins.getSkins().values());
            skinsByName = SkinFilter.skinFilterSkinName(skinsByName, skinName);
            
            if (skinsByName.size() > 0)
            {
                returnSkin = skinsByName.get(0);
            }
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getConfiguredSkinByName: Invalid plane " + planeName);
        }
        
        return returnSkin;
    }

    public List<Skin> getLooseSkinByPlane(String planeName)
    {
        List<Skin> looseSkinsForPlane = new ArrayList<>();

        if (skinsForPlanes.containsKey(planeName))
        {
            SkinSet looseSkins = skinsForPlanes.get(planeName).getLooseSkins();
            looseSkinsForPlane = new ArrayList<>(looseSkins.getSkins().values());
        }
        else
        {
            PWCGLogger.log(LogLevel.INFO, "getLooseSkinByPlane: Unconfigured plane for PWCG " + planeName);
        }
        
        return looseSkinsForPlane;
    }
    
    public Map<String, List<Skin>> getAllSkinsByPlane()
    {
        Map<String, List<Skin>> allSkinsInPWCG = new HashMap<String, List<Skin>>();
        
        for (String planeType : skinsForPlanes.keySet())
        {
            SkinsForPlane skinsForPlane = skinsForPlanes.get(planeType);
            List<Skin> skins = skinsForPlane.getAllUsedByPWCG();
            
            allSkinsInPWCG.put(planeType, skins);
        }
        
        return allSkinsInPWCG;
    }

    public String getSkinCategory(String planeName, String skinName)
    {
        if (skinsForPlanes.containsKey(planeName))
        {
            SkinsForPlane skinsForPlane = skinsForPlanes.get(planeName);
            return skinsForPlane.getSkinCategory(skinName);
        }
        
        return "";
    }

    public SkinsForPlane getSkinsForPlane(String planeTypeDesc)
    {
        return skinsForPlanes.get(planeTypeDesc);
    }

	public Map<String, SkinsForPlane> getSkinsForPlanes()
	{
		return skinsForPlanes;
	}
}
