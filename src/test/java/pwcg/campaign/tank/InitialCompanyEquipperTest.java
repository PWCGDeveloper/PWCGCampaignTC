package pwcg.campaign.tank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.resupply.InitialCompanyEquipper;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankAttributeMapping;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CompanyTestProfile;

@ExtendWith(MockitoExtension.class)
public class InitialCompanyEquipperTest
{
    public InitialCompanyEquipperTest() throws PWCGException
    {
              
    }

    @Test
    public void testEquipCompanyGermanEast() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper companyEquipper = new InitialCompanyEquipper(campaign, company, equipmentWeightCalculator);
        Equipment equipment = companyEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedTank : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getArchType().equals("tiger"));
            assert(equippedTank.getType().equals(TankAttributeMapping.TIGER_I.getTankType()));
            assert(equippedTank.getSerialNumber() > SerialNumber.TANK_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipCompanyGermanWest() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.PANZER_LEHR_PROFILE);        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.PANZER_LEHR_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper companyEquipper = new InitialCompanyEquipper(campaign, company, equipmentWeightCalculator);
        Equipment equipment = companyEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedTank : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getArchType().equals("panther"));
            assert(equippedTank.getType().equals(TankAttributeMapping.PANTHER_D.getTankType()));
            assert(equippedTank.getSerialNumber() > SerialNumber.TANK_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipCompanyBritish() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.SEVENTH_DIVISION_PROFILE);        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.SEVENTH_DIVISION_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper companyEquipper = new InitialCompanyEquipper(campaign, company, equipmentWeightCalculator);
        Equipment equipment = companyEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedTank : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getArchType().equals("sherman"));
            assert(equippedTank.getType().equals(TankAttributeMapping.SHERMAN_M4A2.getTankType()));
            assert(equippedTank.getSerialNumber() > SerialNumber.TANK_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipCompanyAmerican() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.THIRD_DIVISION_PROFILE);        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.THIRD_DIVISION_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper companyEquipper = new InitialCompanyEquipper(campaign, company, equipmentWeightCalculator);
        Equipment equipment = companyEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedTank : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getArchType().equals("sherman"));
            assert(equippedTank.getType().equals(TankAttributeMapping.SHERMAN_M4A2.getTankType()));
            assert(equippedTank.getSerialNumber() > SerialNumber.TANK_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipCompanyRussian() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.TANK_DIVISION_147_PROFILE);        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper companyEquipper = new InitialCompanyEquipper(campaign, company, equipmentWeightCalculator);
        Equipment equipment = companyEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedTank : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getArchType().equals("il2"));
            assert(equippedTank.getType().equals("il2m41") || equippedTank.getType().equals("il2m42"));
            assert(equippedTank.getSerialNumber() > SerialNumber.TANK_STARTING_SERIAL_NUMBER);
        }
    }
}
