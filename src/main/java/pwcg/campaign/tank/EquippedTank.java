package pwcg.campaign.tank;

import java.util.Date;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.utils.DateUtils;
import pwcg.mission.ground.vehicle.Vehicle;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class EquippedTank extends Vehicle
{
    protected int serialNumber = SerialNumber.NO_SERIAL_NUMBER;
    protected int tankStatus = TankStatus.NO_STATUS;
    protected int companyId;
    protected Date dateRemovedFromService;
    protected String tankIdCode;
    protected boolean isEquipmentRequest = false;
    protected TankTypeInformation tankType;

    // Equipped tank for company
    public EquippedTank(VehicleDefinition vehicleDefinition, TankTypeInformation tankType, int serialNumber, Company company, int tankStatus)
    {
        super(vehicleDefinition, company.getCountry().getCountry());
        this.tankType = tankType;
        this.serialNumber = serialNumber;
        this.companyId = company.getCompanyId();
        this.tankStatus = tankStatus;
    }

    // Equipped tank for depot
    public EquippedTank(VehicleDefinition vehicleDefinition, TankTypeInformation tankType, int serialNumber, int tankStatus, Country country)
    {
        super(vehicleDefinition, country);
        this.tankType = tankType;
        this.serialNumber = serialNumber;
        this.companyId = Company.DEPOT;
        this.tankStatus = tankStatus;
    }

    // Player Tank from TankMcu
    public EquippedTank(EquippedTank equippedTank, Country country)
    {
        super(equippedTank.getVehicleDefinition(), country);
        this.copyFromTemplate(equippedTank);
    }

    // AI Tank from TankMcu
    public EquippedTank(VehicleDefinition vehicleDefinition, TankTypeInformation tankType, Country country)
    {
        super(vehicleDefinition, country);
        this.tankType = tankType;
        this.tankStatus = TankStatus.STATUS_DEPLOYED;
        this.serialNumber = SerialNumber.NO_SERIAL_NUMBER;
        this.companyId = Company.AI;
        try
        {
            this.dateRemovedFromService = DateUtils.getEndOfWar();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void copyFromTemplate(EquippedTank equippedTank)
    {
        super.copyFromTemplate(equippedTank);
        this.serialNumber = equippedTank.serialNumber;
        this.companyId = equippedTank.companyId;
        this.dateRemovedFromService = equippedTank.dateRemovedFromService;
        this.tankStatus = equippedTank.tankStatus;
        this.tankIdCode = equippedTank.tankIdCode;
        this.tankType = equippedTank.tankType;
    }

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber)
    {
        this.serialNumber = serialNumber;
    }
    
    public int getPlaneStatus()
    {
        return tankStatus;
    }

    public void setTankStatus(int planeStatus)
    {
        this.tankStatus = planeStatus;
    }

    public Date getDateRemovedFromService()
    {
        return dateRemovedFromService;
    }

    public void setDateRemovedFromService(Date dateRemovedFromService)
    {
        this.dateRemovedFromService = dateRemovedFromService;
    }

    public int getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(int companyId)
    {
        this.companyId = companyId;
    }

    public String getAircraftIdCode()
    {
        return tankIdCode;
    }

    public void setTankIdCode(String aircraftIdCode)
    {
        this.tankIdCode = aircraftIdCode;
    }

    public boolean isEquipmentRequest()
    {
        return isEquipmentRequest;
    }

    public void setEquipmentRequest(boolean isEquipmentRequest)
    {
        this.isEquipmentRequest = isEquipmentRequest;
    }

    public String getDisplayName()
    {
        return this.getName();
    }

    public PwcgRoleCategory determinePrimaryRoleCategory()
    {
        return tankType.determinePrimaryRoleCategory();
    }

    public String getArchType()
    {
        return tankType.getArchType();
    }

    public int getGoodness()
    {
        return tankType.getGoodness();
    }

    public Date getWithdrawal()
    {
        return tankType.getWithdrawal();
    }
}
