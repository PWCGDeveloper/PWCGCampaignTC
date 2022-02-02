package pwcg.mission.platoon.tank;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.campaign.tank.payload.ITankPayload;
import pwcg.campaign.tank.payload.TankPayloadFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogCategory;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.mcu.McuEvent;
import pwcg.mission.mcu.McuTREntity;
import pwcg.mission.platoon.ITankPlatoon;
import pwcg.product.bos.config.TCProductSpecificConfiguration;

public class TankMcu extends EquippedTank implements Cloneable, IVehicle
{
    private Skin skin = null;
    private int coopStart = 0;
    private Callsign callsign = Callsign.NONE;
    private int callnum = 0;
    private double fuel = 1.0;

    private ITankPayload payload = null;

    private CrewMember tankCommander = null;

    public TankMcu(EquippedTank equippedTank, ICountry country, CrewMember crewMember) throws PWCGException
    {
        super(equippedTank, country.getCountry());
        this.tankCommander = crewMember;
        
        buildPlayerTank(equippedTank, country);
    }

    public TankMcu(VehicleDefinition vehicleDefinition, TankTypeInformation tankType, ICountry country) throws PWCGException
    {
        super(vehicleDefinition, tankType, country.getCountry());
        this.tankCommander = null;
        
        buildAiTank(tankType, country);
    }
    
    private void buildPlayerTank(EquippedTank equippedTank, ICountry country) throws PWCGException
    {
        equippedTank.copyFromTemplate(this);
        this.setVehicleName(tankCommander.getNameAndRank());
        this.setDescription(tankCommander.getNameAndRank());
        this.setCountry(country);
        setDeleteAfterDeath();
    }
    
    private void buildAiTank(TankTypeInformation tankType, ICountry country) throws PWCGException
    {
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

    public void populateEntity(TankMcu unitLeader)
    {
        if (unitLeader.getIndex() != index)
        {
            entity.setTarget(unitLeader.getLinkTrId());
        }
    }

    public boolean isActivePlayerTank(Campaign campaign) throws PWCGException
    {
        boolean isPlayerTank = false;
        if (aiLevel == AiSkillLevel.PLAYER)
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
    
    public ITankPayload buildTankPayload(ITankPlatoon unit, Date date) throws PWCGException
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

            super.writeInternals(writer);

            writer.write("  CoopStart = " + coopStart + ";");
            writer.newLine();
            writer.write("  PayloadId = " + payload.getSelectedPayloadId() + ";");
            writer.newLine();
            writer.write("  WMMask = " + payload.generateFullModificationMask() + ";");
            writer.newLine();
            writer.write("  Fuel = " + fuel + ";");
            writer.newLine();

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

            // BoS specific parameters
            TCProductSpecificConfiguration productSpecificConfiguration =new TCProductSpecificConfiguration();
            if (productSpecificConfiguration.useCallSign())
            {
                writer.write("  Callsign = " + callsign.getNum(country) + ";");
                writer.newLine();
                writer.write("  Callnum = " + callnum + ";");
                writer.newLine();
            }


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

    public int getCoopStart()
    {
        return coopStart;
    }

    public void setCoopStart(int coopStart)
    {
        this.coopStart = coopStart;
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

    public CrewMember getTankCommander()
    {
        return tankCommander;
    }

    public void replaceCrewMember(CrewMember newCrewMember)
    {
        this.tankCommander = newCrewMember;
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

    public int getCruisingSpeed()
    {
        if (tankType != null)
        {
            return tankType.getCruisingSpeed();
        }
        else
        {
            return 10;
        }
    }

    public ArrayList<PwcgRoleCategory> getRoleCategories()
    {
        return tankType.getRoleCategories();
    }

    public boolean isPlayerTank()
    {
        if (tankCommander != null)
        {
            if (tankCommander.isPlayer())
            {
                return true;
            }
        }
        return false;
    }
}
