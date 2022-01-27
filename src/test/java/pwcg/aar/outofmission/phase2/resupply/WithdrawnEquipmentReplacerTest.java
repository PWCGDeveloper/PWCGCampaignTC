package pwcg.aar.outofmission.phase2.resupply;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.resupply.equipment.WithdrawnEquipmentReplacer;
import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankEquipmentFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WithdrawnEquipmentReplacerTest
{
    @Mock private Campaign campaign;
    @Mock private Company company;
    @Mock private ICountry country;
    
    private Equipment equipment = new Equipment();
    private SerialNumber serialNumber = new SerialNumber();
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        
        Mockito.when(company.getCompanyId()).thenReturn(CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId());
        Mockito.when(company.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.GERMANY);
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        equipment = new Equipment();
    }

    @Test
    public void testRemovalOfPZIIIL() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19440201");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        for (int i = 0; i < 6; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "_pziii-l", company);
            equipment.addEquippedTankToCompany(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), equippedTank);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "_pziv-g", company);
            equipment.addEquippedTankToCompany(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), equippedTank);
        }

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, company);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 6);
        assert(equipment.getActiveEquippedTanks().size() == 14);
        for (EquippedTank equippedTank: equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getType().equals("_pziv-g") || equippedTank.getType().equals("_pzv-d"));
        }
    }

    @Test
    public void testKeepingPzIVG() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19440201");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        for (int i = 0; i < 6; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "_pziii-l", company);
            equipment.addEquippedTankToCompany(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), equippedTank);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "_pziv-g", company);
            equipment.addEquippedTankToCompany(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), equippedTank);
        }

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, company);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 6);
        assert(equipment.getActiveEquippedTanks().size() == 14);
        boolean pzivgFound = false;
        for (EquippedTank equippedTank: equipment.getActiveEquippedTanks().values())
        {
            if (equippedTank.getType().equals("_pziv-g"))
            {
                pzivgFound = true;
            }
        }
        assert(pzivgFound);
    }

    @Test
    public void testReplaceOnlyPzIIIL() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19440201");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        for (int i = 0; i < 3; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "_pziii-l", company);
            equipment.addEquippedTankToCompany(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), equippedTank);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "_pziv-g", company);
            equipment.addEquippedTankToCompany(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), equippedTank);
        }

        assert(equipment.getActiveEquippedTanks().size() == 11);

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, company);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 3);
        assert(equipment.getActiveEquippedTanks().size() == 11);
        for (EquippedTank equippedTank: equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getType().equals("_pziv-g") || equippedTank.getType().equals("_pzv-d"));
        }

        for (EquippedTank equippedTank: equipment.getRecentlyInactiveEquippedTanks(campaign.getDate()).values())
        {
            assert(equippedTank.getType().equals("_pziii-l"));
        }
    }

    @Test
    public void testKeepPZIIILBecauseItIsARequestEquipment() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19440201");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        for (int i = 0; i < 3; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "_pziii-l", company);
            equippedTank.setEquipmentRequest(true);
            equipment.addEquippedTankToCompany(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), equippedTank);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "_pziv-g", company);
            equipment.addEquippedTankToCompany(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), equippedTank);
        }

        assert(equipment.getActiveEquippedTanks().size() == 11);

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, company);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 0);
        assert(equipment.getActiveEquippedTanks().size() == 11);
        
        int bf109F2Found = 0;
        for (EquippedTank equippedTank: equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getType().equals("_pziii-l") || equippedTank.getType().equals("_pziv-g") || equippedTank.getType().equals("_pzv-d"));
            if (equippedTank.getType().equals("_pziii-l"))
            {
                ++bf109F2Found;
            }
        }
        assert(bf109F2Found == 3);
    }

    @Test
    public void testAddExtraPzIVG() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19440201");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        for (int i = 0; i < 3; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "_pziii-l", company);
            equipment.addEquippedTankToCompany(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), equippedTank);
        }
        
        for (int i = 0; i < 3; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "_pziv-g", company);
            equipment.addEquippedTankToCompany(campaign, CompanyTestProfile.GROSS_DEUTSCHLAND_PROFILE.getCompanyId(), equippedTank);
        }

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, company);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 7);
        assert(equipment.getActiveEquippedTanks().size() == 10);
        for (EquippedTank equippedTank: equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getType().equals("_pziv-g") || equippedTank.getType().equals("_pzv-d"));
        }
    }
}
