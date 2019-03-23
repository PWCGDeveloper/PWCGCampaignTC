package pwcg.campaign.medals;

import java.util.Date;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.campaign.ww1.country.RoFServiceManager;
import pwcg.campaign.ww1.medals.BritishMedalManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.dialogs.ImageCache;
import pwcg.gui.utils.ContextSpecificImages;

@RunWith(MockitoJUnitRunner.class)
public class RoFBritishMedalManagerTest extends MedalManagerTestBase
{
    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        super.setup();
        Mockito.when(country.isCountry(Country.BRITAIN)).thenReturn(true);
        ICountry country = CountryFactory.makeCountryByCountry(Country.BRITAIN);
        medalManager = MedalManagerFactory.createMedalManager(country, campaign);
        medals.clear();
    }
    
    @Test
    public void testRFCFMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170201"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(RoFServiceManager.RFC, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(BritishMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(BritishMedalManager.MC, 3, 1);
		awardMedal(BritishMedalManager.DSO, 15, 1);
		awardMedal(BritishMedalManager.DSO_BAR, 25, 2);
		awardMedal(BritishMedalManager.VC, 35, 3);
    }
    
    @Test
    public void testRNASMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170201"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(RoFServiceManager.RNAS, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(BritishMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(BritishMedalManager.DSC, 3, 1);
		awardMedal(BritishMedalManager.DSC_BAR, 12, 2);
		awardMedal(BritishMedalManager.DSO, 15, 1);
		awardMedal(BritishMedalManager.DSO_BAR, 25, 2);
		awardMedal(BritishMedalManager.VC, 40, 2);
    }
    
    @Test
    public void testRAFMedals () throws PWCGException
    {            	
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getRAFDate());
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(RoFServiceManager.RFC, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(BritishMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(BritishMedalManager.DFC, 8, 1);
		awardMedal(BritishMedalManager.DFC_BAR_1, 12, 2);
		awardMedal(BritishMedalManager.DSO, 15, 1);
		awardMedal(BritishMedalManager.DSO_BAR, 25, 2);
		awardMedal(BritishMedalManager.DFC_BAR_2, 30, 3);
		awardMedal(BritishMedalManager.VC, 40, 2);
    }

    @Test
    public void testVCFail () throws PWCGException
    {            
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19170201"));
	    service = ArmedServiceFactory.createServiceManager().getArmedServiceById(RoFServiceManager.RFC, campaign.getDate());
        Mockito.when(player.determineService(Matchers.<Date>any())).thenReturn(service);

        awardMedal(BritishMedalManager.PILOTS_BADGE, 0, 0);
		awardMedal(BritishMedalManager.MC, 3, 1);
		awardMedal(BritishMedalManager.DSO, 15, 1);
		awardMedal(BritishMedalManager.DSO_BAR, 25, 2);

    	makeVictories(35);
        Medal medal = medalManager.award(campaign, player, service, 1);
        assert (medal == null);
    }

    @Test
    public void testImages () throws PWCGException
    {            
    	for (Medal medal : medalManager.getAllAwardsForService())
    	{
	        String medalPath = ContextSpecificImages.imagesMedals() + "Allied\\" + medal.getMedalImage();
	        ImageIcon medalIcon = ImageCache.getInstance().getImageIcon(medalPath);
	        assert (medalIcon != null);
    	}
    }	
}
