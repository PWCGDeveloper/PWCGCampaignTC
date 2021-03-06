package pwcg.mission.ground.vehicle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.mcu.McuTREntity;

public class Vehicle implements Cloneable, IVehicle
{
    protected VehicleDefinition vehicleDefinition;
    protected String vehicleName = "";
    protected String vehicleType = "";
    protected int index;
    protected int linkTrId;
    protected Coordinate position;
    protected Orientation orientation;
    protected String script = "";
    protected String model = "";
    protected String Desc = "";
    protected int numberInFormation = 1;
    protected int vulnerable = 1;
    protected int engageable = 1;
    protected int limitAmmo = 1;
    protected AiSkillLevel aiLevel = AiSkillLevel.NOVICE;
    protected int damageReport = 50;
    protected int damageThreshold = 1;
    protected int deleteAfterDeath = 0;
    protected int spotter = -1;
    protected int beaconChannel = 0;
    protected Country country = Country.NEUTRAL;

    protected McuTREntity entity;

    public Vehicle(VehicleDefinition vehicleDefinition, Country country)
    {
        index = IndexGenerator.getInstance().getNextIndex();
        entity = new McuTREntity(index);
        linkTrId = entity.getIndex();

        this.vehicleDefinition = vehicleDefinition;
        this.makeVehicleFromDefinition(country);
    }

    private void makeVehicleFromDefinition(Country country)
    {
        this.country = country;
        this.vehicleType = vehicleDefinition.getVehicleType();
        this.vehicleName = vehicleDefinition.getVehicleName();
        this.script = "LuaScripts\\WorldObjects\\" + vehicleDefinition.getScriptDir() + vehicleDefinition.getVehicleType() + ".txt";
        this.model = "graphics\\" + vehicleDefinition.getModelDir() + vehicleDefinition.getVehicleType() + ".mgm";
        setPosition(new Coordinate());
        setOrientation(new Orientation());
        populateEntity();
    }

    @Override
    public void populateEntity()
    {
        entity.setPosition(position);
        entity.setOrientation(orientation);
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("Vehicle");
            writer.newLine();
            writer.write("{");
            writer.newLine();

            writeInternals(writer);

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

    protected void writeInternals(BufferedWriter writer) throws PWCGException
    {
        try
        {
            writer.write("  Name = \"" + vehicleName + "\";");
            writer.newLine();
            writer.write("  Desc = \"" + Desc + "\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  LinkTrId = " + linkTrId + ";");
            writer.newLine();
            writer.write("  Script = \"" + script + "\";");
            writer.newLine();
            writer.write("  Model = \"" + model + "\";");
            writer.newLine();

            this.getCountry().writeAdjusted(writer);

            position.write(writer);
            orientation.write(writer);


            writer.write("  NumberInFormation = " + numberInFormation + ";");
            writer.newLine();
            writer.write("  Engageable = " + engageable + ";");
            writer.newLine();
            writer.write("  LimitAmmo = " + limitAmmo + ";");
            writer.newLine();
            writer.write("  AILevel = " + aiLevel.getAiSkillLevel() + ";");
            writer.newLine();
            writer.write("  Vulnerable = " + vulnerable + ";");
            writer.newLine();
            writer.write("  DamageReport = " + damageReport + ";");
            writer.newLine();
            writer.write("  DamageThreshold = " + damageThreshold + ";");
            writer.newLine();
            writer.write("  DeleteAfterDeath = " + deleteAfterDeath + ";");
            writer.newLine();
            writer.write("  Spotter = " + spotter + ";");
            writer.newLine();
            writer.write("  BeaconChannel = " + beaconChannel + ";");
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    public boolean vehicleExists()
    {
        String scriptFilename = "..\\data\\" + script;
        File scriptFile = new File(scriptFilename);

        String modelFilename = "..\\data\\" + model;
        File modelFile = new File(modelFilename);

        if (scriptFile.exists() && modelFile.exists())

        {
            return true;
        }

        return false;
    }

    @Override
    public Coordinate getPosition()
    {
        return position;
    }

    @Override
    public void setPosition(Coordinate position)
    {
        this.position = position.copy();
        entity.setPosition(position);
    }

    @Override
    public Orientation getOrientation()
    {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
        entity.setOrientation(orientation);
    }

    @Override
    public String getDescription()
    {
        return (vehicleType + " / " + script + " / " + model);
    }

    @Override
    public McuTREntity getEntity()
    {
        return entity;
    }

    public int getEngageable()
    {
        return engageable;
    }

    @Override
    public void setEngageable(int engageable)
    {
        this.engageable = engageable;
    }

    public int getNumberInFormation()
    {
        return numberInFormation;
    }

    public void setNumberInFormation(int numberInFormation)
    {
        this.numberInFormation = numberInFormation;
    }

    @Override
    public void setAiLevel(AiSkillLevel aiLevel)
    {
        this.aiLevel = aiLevel;
    }

    @Override
    public void setCountry(ICountry country)
    {
        this.country = country.getCountry();
    }

    @Override
    public ICountry getCountry()
    {
        return CountryFactory.makeCountryByCountry(country);
    }

    @Override
    public String getName()
    {
        return vehicleName;
    }

    @Override
    public String getScript()
    {
        return script;
    }

    @Override
    public int getIndex()
    {
        return index;
    }

    @Override
    public String getType()
    {
        return vehicleType;
    }

    @Override
    public int getLinkTrId()
    {
        return linkTrId;
    }

    public void setVehicleName(String vehicleName)
    {
        this.vehicleName = vehicleName;
    }

    public void setDescription(String desc)
    {
        Desc = desc;
    }

    public VehicleDefinition getVehicleDefinition()
    {
        return vehicleDefinition;
    }


    public void setLinkTrId(int linkTrId)
    {
        this.linkTrId = linkTrId;
    }
}
