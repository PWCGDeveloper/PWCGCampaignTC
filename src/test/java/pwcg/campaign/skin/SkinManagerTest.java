package pwcg.campaign.skin;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.testutils.TestIdentifiers;

public class SkinManagerTest
{
    public SkinManagerTest() throws PWCGException
    {
        
    }

    @Test
    public void skinLoaderInitializeTest() throws PWCGException
    {
        SkinManager skinManager = PWCGContext.getInstance().getSkinManager();
        List<Skin> testSkins;
        String tankType = "pziiif4";
        ICountry iCountry = CountryFactory.makeCountryByCountry(Country.GERMANY);

        testSkins = skinManager.getLooseSkinByTank(tankType);
        Assertions.assertTrue (testSkins.size() == 0);
        
        testSkins = skinManager.getPersonalSkinsByTankCountryDateInUse(tankType, iCountry.getCountryName(), DateUtils.getDateYYYYMMDD("19420801"));
        Assertions.assertTrue (testSkins.size() == 0);
        
        testSkins = skinManager.getSkinsByTankCompany(tankType, TestIdentifiers.TEST_GERMAN_COMPANY_ID);
        Assertions.assertTrue (testSkins.size() == 0);
        
        testSkins = skinManager.getSkinsByTankCompany(tankType, TestIdentifiers.TEST_GERMAN_COMPANY_ID);
        Assertions.assertTrue (testSkins.size() == 0);
        
        testSkins = skinManager.getSkinsByTankCountry(tankType, iCountry.getCountryName());
        Assertions.assertTrue (testSkins.size() == 0);
        
        testSkins = skinManager.getSkinsByTankCompanyDateInUse(tankType, TestIdentifiers.TEST_GERMAN_COMPANY_ID, DateUtils.getDateYYYYMMDD("19420801"));
        Assertions.assertTrue (testSkins.size() == 0);
    }

}
