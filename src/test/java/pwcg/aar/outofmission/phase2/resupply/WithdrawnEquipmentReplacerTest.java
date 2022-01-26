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
import pwcg.campaign.company.Company;
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
    @Mock
    private Campaign campaign;
    
    @Mock
    private Company company;
    
    private Equipment equipment = new Equipment();
    private SerialNumber serialNumber = new SerialNumber();
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {        
        
        Mockito.when(company.getCompanyId()).thenReturn(20111051);
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        equipment = new Equipment();
    }

    @Test
    public void testRemovalOfMe109F2() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420404");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        for (int i = 0; i < 6; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "bf109f2", company);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedTank);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "bf109f4", company);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedTank);
        }

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, company);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 6);
        assert(equipment.getActiveEquippedTanks().size() == 14);
        for (EquippedTank equippedTank: equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getType().equals("bf109f4") || equippedTank.getType().equals("bf109g2"));
        }
    }

    @Test
    public void testKeepingMe109F2() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420801");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        for (int i = 0; i < 6; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "bf109f2", company);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedTank);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "bf109f4", company);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedTank);
        }

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, company);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 0);
        assert(equipment.getActiveEquippedTanks().size() == 14);
        boolean me109F2Found = false;
        for (EquippedTank equippedTank: equipment.getActiveEquippedTanks().values())
        {
            if (equippedTank.getType().equals("bf109f2"))
            {
                me109F2Found = true;
            }
        }
        assert(me109F2Found);
    }

    @Test
    public void testReplaceOnlyMe109F2() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420404");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        for (int i = 0; i < 3; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "bf109f2", company);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedTank);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "bf109f4", company);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedTank);
        }

        assert(equipment.getActiveEquippedTanks().size() == 11);

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, company);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 3);
        assert(equipment.getActiveEquippedTanks().size() == 11);
        for (EquippedTank equippedTank: equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getType().equals("bf109f4") || equippedTank.getType().equals("bf109g2"));
        }

        for (EquippedTank equippedTank: equipment.getRecentlyInactiveEquippedTanks(campaign.getDate()).values())
        {
            assert(equippedTank.getType().equals("bf109f2"));
        }
    }

    @Test
    public void testKeepMe109F2BecauseItIsARequestEquipment() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420404");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        for (int i = 0; i < 3; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "bf109f2", company);
            equippedTank.setEquipmentRequest(true);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedTank);
        }
        
        for (int i = 0; i < 8; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "bf109f4", company);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedTank);
        }

        assert(equipment.getActiveEquippedTanks().size() == 11);

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, company);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 0);
        assert(equipment.getActiveEquippedTanks().size() == 11);
        
        int bf109F2Found = 0;
        for (EquippedTank equippedTank: equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getType().equals("bf109f2") || equippedTank.getType().equals("bf109f4") || equippedTank.getType().equals("bf109g2"));
            if (equippedTank.getType().equals("bf109f2"))
            {
                ++bf109F2Found;
            }
        }
        assert(bf109F2Found == 3);
    }

    @Test
    public void testAddExtraMe109F4() throws PWCGException
    {
        Date campaigndate = DateUtils.getDateYYYYMMDD("19420404");
        Mockito.when(campaign.getDate()).thenReturn(campaigndate);

        for (int i = 0; i < 3; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "bf109f2", company);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedTank);
        }
        
        for (int i = 0; i < 3; ++i)
        {
            EquippedTank equippedTank  = TankEquipmentFactory.makeTankForCompany(campaign, "bf109f4", company);
            equipment.addEquippedTankToCompany(campaign, 20111051, equippedTank);
        }

        WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, company);
        int numAdded = withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
        assert(numAdded == 7);
        assert(equipment.getActiveEquippedTanks().size() == 10);
        for (EquippedTank equippedTank: equipment.getActiveEquippedTanks().values())
        {
            assert(equippedTank.getType().equals("bf109f4") || equippedTank.getType().equals("bf109g2"));
        }
    }
}
