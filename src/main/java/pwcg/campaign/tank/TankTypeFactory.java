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
        List<TankTypeInformation> alliedTanks = new ArrayList<TankTypeInformation>();

        for (TankTypeInformation tankType : tankTypes.values())
        {
            if (tankType.getSide() == side)
            {
                alliedTanks.add(tankType);
            }
        }

        return alliedTanks;
    }

    @Override
    public List<TankTypeInformation> getAllTanks() throws PWCGException
    {
        List<TankTypeInformation> allTanks = new ArrayList<TankTypeInformation>();
        Map<String, TankTypeInformation> allTanksSet = new HashMap<String, TankTypeInformation>();
        for (TankTypeInformation tank : tankTypes.values())
        {
            allTanksSet.put(tank.getType(), tank);
        }
        allTanks.addAll(allTanksSet.values());

        return allTanks;
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
    public TankTypeInformation createTankTypeByAnyName(String name) throws PWCGException
    {
        TankTypeInformation tank = this.getTankTypeByType(name);
        if (tank != null)
        {
            return tank;
        }

        tank = getTankByVehicleName(name);
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
        for (TankTypeInformation thisTank : tankTypes.values())
        {
            if (thisTank.getArchType().equals(tankArchType))
            {
                tankTypesForArchType.add(thisTank);
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
        for (TankTypeInformation thisTank : tankTypes.values())
        {
            if (thisTank.getArchType().equals(tankArchType))
            {
                if (DateUtils.isDateInRange(date, thisTank.getIntroduction(), thisTank.getWithdrawal()))
                {
                    tankTypesForArchType.add(thisTank);
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
        for (TankTypeInformation thisTank : tankTypes.values())
        {
            if (thisTank.getArchType().equals(tankArchType))
            {
                if (thisTank.getIntroduction().before(date))
                {
                    tankTypesForArchType.add(thisTank);
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
        for (TankTypeInformation thisTank : tankTypes.values())
        {
            if (thisTank.getArchType().equals(tankArchType))
            {
                tankTypesTypeByIntroduction.put(thisTank.getIntroduction(), thisTank);
            }
        }

        List<TankTypeInformation> tankTypesForArchType = new ArrayList<>(tankTypesTypeByIntroduction.values());
        return tankTypesForArchType;
    }

    @Override
    public List<TankTypeInformation> createActiveTankTypesForDateAndSide(Side side, Date date) throws PWCGException
    {
        List<TankTypeInformation> tankTypesForArchType = new ArrayList<>();
        for (TankTypeInformation thisTank : tankTypes.values())
        {
            if (DateUtils.isDateInRange(date, thisTank.getIntroduction(), thisTank.getWithdrawal()))
            {
                if (thisTank.getSide() == side)
                {
                    tankTypesForArchType.add(thisTank);
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
        List<TankTypeInformation> possibleTanks = new ArrayList<>();
        for (TankTypeInformation tankType : tankTypes.values())
        {
            if (tankType.isUsedBy(country))
            {
                if (!(tankType.getIntroduction().after(date)))
                {
                    if (tankType.isRoleCategory(roleCategory))
                    {
                        possibleTanks.add(tankType);
                    }
                }
            }
        }

        TankTypeInformation selectedTank = null;
        if (possibleTanks.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(possibleTanks.size());
            selectedTank = possibleTanks.get(index);
        }

        return selectedTank;
    }

    @Override
    public TankTypeInformation findAnyTankTypeForCountryAndDate(ICountry country, Date date) throws PWCGException
    {
        List<TankTypeInformation> possibleTanks = new ArrayList<>();
        for (TankTypeInformation tankType : tankTypes.values())
        {
            if (tankType.isUsedBy(country))
            {
                if (!(tankType.getIntroduction().after(date)))
                {
                    possibleTanks.add(tankType);
                }
            }
        }

        TankTypeInformation selectedTank = null;
        if (possibleTanks.size() > 0)
        {
            int index = RandomNumberGenerator.getRandom(possibleTanks.size());
            selectedTank = possibleTanks.get(index);
        }

        return selectedTank;
    }

    private TankTypeInformation getTankByDisplayName(String pwcgDesc) throws PWCGException
    {
        TankTypeInformation tank = null;

        for (TankTypeInformation thisTank : tankTypes.values())
        {
            if (thisTank.getDisplayName().equalsIgnoreCase(pwcgDesc))
            {
                tank = thisTank;
                break;
            }
        }

        return tank;
    }

    private TankTypeInformation getTankByVehicleName(String pwcgDesc) throws PWCGException
    {
        TankTypeInformation tank = null;

        for (TankTypeInformation thisTank : tankTypes.values())
        {
            if (thisTank.getVehicleName().equalsIgnoreCase(pwcgDesc))
            {
                tank = thisTank;
                break;
            }
        }

        return tank;
    }

    private TankTypeInformation getTankTypeByType(String tankTypeName) throws PWCGException
    {
        TankTypeInformation tank = null;

        for (TankTypeInformation thisTank : tankTypes.values())
        {
            if (thisTank.getType().equalsIgnoreCase(tankTypeName))
            {
                tank = thisTank;
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
