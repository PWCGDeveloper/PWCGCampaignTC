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
    private Map<String, SkinsForTank> skinsForTank = new HashMap<>();
		
    public SkinManager ()
	{
	}

	public void initialize()
    {
        SkinLoader skinLoader = new SkinLoader();
        skinsForTank = skinLoader.loadPwcgSkins();
    }

    public List<Skin> getCompanySkinsByTankCompanyDate(String tankName, int companyId, Date date)
    {
    	List<Skin> skinsForCompany = new ArrayList<Skin>();

        if (skinsForTank.containsKey(tankName))
        {
        	SkinSet companySkinSet = skinsForTank.get(tankName).getCompanySkins();
        	skinsForCompany = new ArrayList<>(companySkinSet.getSkins().values());
            skinsForCompany = SkinFilter.skinFilterCompany(skinsForCompany, companyId);
            skinsForCompany = SkinFilter.skinFilterDate(skinsForCompany, date);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsByCompanyPlaneDateInUse: Invalid tank " + tankName);
        }
        
        return skinsForCompany;
    }

    public List<Skin> getSkinsByCompany(int companyId)
    {
        List<Skin> configuredSkinsForCompany = new ArrayList<Skin>();

        for (SkinsForTank skinsForPlane : skinsForTank.values())
        {
            SkinSet configuredSkins = skinsForPlane.getConfiguredSkins();
        	configuredSkinsForCompany = new ArrayList<>(configuredSkins.getSkins().values());
            List<Skin> skinsForCompanyAndPlane = SkinFilter.skinFilterCompany(configuredSkinsForCompany, companyId);
            
            configuredSkinsForCompany.addAll(skinsForCompanyAndPlane);
        }

        
        return configuredSkinsForCompany;
    }

	public List<Skin> getSkinsByTankCompanyDateInUse(String tankName, int companyId, Date date)
	{
        List<Skin> skinsForCompanyPlaneDate = new ArrayList<Skin>();

        if (skinsForTank.containsKey(tankName))
        {
            SkinSet configuredSkins = skinsForTank.get(tankName).getConfiguredSkins();
            skinsForCompanyPlaneDate = new ArrayList<>(configuredSkins.getSkins().values());
            skinsForCompanyPlaneDate = SkinFilter.skinFilterCompany(skinsForCompanyPlaneDate, companyId);
            skinsForCompanyPlaneDate = SkinFilter.skinFilterDate(skinsForCompanyPlaneDate, date);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsByCompanyPlaneDateInUse: Invalid tank " + tankName);
        }
		
		return skinsForCompanyPlaneDate;
	}

    public List<Skin> getPersonalSkinsByTankCountryDateInUse(String tankName, String countryName, Date date)
    {
        List<Skin> skinsForCountryPlaneDate = new ArrayList<Skin>();

        if (skinsForTank.containsKey(tankName))
        {
        	SkinSet configuredSkins = skinsForTank.get(tankName).getConfiguredSkins();
        	skinsForCountryPlaneDate = new ArrayList<>(configuredSkins.getSkins().values());
        	skinsForCountryPlaneDate = SkinFilter.skinFilterCompany(skinsForCountryPlaneDate, Skin.PERSONAL_SKIN);
        	skinsForCountryPlaneDate = SkinFilter.skinFilterDate(skinsForCountryPlaneDate, date);
        	skinsForCountryPlaneDate = SkinFilter.skinFilterCountry(skinsForCountryPlaneDate, countryName);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getPersonalSkinsByPlaneCountryDateInUse: Invalid tank " + tankName);
        }
        
        return skinsForCountryPlaneDate;
    }

    public List<Skin> getSkinsByCompanyTankDate(String tankName, int companyId, Date date)
    {
        List<Skin> skinsForCompanyPlaneDate = new ArrayList<Skin>();

        if (skinsForTank.containsKey(tankName))
        {
        	SkinSet configuredSkins = skinsForTank.get(tankName).getConfiguredSkins();
        	skinsForCompanyPlaneDate = new ArrayList<>(configuredSkins.getSkins().values());
            skinsForCompanyPlaneDate = SkinFilter.skinFilterCompany(skinsForCompanyPlaneDate, companyId);
            skinsForCompanyPlaneDate = SkinFilter.skinFilterDate(skinsForCompanyPlaneDate, date);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsByCompanyPlaneDate: Invalid tank " + tankName);
        }
        
        return skinsForCompanyPlaneDate;
    }

    public List<Skin> getSkinsByTankCompany(String tankName, int companyId)
    {
        List<Skin> skinsForCompanyPlane = new ArrayList<Skin>();

        if (skinsForTank.containsKey(tankName))
        {
        	SkinSet configuredSkins = skinsForTank.get(tankName).getConfiguredSkins();
        	skinsForCompanyPlane = new ArrayList<>(configuredSkins.getSkins().values());
            skinsForCompanyPlane = SkinFilter.skinFilterCompany(skinsForCompanyPlane, companyId);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsByPlaneCompany: Invalid tank " + tankName);
        }
        
        return skinsForCompanyPlane;
    }

    public List<Skin> getSkinsByTankCountry(String tankName,  String countryName)
    {
        List<Skin> skinsForPlaneCountry = new ArrayList<Skin>();

        if (skinsForTank.containsKey(tankName))
        {
        	SkinSet configuredSkins = skinsForTank.get(tankName).getConfiguredSkins();
        	skinsForPlaneCountry = new ArrayList<>(configuredSkins.getSkins().values());
            skinsForPlaneCountry = SkinFilter.skinFilterCountry(skinsForPlaneCountry, countryName);
        }
        else
        {
            PWCGLogger.log(LogLevel.ERROR, "getSkinsByPlaneCountry: Invalid tank " + tankName);
        }
        
        return skinsForPlaneCountry;
    }

    public Skin getConfiguredSkinByName(String tankName, String skinName)
    {
        Skin returnSkin = null;

        if (skinsForTank.containsKey(tankName))
        {
        	SkinsForTank skinsForPlane = skinsForTank.get(tankName);
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
            PWCGLogger.log(LogLevel.ERROR, "getConfiguredSkinByName: Invalid tank " + tankName);
        }
        
        return returnSkin;
    }

    public List<Skin> getLooseSkinByTank(String tankName)
    {
        List<Skin> looseSkinsForPlane = new ArrayList<>();

        if (skinsForTank.containsKey(tankName))
        {
            SkinSet looseSkins = skinsForTank.get(tankName).getLooseSkins();
            looseSkinsForPlane = new ArrayList<>(looseSkins.getSkins().values());
        }
        else
        {
            PWCGLogger.log(LogLevel.INFO, "getLooseSkinByPlane: Unconfigured tank for PWCG " + tankName);
        }
        
        return looseSkinsForPlane;
    }
    
    public Map<String, List<Skin>> getAllSkinsByTank()
    {
        Map<String, List<Skin>> allSkinsInPWCG = new HashMap<String, List<Skin>>();
        
        for (String tankType : skinsForTank.keySet())
        {
            SkinsForTank skinsForPlane = skinsForTank.get(tankType);
            List<Skin> skins = skinsForPlane.getAllUsedByPWCG();
            
            allSkinsInPWCG.put(tankType, skins);
        }
        
        return allSkinsInPWCG;
    }

    public String getSkinCategory(String tankName, String skinName)
    {
        if (skinsForTank.containsKey(tankName))
        {
            SkinsForTank skinsForPlane = skinsForTank.get(tankName);
            return skinsForPlane.getSkinCategory(skinName);
        }
        
        return "";
    }

    public SkinsForTank getSkinsForTank(String tankTypeDesc)
    {
        return skinsForTank.get(tankTypeDesc);
    }

	public Map<String, SkinsForTank> getSkinsForTank()
	{
		return skinsForTank;
	}
}
