package pwcg.campaign.tank;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.campaign.tank.TankTypeFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class TankTypeFactoryTest
{
    @Mock Campaign campaign;
    
    public TankTypeFactoryTest() throws PWCGException
    {
        
    }

    @Test
    public void testCreatePlane() throws PWCGException
    {
        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        TankTypeInformation tankType =  tankTypeFactory.createTankTypeByType("_pziii-l");
        assert(tankType.getType().equals("_pziii-l"));
        assert(tankType.getArchType().equals("pziii"));
    }

    @Test
    public void testCreatePlaneByDesc() throws PWCGException
    {
        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        TankTypeInformation tankType =  tankTypeFactory.createTankTypeByAnyName("Pz.Kpfw.III Ausf.L");
        assert(tankType.getType().equals("_pziii-l"));
        assert(tankType.getArchType().equals("pziii"));
    }

    @Test
    public void getAvailableTankTypesTest() throws PWCGException
    {
        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        
        List<TankTypeInformation> availableGermanTankTypes = tankTypeFactory.getAvailableTankTypes(CountryFactory.makeCountryByCountry(Country.GERMANY), PwcgRoleCategory.MAIN_BATTLE_TANK, DateUtils.getDateYYYYMMDD("19431001"));        
        assert(availableGermanTankTypes.size() == 5);

        List<TankTypeInformation> availableBritishTankTypes = tankTypeFactory.getAvailableTankTypes(CountryFactory.makeCountryByCountry(Country.BRITAIN), PwcgRoleCategory.MAIN_BATTLE_TANK, DateUtils.getDateYYYYMMDD("19431001"));        
        assert(availableBritishTankTypes.size() == 1);

        List<TankTypeInformation> availableAmericanTankTypes = tankTypeFactory.getAvailableTankTypes(CountryFactory.makeCountryByCountry(Country.USA), PwcgRoleCategory.MAIN_BATTLE_TANK, DateUtils.getDateYYYYMMDD("19431001"));        
        assert(availableAmericanTankTypes.size() == 1);

        List<TankTypeInformation> availableRussianTankTypes = tankTypeFactory.getAvailableTankTypes(CountryFactory.makeCountryByCountry(Country.RUSSIA), PwcgRoleCategory.MAIN_BATTLE_TANK, DateUtils.getDateYYYYMMDD("19431001"));        
        assert(availableRussianTankTypes.size() == 3);
        
        List<TankTypeInformation> availableGermanAttackTankTypes = tankTypeFactory.getAvailableTankTypes(CountryFactory.makeCountryByCountry(Country.RUSSIA), PwcgRoleCategory.SELF_PROPELLED_GUN, DateUtils.getDateYYYYMMDD("19431001"));        
        assert(availableGermanAttackTankTypes.size() == 2);
    }

    @Test
    public void testAllTankTypes() throws PWCGException
    {
        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        Map<String, TankTypeInformation> tankTypes =  tankTypeFactory.getTankTypes();
        assert(tankTypes.size() > 10);
        for (TankTypeInformation tankType : tankTypes.values())
        {
            assert(tankType.getArchType() != null);
            assert(tankType.getIntroduction().after(DateUtils.getDateYYYYMMDD("19390901")));
            assert(tankType.getWithdrawal().before(DateUtils.getDateYYYYMMDD("1945601")));
            assert(tankType.getEndProduction().before(DateUtils.getDateYYYYMMDD("1945601")));
            assert(tankType.getRoleCategories().size() > 0);
            assert(tankType.getPrimaryUsedBy().size() > 0);
        }
    }

    @Test
    public void testCreateTanksForArchType() throws PWCGException
    {
        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        List<TankTypeInformation> tankTypes =  tankTypeFactory.createTankTypesForArchType("t34");
        assert(tankTypes.size() == 2);
        for (TankTypeInformation tankType : tankTypes)
        {
            assert(tankType.getArchType().equals("t34"));
        }
    }

    @Test
    public void testCreateActiveTanksForArchType() throws PWCGException
    {
        Date tankDate = DateUtils.getDateYYYYMMDD("19420402");
        
        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        List<TankTypeInformation> tankTypes =  tankTypeFactory.createActiveTankTypesForArchType("t34", tankDate);
        assert(tankTypes.size() == 1);
        for (TankTypeInformation tankType : tankTypes)
        {
            assert(tankType.getArchType().equals("t34"));
        }
    }
}
