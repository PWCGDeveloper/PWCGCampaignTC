package pwcg.campaign.tank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.skin.TacticalCodeColor;
import pwcg.campaign.tank.payload.TankPayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class TankTypeInformation implements Cloneable
{
    protected String type = "";
    protected String archType = "";
    protected int cruisingSpeed;
    protected int goodness = 50;
    protected ArrayList<PwcgRoleCategory> roleCategories = new ArrayList<>();
    protected Date introduction;
    protected Date withdrawal;
    protected Date endProduction;;
    protected boolean isPlayer = false;
    protected List<Country> primaryUsedBy = new ArrayList<>();
    protected List<TankPayloadElement> stockModifications = new ArrayList<>();
    protected TacticalCodeColor tacticalCodeColor = TacticalCodeColor.BLACK;

    public TankTypeInformation()
    {
    }

    public TankTypeInformation copy()
    {
        TankTypeInformation tankType = new TankTypeInformation();
        copyTemplate(tankType);
        return tankType;
    }

    public void copyTemplate(TankTypeInformation tankType)
    {
        tankType.type = this.type;
        tankType.archType = this.archType;

        tankType.cruisingSpeed = this.cruisingSpeed;
        tankType.goodness = this.goodness;

        tankType.roleCategories = new ArrayList<>();
        tankType.roleCategories.addAll(roleCategories);

        tankType.introduction = this.introduction;
        tankType.withdrawal = this.withdrawal;
        tankType.endProduction = this.endProduction;

        tankType.primaryUsedBy = new ArrayList<>(this.primaryUsedBy);
        tankType.tacticalCodeColor = this.tacticalCodeColor;
    }

    public int getCruisingSpeed()
    {
        return cruisingSpeed;
    }

    public void setCruisingSpeed(int cruisingSpeed)
    {
        this.cruisingSpeed = cruisingSpeed;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getArchType()
    {
        return archType;
    }

    public void setArchType(String archType)
    {
        this.archType = archType;
    }

    public boolean isRoleCategory(PwcgRoleCategory roleCategoryToFind)
    {
        if (roleCategories.size() == 0)
        {
            PWCGLogger.log(LogLevel.ERROR, "No roles for: " + getType());
        }
        
        for (PwcgRoleCategory roleCategory : roleCategories)
        {
            if (roleCategory == roleCategoryToFind)
            {
                return true;
            }
        }

        return false;
    }

    public boolean isPrimaryRole(PwcgRole role)
    {
        if (roleCategories.size() == 0)
        {
            PWCGLogger.log(LogLevel.ERROR, "No roles for: " + getType());
        }

        if (role.getRoleCategory() == determinePrimaryRoleCategory())
        {
            return true;
        }

        return false;
    }

    public boolean isPlaneActive(Date date)
    {
        if (getIntroduction().before(date))
        {
            if (getWithdrawal().after(date))
            {
                return true;
            }
        }

        return false;
    }
    
    public boolean isStockModification(TankPayloadElement modification)
    {
        for (TankPayloadElement stockModification : getStockModifications())
        {
            if (stockModification == modification)
            {
                return true;
            }
        }
        return false;
    }

    public PwcgRoleCategory determinePrimaryRoleCategory()
    {
        return getRoleCategories().get(0);
    }

    public Date getWithdrawal()
    {
        return this.withdrawal;
    }

    public void setWithdrawal(Date withdrawal)
    {
        this.withdrawal = withdrawal;
    }

    public Date getEndProduction()
    {
        return endProduction;
    }

    public void setEndProduction(Date endProduction)
    {
        this.endProduction = endProduction;
    }

    public Date getIntroduction()
    {
        return this.introduction;
    }

    public void setIntroduction(Date introduction)
    {
        this.introduction = introduction;
    }

    public ArrayList<PwcgRoleCategory> getRoleCategories()
    {
        return roleCategories;
    }

    public int getGoodness()
    {
        return goodness;
    }

    public void setGoodness(int goodness)
    {
        this.goodness = goodness;
    }

    public Side getSide()
    {
        ICountry country = CountryFactory.makeCountryByCountry(primaryUsedBy.get(0));
        return country.getSide();
    }

    public boolean isUsedBy(ICountry country)
    {
        for (Country countryEnum: primaryUsedBy) 
        {
            if (countryEnum == country.getCountry())
            {
                return true;
            }
        }

        return false;
    }

    public List<TankPayloadElement> getStockModifications()
    {
        return stockModifications;
    }

    public List<Country> getPrimaryUsedBy()
    {
        return primaryUsedBy;
    }

    public TacticalCodeColor getTacticalCodeColor()
    {
        return tacticalCodeColor;
    }

    public String getDisplayName() throws PWCGException
    {
        VehicleDefinition vehicleDefinition = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinition(type);
        return vehicleDefinition.getDisplayName();
    }

    public boolean isPlayer()
    {
        return isPlayer;
    }
}
