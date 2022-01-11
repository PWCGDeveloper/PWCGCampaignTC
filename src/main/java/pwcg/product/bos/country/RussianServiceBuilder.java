package pwcg.product.bos.country;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.ArmedServiceType;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.gui.colors.VVSColorMap;

public class RussianServiceBuilder
{
    public static String SSV_NAME ="Sovetskiye Sukhoputnye Voyska";
    public static String SSV_ICON ="ServiceSSV";

    public static List <ArmedService> createServices() throws PWCGException
    {
        List <ArmedService> russianServices = new ArrayList<ArmedService>();
        russianServices.add(createRussianArmy());
        return russianServices;
    }

    private static ArmedService createRussianArmy() throws PWCGException
    {
        ArmedService svv = new ArmedService();
        svv.setServiceId(TCServiceManager.SSV);
        svv.setCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        svv.setNameCountry(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        svv.setName(SSV_NAME);
        svv.setServiceIcon(SSV_ICON);
        svv.setStartDate(DateUtils.getBeginningOfGame());
        svv.setEndDate(DateUtils.getEndOfWar());
        svv.setServiceColorMap(new VVSColorMap());
        svv.setGeneralRankForService("General-lieutenant");
        svv.setDailyPersonnelReplacementRatePerCompany(2.2);
        svv.setDailyEquipmentReplacementRatePerCompany(3.0);

        List<String> irasPics = new ArrayList<String>();
        irasPics.add("Russian");
        svv.setPicDirs(irasPics);
        
        svv.addServiceQuality(DateUtils.getDateYYYYMMDD("19390101"), 10);
        svv.addServiceQuality(DateUtils.getDateYYYYMMDD("19420101"), 20);
        svv.addServiceQuality(DateUtils.getDateYYYYMMDD("19430101"), 40);
        svv.addServiceQuality(DateUtils.getDateYYYYMMDD("19440101"), 50);
        
        svv.setAirVictoriesForgreatAce(20);
        svv.setGroundVictoriesForgreatAce(100);

        svv.setArmedServiceType(ArmedServiceType.ARMED_SERVICE_GROUND);

        return svv;
    }
}
