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
        String planeType = "bf109f4";
        ICountry iCountry = CountryFactory.makeCountryByCountry(Country.GERMANY);

        testSkins = skinManager.getLooseSkinByPlane(planeType);
        Assertions.assertTrue (testSkins.size() > 0);
        
        testSkins = skinManager.getPersonalSkinsByPlaneCountryDateInUse(planeType, iCountry.getCountryName(), DateUtils.getDateYYYYMMDD("19420801"));
        Assertions.assertTrue (testSkins.size() > 0);
        
        testSkins = skinManager.getSkinsByPlaneCompany(planeType, 20111051);
        Assertions.assertTrue (testSkins.size() == 0);
        
        testSkins = skinManager.getSkinsByPlaneCompany(planeType, TestIdentifiers.TEST_GERMAN_COMPANY_ID);
        Assertions.assertTrue (testSkins.size() > 0);
        
        testSkins = skinManager.getSkinsByPlaneCountry(planeType, iCountry.getCountryName());
        Assertions.assertTrue (testSkins.size() > 0);
        
        testSkins = skinManager.getSkinsByPlaneCompanyDateInUse(planeType, TestIdentifiers.TEST_GERMAN_COMPANY_ID, DateUtils.getDateYYYYMMDD("19420801"));
        Assertions.assertTrue (testSkins.size() > 0);
    }

}
