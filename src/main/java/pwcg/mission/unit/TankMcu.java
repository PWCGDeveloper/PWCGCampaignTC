package pwcg.mission.unit;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankType;
import pwcg.campaign.tank.payload.ITankPayload;
import pwcg.campaign.tank.payload.TankPayloadFactory;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogCategory;
import pwcg.mission.mcu.McuEvent;
import pwcg.mission.mcu.McuTREntity;
import pwcg.product.bos.config.TCProductSpecificConfiguration;

public class TankMcu extends EquippedTank implements Cloneable
{
    private String name = "";
    private int index;
    private int linkTrId;
    private Coordinate position;
    private Orientation orientation;
    private Skin skin = null;
    private AiSkillLevel aiSkillLevel = AiSkillLevel.NOVICE;
    private int coopStart = 0;
    private int numberInFormation = 1;
    private int vulnerable = 1;
    private int engageable = 1;
    private int limitAmmo = 1;
    private Callsign callsign = Callsign.NONE;
    private int callnum = 0;
    private int time = 60;
    private double fuel = .6;
    private int damageReport = 50;
    private ICountry country = CountryFactory.makeNeutralCountry();
    private int damageThreshold = 1;
    private int deleteAfterDeath = 1;

    private ITankPayload payload = null;

    private McuTREntity entity;

    private Campaign campaign;
    private CrewMember crewMember;

    public TankMcu()
    {
        super();
        this.index = IndexGenerator.getInstance().getNextIndex();
        this.entity = new McuTREntity(index);
        this.linkTrId = entity.getIndex();
    }

    public TankMcu(Campaign campaign, CrewMember crewMember)
    {
        super();

        this.campaign = campaign;
        this.crewMember = crewMember;

        this.index = IndexGenerator.getInstance().getNextIndex();
        this.entity = new McuTREntity(index);
        this.linkTrId = entity.getIndex();
    }
    
    public void buildTank(TankType tankType, ICountry country) throws PWCGException
    {
        tankType.copyTemplate(this);
        this.setName(crewMember.getNameAndRank());
        this.setDesc(crewMember.getNameAndRank());
        this.setCountry(country);
        setDeleteAfterDeath();
    }

    private void setDeleteAfterDeath() throws PWCGException
    {
        int isDeleteAfterDeath = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.DeleteAfterDeathKey);
        if (isDeleteAfterDeath == 0) 
        {
            deleteAfterDeath = 0;
        }
    }

    public TankMcu copy()
    {
        TankMcu tank = new TankMcu();
        copyTemplate(tank);
        return tank;
    }

    private void copyTemplate(TankMcu tank)
    {
        super.copyTemplate(tank);

        tank.name = this.name;
        tank.position = this.position;
        tank.orientation = this.orientation;
        tank.skin = this.skin;
        tank.aiSkillLevel = this.aiSkillLevel;
        tank.coopStart = this.coopStart;
        tank.numberInFormation = this.numberInFormation;
        tank.vulnerable = this.vulnerable;
        tank.engageable = this.engageable;
        tank.limitAmmo = this.limitAmmo;
        tank.callsign = this.callsign;
        tank.callnum = this.callnum;
        tank.time = this.time;
        tank.fuel = this.fuel;
        tank.damageReport = this.damageReport;
        tank.country = CountryFactory.makeCountryByCountry(this.country.getCountry());
        tank.damageThreshold = this.damageThreshold;
        tank.deleteAfterDeath = this.deleteAfterDeath;
        if (payload != null)
        {
            tank.payload = this.payload.copy();
        }
        else
        {
            tank.payload = null;
        }

        tank.index = IndexGenerator.getInstance().getNextIndex();
        tank.entity = this.entity.copy(tank.index);
        tank.linkTrId = tank.entity.getIndex();

        tank.campaign = this.campaign;
        tank.crewMember = this.crewMember;
    }

    public void populateEntity(TankMcu unitLeader)
    {
        if (unitLeader.getIndex() != index)
        {
            entity.setTarget(unitLeader.getLinkTrId());
        }
    }

    public boolean isActivePlayerTank() throws PWCGException
    {
        boolean isPlayerTank = false;
        if (getAiLevel() == AiSkillLevel.PLAYER)
        {
            isPlayerTank = true;
        }

        CrewMembers companyMembers = campaign.getPersonnelManager().getAllActivePlayers();
        if (companyMembers.isCrewMember(serialNumber))
        {
            isPlayerTank = true;
        }

        return isPlayerTank;
    }
    
    public ITankPayload buildTankPayload(IPlayerUnit unit, Date date) throws PWCGException
    {
        TankPayloadFactory payloadFactory = new TankPayloadFactory();        
        payload = payloadFactory.createPayload(this.getType(), date);
        payload.createWeaponsPayload(unit);
        return payload.copy();
    }

    public ITankPayload buildStandardTankPayload(Date date) throws PWCGException
    {
        TankPayloadFactory payloadFactory = new TankPayloadFactory();        
        payload = payloadFactory.createPayload(this.getType(), date);
        payload.createStandardWeaponsPayload();
        return payload.copy();
    }

    public void setTankPayload(ITankPayload payload) throws PWCGException
    {
        if (payload != null)
        {
            this.payload = payload.copy();
        }
    }

    public ITankPayload getTankPayload() throws PWCGException
    {
        if (payload != null)
        {
            return this.payload.copy();
        }

        return null;
    }

    public void setVehicleSkinWithCheck(Skin newSkin)
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        Date campaignDate = campaign.getDate();

        if (!(campaignDate.before(newSkin.getStartDate())))
        {
            if (!(campaignDate.after(newSkin.getEndDate())))
            {
                if (newSkin.skinExists(Skin.PRODUCT_SKIN_DIR) || newSkin.isDefinedInGame())
                {
                    this.skin = newSkin;
                }
                else
                {
                    PWCGLogger.logByCategory(LogCategory.SKIN, "setVehicleSkin: skin rejected by skin exists");
                }
            }
            else
            {
                PWCGLogger.logByCategory(LogCategory.SKIN,
                        "setVehicleSkin: skin rejected by end date: " + DateUtils.getDateStringDashDelimitedYYYYMMDD(newSkin.getEndDate()));
            }
        }
        else
        {
            PWCGLogger.logByCategory(LogCategory.SKIN,
                    "setVehicleSkin: skin rejected by start date: " + DateUtils.getDateStringDashDelimitedYYYYMMDD(newSkin.getStartDate()));
        }
    }

    private void validateFuel()
    {
        if (fuel < .3)
        {
            fuel = .3;
        }

        if (fuel > 1.0)
        {
            fuel = 1.0;
        }
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Vehicle");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            super.write(writer);

            writer.write("  Name = \"\u0001" + name + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  LinkTrId = " + linkTrId + ";");
            writer.newLine();

            position.write(writer);
            orientation.write(writer);

            country.writeAdjusted(writer);

            String skinName = "";
            if (skin != null)
            {
                skinName = getType() + "/" + skin.getSkinName();
                skinName = skinName.toLowerCase();
                if (!skinName.contains(".dds"))
                {
                    skinName += ".dds";
                }
            }

            writer.write("  Skin = \"" + skinName + "\";");
            writer.newLine();
            writer.write("  AILevel = " + aiSkillLevel.getAiSkillLevel() + ";");
            writer.newLine();
            writer.write("  CoopStart = " + coopStart + ";");
            writer.newLine();
            writer.write("  NumberInFormation = " + numberInFormation + ";");
            writer.newLine();
            writer.write("  Vulnerable = " + vulnerable + ";");
            writer.newLine();
            writer.write("  Engageable = " + engageable + ";");
            writer.newLine();
            writer.write("  LimitAmmo = " + limitAmmo + ";");
            writer.newLine();
            writer.write("  Time = " + time + ";");
            writer.newLine();
            writer.write("  DamageReport = " + damageReport + ";");
            writer.newLine();
            writer.write("  DamageThreshold = " + damageThreshold + ";");
            writer.newLine();
            writer.write("  PayloadId = " + payload.getSelectedPayloadId() + ";");
            writer.newLine();
            writer.write("  DeleteAfterDeath = " + deleteAfterDeath + ";");
            writer.newLine();
            writer.write("  Fuel = " + fuel + ";");
            writer.newLine();

            // BoS specific parameters
            TCProductSpecificConfiguration productSpecificConfiguration =new TCProductSpecificConfiguration();
            if (productSpecificConfiguration.useCallSign())
            {
                writer.write("  Callsign = " + callsign.getNum(country.getCountry()) + ";");
                writer.newLine();
                writer.write("  Callnum = " + callnum + ";");
                writer.newLine();
            }

            writer.write("  WMMask = " + payload.generateFullModificationMask() + ";");
            writer.newLine();

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();

            entity.write(writer);
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    public void populateEntity(IPlayerUnit unit, TankMcu unitLeader)
    {
        if (unitLeader.getIndex() != index)
        {
            entity.setTarget(unitLeader.getLinkTrId());
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getIndex()
    {
        return index;
    }

    public int getLinkTrId()
    {
        return linkTrId;
    }

    public Coordinate getPosition()
    {
        return position.copy();
    }

    public void setPosition(Coordinate position)
    {
        this.position = position;

        if (entity != null)
        {
            entity.setPosition(position);
        }
    }

    public Orientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;

        if (entity != null)
        {
            entity.setOrientation(orientation);
        }
    }

    public Skin getVehicleSkin()
    {
        return skin;
    }

    public void setVehicleSkin(Skin newSkin)
    {
        if (newSkin != null)
        {
            this.skin = newSkin;
        }
    }

    public AiSkillLevel getAiLevel()
    {
        return aiSkillLevel;
    }

    public void setAiLevel(AiSkillLevel aiLevel)
    {
        this.aiSkillLevel = aiLevel;
    }

    public int getCoopStart()
    {
        return coopStart;
    }

    public void setCoopStart(int coopStart)
    {
        this.coopStart = coopStart;
    }

    public int getNumberInFormation()
    {
        return numberInFormation;
    }

    public void setNumberInFormation(int numberInFormation)
    {
        this.numberInFormation = numberInFormation;
    }

    public int getVulnerable()
    {
        return vulnerable;
    }

    public void setVulnerable(int vulnerable)
    {
        this.vulnerable = vulnerable;
    }

    public int getEngageable()
    {
        return engageable;
    }

    public void setEngageable(int engageable)
    {
        this.engageable = engageable;
    }

    public int getLimitAmmo()
    {
        return limitAmmo;
    }

    public void setLimitAmmo(int limitAmmo)
    {
        this.limitAmmo = limitAmmo;
    }

    public int getTime()
    {
        return time;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    public int getDamageReport()
    {
        return damageReport;
    }

    public void setDamageReport(int damageReport)
    {
        this.damageReport = damageReport;
    }

    public int getDamageThreshold()
    {
        return damageThreshold;
    }

    public void setDamageThreshold(int damageThreshold)
    {
        this.damageThreshold = damageThreshold;
    }

    public ICountry getCountry()
    {
        return country;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;
        this.setSide(country.getSide());
    }

    public double getFuel()
    {
        return fuel;
    }

    public void setFuel(double fuel)
    {
        this.fuel = fuel;

        validateFuel();
    }

    public CrewMember getCrewMember()
    {
        return crewMember;
    }

    public void replaceCrewMember(CrewMember newCrewMember)
    {
        this.crewMember = newCrewMember;
    }

    public Callsign getCallsign()
    {
        return callsign;
    }

    public void setCallsign(Callsign callsign)
    {
        this.callsign = callsign;
    }

    public int getCallnum()
    {
        return callnum;
    }

    public void setCallnum(int callnum)
    {
        this.callnum = callnum;
    }

    public void setLinkTrId(int linkTrId)
    {
        this.linkTrId = linkTrId;
    }

    public Skin getSkin()
    {
        return skin;
    }

    public void setTarget(int target)
    {
        entity.setTarget(target);
    }

    public void copyEntityIndexFromTank(TankMcu unitLeaderTankMcu)
    {
        entity.setIndex(unitLeaderTankMcu.entity.getIndex());
        linkTrId = entity.getIndex();
    }

    public void enable(boolean enable)
    {
        if (enable)
        {
            entity.enableEntity();
        }
        else
        {
            entity.disableEntity();
        }
    }

    public void setOnMessages(int message, int takeoffIndex, int waypointIndex)
    {
        entity.setOnMessages(
                message,
                takeoffIndex,
                waypointIndex);
    }

    public void addEvent(McuEvent event)
    {
        entity.addEvent(event);
    }

    public void resetTarget(int unitLeaderLinkTrId)
    {
        entity.clearTargets();
        entity.setTarget(unitLeaderLinkTrId);
    }

    public McuTREntity getEntity()
    {
        return entity;
    }

    public CrewMember getCommander()
    {
        return null;
    }
    
    
}
