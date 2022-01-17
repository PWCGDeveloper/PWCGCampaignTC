package pwcg.campaign.medals;

import java.util.Date;

import javax.swing.ImageIcon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.image.ImageIconCache;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.product.bos.country.TCServiceManager;
import pwcg.product.bos.medals.BritishMedalManager;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BritishMedalManagerTest extends MedalManagerTestBase
{
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        
        super.setupBase();
        Mockito.when(country.isCountry(Country.BRITAIN)).thenReturn(true);
        ICountry country = CountryFactory.makeCountryByCountry(Country.BRITAIN);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        medals.clear();
    }
    
    @Test
    public void testOfficerMedals () throws PWCGException
    {               
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getStartofWWIIBritain());
        service = ArmedServiceFactory.createServiceManager().getArmedServiceById(TCServiceManager.BRITISH_ARMY);
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);
        Mockito.when(player.determineRankPos(ArgumentMatchers.<Date>any())).thenReturn(2);

        awardMedal(BritishMedalManager.CREWS_BADGE, 0, 0);
        awardMedal(BritishMedalManager.MC, 2, 1);
        awardMedal(BritishMedalManager.DSC, 5, 1);
        awardMedal(BritishMedalManager.DSO, 10, 1);
        awardMedal(BritishMedalManager.DSO_BAR, 15, 1);
        awardMedal(BritishMedalManager.VC, 30, 2);
    }
    
    @Test
    public void testEnlistedrMedals () throws PWCGException
    {               
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getStartofWWIIBritain());
        service = ArmedServiceFactory.createServiceManager().getArmedServiceById(TCServiceManager.BRITISH_ARMY);
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);
        Mockito.when(player.determineRankPos(ArgumentMatchers.<Date>any())).thenReturn(3);

        awardMedal(BritishMedalManager.CREWS_BADGE, 0, 0);
        awardMedal(BritishMedalManager.MM, 2, 1);
        awardMedal(BritishMedalManager.DSM, 5, 1);
        awardMedal(BritishMedalManager.DSO, 10, 1);
        awardMedal(BritishMedalManager.DSO_BAR, 15, 1);
        awardMedal(BritishMedalManager.VC, 20, 5);
    }

    @Test
    public void testVCFail () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getStartofWWIIBritain());
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(TCServiceManager.BRITISH_ARMY);
        Mockito.when(player.determineService(ArgumentMatchers.<Date>any())).thenReturn(service);
        Mockito.when(player.determineRankPos(ArgumentMatchers.<Date>any())).thenReturn(3);

        awardMedal(BritishMedalManager.CREWS_BADGE, 0, 0);
        awardMedal(BritishMedalManager.MM, 2, 1);
        awardMedal(BritishMedalManager.DSM, 5, 1);
        awardMedal(BritishMedalManager.DSO, 10, 1);
        awardMedal(BritishMedalManager.DSO_BAR, 15, 1);

    	makeVictories(35);
        Medal medal = medalManager.award(campaign, player, service, 1);
        Assertions.assertTrue (medal == null);
    }

    @Test
    public void testImages () throws PWCGException
    {            
    	for (Medal medal : medalManager.getAllAwardsForService())
    	{
	        String medalPath = ContextSpecificImages.imagesMedals() + "Allied\\" + medal.getMedalImage();
	        ImageIcon medalIcon = ImageIconCache.getInstance().getImageIcon(medalPath);
	        Assertions.assertTrue (medalIcon != null);
    	}
    }	
}
