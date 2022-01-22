package pwcg.campaign.tank;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;

abstract class TankTypeFactory implements ITankTypeFactory
{
    protected Map<String, TankTypeInformation> tankTypes = new TreeMap<>();
    protected Map<String, TankArchType> tankArchTypes = new TreeMap<>();

    protected void createTankArchTypes()
    {
        for (TankTypeInformation tankType : tankTypes.values())
        {
            if (!tankArchTypes.containsKey(tankType.getArchType()))
            {
                TankArchType tankArchType = new TankArchType(tankType.getArchType());
                tankArchTypes.put(tankType.getArchType(), tankArchType);
            }

            TankArchType tankArchType = tankArchTypes.get(tankType.getArchType());
            tankArchType.addTankTypeToArchType(tankType);
        }
    }

    public void dump() throws PWCGException
    {
        for (TankTypeInformation tankType : tankTypes.values())
        {
            PWCGLogger.log(LogLevel.DEBUG, "" + tankType.getType() + "    " + tankType.getDisplayName());
        }
    }

    @Override
    public TankArchType getTankArchType(String tankArchTypeName)
    {
        return tankArchTypes.get(tankArchTypeName);
    }

    @Override
    public List<TankTypeInformation> getTanksForSide(Side side)
    {
        List<TankTypeInformation> alliedPlanes = new ArrayList<TankTypeInformation>();

        for (TankTypeInformation tankType : tankTypes.values())
        {
            if (tankType.getSide() == side)
            {
                alliedPlanes.add(tankType);
            }
        }

        return alliedPlanes;
    }

    @Override
    public List<TankTypeInformation> getAllTanks() throws PWCGException
    {
        List<TankTypeInformation> allPlanes = new ArrayList<TankTypeInformation>();
        Map<String, TankTypeInformation> allPlanesSet = new HashMap<String, TankTypeInformation>();
        for (TankTypeInformation tank : tankTypes.values())
        {
            allPlanesSet.put(tank.getType(), tank);
        }
        allPlanes.addAll(allPlanesSet.values());

        return allPlanes;
    }

    @Override
    public TankTypeInformation getTankById(String tankTypeName) throws PWCGException
    {
        TankTypeInformation tank = null;
        if (tankTypes.containsKey(tankTypeName))
        {
            tank = tankTypes.get(tankTypeName);
        }
        else
        {
            throw new PWCGException("Invalid tank id: " + tankTypeName);
        }

        return tank;
    }

    @Override
    public TankTypeInformation createTankTypeByType(String tankTypeName) throws PWCGException
    {
        TankTypeInformation tank = null;

        for (TankTypeInformation thisPlane : tankTypes.values())
        {
            if (thisPlane.getType().equalsIgnoreCase(tankTypeName))
            {
                tank = thisPlane;
                break;
            }
        }

        if (tank == null)
        {
            throw new PWCGException("Invalid tank name: " + tankTypeName);
        }

        return tank;
    }

    @Override
    public TankTypeInformation createTankTypeByAnyName(String name) throws PWCGException
    {
        TankTypeInformation tank = getTankByTankType(name);
        if (tank != null)
        {
            return tank;
        }

        tank = getTankByDisplayName(name);
        if (tank != null)
        {
            return tank;
        }

        return null;
    }

    @Override
    public List<TankTypeInformation> getAvailablePlayerTankTypes(ICountry country, PwcgRoleCategory roleCategory, Date date) throws PWCGException
    {
        Map<Integer, TankTypeInformation> availableTankTypes = new TreeMap<>();
        for (TankTypeInformation thisTank : tankTypes.values())
        {
            if (thisTank.isPlayer)
            {
                if (thisTank.isUsedBy(country))
                {
                    if (thisTank.isRoleCategory(roleCategory))
                    {
                        if (DateUtils.isDateInRange(date, thisTank.getIntroduction(), thisTank.getWithdrawal()))
                        {
                            availableTankTypes.put(thisTank.getGoodness(), thisTank);
                        }
                    }
                }
            }
        }

        return new ArrayList<>(availableTankTypes.values());
    }

    @Override
    public List<TankTypeInformation> createTankTypesForArchType(String tankArchType) throws PWCGException
    {
        List<TankTypeInformation> tankTypesForArchType = new ArrayList<>();
        for (TankTypeInformation thisPlane : tankTypes.values())
        {
            if (thisPlane.getArchType().equals(tankArchType))
            {
                tankTypesForArchType.add(thisPlane);
            }
        }

        if (tankTypesForArchType.isEmpty())
        {
            throw new PWCGException("No tanks found for archtype " + tankArchType);
        }

        return tankTypesForArchType;
    }

    @Override
    public List<TankTypeInformation> createActiveTankTypesForArchType(String tankArchType, Date date) throws PWCGException
    {
        List<TankTypeInformation> tankTypesForArchType = new ArrayList<>();
        for (TankTypeInformation thisPlane : tankTypes.values())
        {
            if (thisPlane.getArchType().equals(tankArchType))
            {
                if (DateUtils.isDateInRange(date, thisPlane.getIntroduction(), thisPlane.getWithdrawal()))
                {
                    tankTypesForArchType.add(thisPlane);
                }
            }
        }

        if (tankTypesForArchType.isEmpty())
        {
            tankTypesForArchType = createOlderTankTypesForArchType(tankArchType, date);
        }

        if (tankTypesForArchType.isEmpty())
        {
            throw new PWCGException("No tanks found for in range archtype " + tankArchType);
        }

        return tankTypesForArchType;
    }

    @Override
    public List<TankTypeInformation> createOlderTankTypesForArchType(String tankArchType, Date date) throws PWCGException
    {
        List<TankTypeInformation> tankTypesForArchType = new ArrayList<>();
        for (TankTypeInformation thisPlane : tankTypes.values())
        {
            if (thisPlane.getArchType().equals(tankArchType))
            {
                if (thisPlane.getIntroduction().before(date))
                {
                    tankTypesForArchType.add(thisPlane);
                }
            }
        }

        if (tankTypesForArchType.isEmpty())
        {
            throw new PWCGException("No older tanks found for archtype " + tankArchType);
        }

        return tankTypesForArchType;
    }

    @Override
    public List<TankTypeInformation> createTanksByIntroduction(String tankArchType) throws PWCGException
    {
        TreeMap<Date, TankTypeInformation> tankTypesTypeByIntroduction = new TreeMap<>();
        for (TankTypeInformation thisPlane : tankTypes.values())
        {
            if (thisPlane.getArchType().equals(tankArchType))
            {
                tankTypesTypeByIntroduction.put(thisPlane.getIntroduction(), thisPlane);
            }
        }

        List<TankTypeInformation> tankTypesForArchType = new ArrayList<>(tankTypesTypeByIntroduction.values());
        return tankTypesForArchType;
    }

    @Override
    public List<TankTypeInformation> createActiveTankTypesForDateAndSide(Side side, Date date) throws PWCGException
    {
        List<TankTypeInformation> tankTypesForArchType = new ArrayList<>();
        for (TankTypeInformation thisPlane : tankTypes.values())
        {
            if (DateUtils.isDateInRange(date, thisPlane.getIntroduction(), thisPlane.getWithdrawal()))
            {
                if (thisPlane.getSide() == side)
                {
                    tankTypesForArchType.add(thisPlane);
                }
            }
        }

        if (tankTypesForArchType.isEmpty())
        {
            throw new PWCGException("No tanks found for date " + DateUtils.getDateStringDashDelimitedYYYYMMDD(date));
        }

        return tankTypesForArchType;
    }

    @Override
    public TankTypeInformation findActiveTankTypeByCountryDateAndRole(ICountry country, Date date, PwcgRoleCategory roleCategory) throws PWCGException
    {
        List<TankTypeInformation> possiblePlanes = new ArrayList<>();
        for (TankTypeInformation tankType : tankTypes.values())
        {
            if (tankType.isUsedBy(country))
            {
                if (!(tankType.getIntroduction().after(date)))
                {
                    if (tankType.isRoleCategory(roleCategory))
                    {
                        possiblePlanes.add(tankType);
                    }
                }
            }
        }

        TankTypeInformation selectedPlane = null;
        if (possiblePlanes.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(possiblePlanes.size());
            selectedPlane = possiblePlanes.get(index);
        }

        return selectedPlane;
    }

    @Override
    public TankTypeInformation findAnyTankTypeForCountryAndDate(ICountry country, Date date) throws PWCGException
    {
        List<TankTypeInformation> possiblePlanes = new ArrayList<>();
        for (TankTypeInformation tankType : tankTypes.values())
        {
            if (tankType.isUsedBy(country))
            {
                if (!(tankType.getIntroduction().after(date)))
                {
                    possiblePlanes.add(tankType);
                }
            }
        }

        TankTypeInformation selectedPlane = null;
        if (possiblePlanes.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(possiblePlanes.size());
            selectedPlane = possiblePlanes.get(index);
        }

        return selectedPlane;
    }

    @Override
    public TankTypeInformation getTankByDisplayName(String pwcgDesc) throws PWCGException
    {
        TankTypeInformation tank = null;

        for (TankTypeInformation thisPlane : tankTypes.values())
        {
            if (thisPlane.getDisplayName().equalsIgnoreCase(pwcgDesc))
            {
                tank = thisPlane;
                break;
            }
        }

        return tank;
    }

    private TankTypeInformation getTankByTankType(String abrevName)
    {
        TankTypeInformation tank = null;

        for (TankTypeInformation thisPlane : tankTypes.values())
        {
            if (abrevName.equalsIgnoreCase(thisPlane.getType()))
            {
                tank = thisPlane;
                break;
            }
        }

        return tank;
    }

    @Override
    public Map<String, TankTypeInformation> getTankTypes()
    {
        return tankTypes;
    }
}
