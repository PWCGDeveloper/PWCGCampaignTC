package pwcg.campaign.tank;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;

public interface ITankTypeFactory
{
    void initialize() throws PWCGException;

    TankArchType getTankArchType(String tankArchTypeName);

    List<TankTypeInformation> getTanksForSide(Side side);

    List<TankTypeInformation> getAllTanks() throws PWCGException;

    List<TankTypeInformation> getAvailablePlayerTankTypes(ICountry country, PwcgRoleCategory roleCategory, Date date) throws PWCGException;

    List<TankTypeInformation> createTankTypesForArchType(String tankArchType) throws PWCGException;

    List<TankTypeInformation> createActiveTankTypesForArchType(String tankArchType, Date date) throws PWCGException;

    List<TankTypeInformation> createOlderTankTypesForArchType(String tankArchType, Date date) throws PWCGException;

    List<TankTypeInformation> createTanksByIntroduction(String tankArchType) throws PWCGException;

    List<TankTypeInformation> createActiveTankTypesForDateAndSide(Side side, Date date) throws PWCGException;

    TankTypeInformation getTankById(String tankTypeName) throws PWCGException;

    TankTypeInformation createTankTypeByAnyName(String name) throws PWCGException;

    TankTypeInformation findActiveTankTypeByCountryDateAndRole(ICountry country, Date date, PwcgRoleCategory roleCategory) throws PWCGException;

    TankTypeInformation findAnyTankTypeForCountryAndDate(ICountry country, Date date) throws PWCGException;

    Map<String, TankTypeInformation> getTankTypes();

    List<TankTypeInformation> getTanksForDate(Side side, Date date) throws PWCGException;

    List<TankTypeInformation> getTanksForRoleCategory(Date date, Side side, PwcgRoleCategory rolecategory) throws PWCGException;

}