package pwcg.campaign.plane;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.resupply.InitialCompanyEquipper;
import pwcg.campaign.resupply.depot.EquipmentWeightCalculator;
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
    public void testEquipCompanyGermanFighter() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper companyEquipper = new InitialCompanyEquipper(campaign, company, equipmentWeightCalculator);
        Equipment equipment = companyEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedPlane : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getArchType().equals("bf109"));
            assert(equippedPlane.getType().equals("bf109f2") || equippedPlane.getType().equals("bf109f4"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipCompanyGermanBomber() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE);        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper companyEquipper = new InitialCompanyEquipper(campaign, company, equipmentWeightCalculator);
        Equipment equipment = companyEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedPlane : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getArchType().equals("he111"));
            assert(equippedPlane.getType().equals("he111h6") || equippedPlane.getType().equals("he111h16"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipCompanyGermanDiveBomber() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.STG77_PROFILE);        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.STG77_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper companyEquipper = new InitialCompanyEquipper(campaign, company, equipmentWeightCalculator);
        Equipment equipment = companyEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedPlane : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getArchType().equals("ju87"));
            assert(equippedPlane.getType().equals("ju87d3"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipCompanyGermanTransport() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.TG2_PROFILE);        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.TG2_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper companyEquipper = new InitialCompanyEquipper(campaign, company, equipmentWeightCalculator);
        Equipment equipment = companyEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedPlane : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getArchType().equals("ju52"));
            assert(equippedPlane.getType().equals("ju523mg4e"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }

    @Test
    public void testEquipCompanyRussianAttack() throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(CompanyTestProfile.TANK_DIVISION_147_PROFILE);        
        Company company = PWCGContext.getInstance().getCompanyManager().getCompany(CompanyTestProfile.TANK_DIVISION_147_PROFILE.getCompanyId());
        
        EquipmentWeightCalculator equipmentWeightCalculator = new EquipmentWeightCalculator(campaign.getDate());
        InitialCompanyEquipper companyEquipper = new InitialCompanyEquipper(campaign, company, equipmentWeightCalculator);
        Equipment equipment = companyEquipper.generateEquipment();
        
        assert(equipment.getActiveEquippedTanks().size() == Company.COMPANY_EQUIPMENT_SIZE);
        for (EquippedTank equippedPlane : equipment.getActiveEquippedTanks().values())
        {
            assert(equippedPlane.getArchType().equals("il2"));
            assert(equippedPlane.getType().equals("il2m41") || equippedPlane.getType().equals("il2m42"));
            assert(equippedPlane.getSerialNumber() > SerialNumber.PLANE_STARTING_SERIAL_NUMBER);
        }
    }
}
