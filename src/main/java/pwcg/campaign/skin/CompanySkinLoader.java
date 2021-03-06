package pwcg.campaign.skin;

import java.util.Map;

import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class CompanySkinLoader
{
    private Map<String, SkinsForTank> skinsForTanks;
    
    public CompanySkinLoader (Map<String, SkinsForTank> skinsForTanks)
    {
        this.skinsForTanks = skinsForTanks;
    }

    public void loadCompanySkins() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        for (Company company : companyManager.getAllCompanies())
        {
            registerCompanySkins(company);
        }
        
    }

    private void registerCompanySkins(Company company)
    {
        for (Skin companySkin : company.getSkins())
        {
            if (!companySkin.getPlane().equals(""))
            {
                SkinsForTank skinsForPlane = skinsForTanks.get(companySkin.getPlane());
                if (skinsForPlane != null)
                {
                    skinsForPlane.addCompanySkin(companySkin);
                }
                else
                {
                    PWCGLogger.log(LogLevel.ERROR, "Invalid plane for company skin <" + company.getCompanyId()
                    + "><"  + companySkin.getSkinName() + ">" );
                }
            }
            for (String archTypeName : companySkin.getArchTypes())
            {
                TankArchType archType = PWCGContext.getInstance().getPlayerTankTypeFactory().getTankArchType(archTypeName);
                for (TankTypeInformation planeType : archType.getAllMemberTankTypes())
                {
                    String planeName = planeType.getType();
                    Skin planeSkin = companySkin.copy();
                    planeSkin.setPlane(planeName);
                    SkinsForTank skinsForPlane = skinsForTanks.get(planeName);
                    skinsForPlane.addCompanySkin(planeSkin);
                }
            }
        }
    }
}
