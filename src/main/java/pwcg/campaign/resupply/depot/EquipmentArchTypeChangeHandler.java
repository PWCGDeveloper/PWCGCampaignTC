package pwcg.campaign.resupply.depot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;

public class EquipmentArchTypeChangeHandler 
{
    private Campaign campaign;
    private Date newDate;
    private Set<Company> companysToEquip = new HashSet<>();
	
	public EquipmentArchTypeChangeHandler(Campaign campaign, Date newDate)
	{
        this.campaign = campaign;
        this.newDate = newDate;
	}
	
	public void updateCampaignEquipmentForArchtypeChange () throws PWCGException
	{
        removeOutdatedArchTypes();
        addNewArchTypes();
	}

    private void removeOutdatedArchTypes() throws PWCGException
    {
        CompanyManager companyManager = PWCGContext.getInstance().getCompanyManager();
        for (Company company : companyManager.getActiveCompanies(campaign.getDate()))
        {
            Equipment companyEquipment = campaign.getEquipmentManager().getEquipmentForCompany(company.getCompanyId());
            if (companyEquipment != null)
            {
                Set<Integer> planesToRemove = new HashSet<>();
                for (EquippedTank plane : companyEquipment.getActiveEquippedTanks().values())
                {
                    boolean isActiveArchType = company.isPlaneInActiveCompanyArchTypes(newDate, plane);
                    if (!isActiveArchType)
                    {
                        planesToRemove.add(plane.getSerialNumber());
                        companysToEquip.add(company);
                    }
                }

                for (Integer tankSerialNumber : planesToRemove)
                {
                    companyEquipment.deactivateEquippedTankFromCompany(tankSerialNumber, campaign.getDate());
                }
            }            
        }
    }

    private void addNewArchTypes() throws PWCGException
    {
        for (Company company : companysToEquip)
        {
            Equipment companyEquipment = campaign.getEquipmentManager().getEquipmentForCompany(company.getCompanyId());
            int numPlanesNeeded = Company.COMPANY_EQUIPMENT_SIZE - companyEquipment.getActiveEquippedTanks().size();

            TankTypeInformation bestTankType = getBestTankTypeForCompany(company);
            for (int i = 0; i < numPlanesNeeded; ++i)
            {
                EquippedTank replacementPlane = TankEquipmentFactory.makeTankForCompany(campaign, bestTankType.getType(), company.getCompanyId());
                companyEquipment.addEquippedTankToCompany(campaign, company.getCompanyId(), replacementPlane);
            }
        }
    }
    
    private TankTypeInformation getBestTankTypeForCompany(Company company) throws PWCGException
    {
        List<TankTypeInformation> planeTypesForCompany = new ArrayList<>();
        for (TankArchType archType : company.determineCurrentAircraftArchTypes(newDate))
        {
            List<TankTypeInformation> planeTypesForArchType = archType.getActiveMemberTankTypes(newDate);
            planeTypesForCompany.addAll(planeTypesForArchType);
        }
        
        TankTypeInformation bestTankType = planeTypesForCompany.get(0);
        for (TankTypeInformation planeType : planeTypesForCompany)
        {
            if (planeType.getGoodness() > bestTankType.getGoodness())
            {
                bestTankType = planeType;
            }
        }
        
        return bestTankType;
    }
}
