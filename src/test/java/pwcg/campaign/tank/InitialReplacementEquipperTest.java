package pwcg.campaign.tank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.resupply.depot.EquipmentDepotInitializer;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.product.bos.country.TCServiceManager;

@ExtendWith(MockitoExtension.class)
public class InitialReplacementEquipperTest
{
    @Mock private Campaign campaign;
    
    private SerialNumber serialNumber = new SerialNumber();
    
    public InitialReplacementEquipperTest() throws PWCGException
    {
        
    }

    @Test
    public void testEquipGermanReplacementsEarly() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19411101"));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        Mockito.when(campaign.getCampaignMap()).thenReturn(FrontMapIdentifier.MOSCOW_MAP);
        PWCGContext.getInstance().setCampaign(campaign);
        
        TCServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        ArmedService luftwaffe = serviceManager.getArmedService(TCServiceManager.WEHRMACHT);
        
        EquipmentDepotInitializer replacementEquipper = new EquipmentDepotInitializer(campaign, luftwaffe);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        assert(equipment.getAvailableDepotTanks().size() == 5);
        
        boolean pziiilFound = false;
        boolean pziiimFound = false;
        boolean pzivgFound = false;
        boolean pantherFound = false;
        boolean tigerFound = false;
        
        for (EquippedTank replacementTank : equipment.getAvailableDepotTanks().values())
        {
            if (replacementTank.getType().equals(TankAttributeMapping.PZKW_III_L.getTankType()))
            {
                pziiilFound = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.PZKW_III_M.getTankType()))
            {
                pziiimFound = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.PZKW_IV_G.getTankType()))
            {
                pzivgFound = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.PANTHER_D.getTankType()))
            {
                pantherFound = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.TIGER_I.getTankType()))
            {
                tigerFound = true;
            }
        }
        
        assert(pziiilFound || pziiimFound);
        assert(!pzivgFound);
        assert(!pantherFound);
        assert(!tigerFound);
    }


    @Test
    public void testEquipGermanReplacementsMid() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430901"));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        Mockito.when(campaign.getCampaignMap()).thenReturn(FrontMapIdentifier.KUBAN_MAP);
        PWCGContext.getInstance().setCampaign(campaign);

        TCServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        ArmedService luftwaffe = serviceManager.getArmedService(TCServiceManager.WEHRMACHT);
        
        EquipmentDepotInitializer replacementEquipper = new EquipmentDepotInitializer(campaign, luftwaffe);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        assert(equipment.getAvailableDepotTanks().size() == 6);
        
        boolean pziiilFound = false;
        boolean pziiimFound = false;
        boolean pzivgFound = false;
        boolean pantherFound = false;
        boolean tigerFound = false;
        
        for (EquippedTank replacementTank : equipment.getAvailableDepotTanks().values())
        {
            if (replacementTank.getType().equals(TankAttributeMapping.PZKW_III_L.getTankType()))
            {
                pziiilFound = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.PZKW_III_M.getTankType()))
            {
                pziiimFound = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.PZKW_IV_G.getTankType()))
            {
                pzivgFound = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.PANTHER_D.getTankType()))
            {
                pantherFound = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.TIGER_I.getTankType()))
            {
                tigerFound = true;
            }
        }
        
        assert(!pziiilFound);
        assert(!pziiimFound);
        assert(pzivgFound);
        assert(pantherFound);
        assert(tigerFound);
    }

    @Test
    public void testEquipRussianReplacementsEarly() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19411101"));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        Mockito.when(campaign.getCampaignMap()).thenReturn(FrontMapIdentifier.MOSCOW_MAP);
        PWCGContext.getInstance().setCampaign(campaign);
        
        TCServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        ArmedService luftwaffe = serviceManager.getArmedService(TCServiceManager.SSV);
        
        EquipmentDepotInitializer replacementEquipper = new EquipmentDepotInitializer(campaign, luftwaffe);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        assert(equipment.getAvailableDepotTanks().size() == 5);
        
        boolean t34EarlyFound = false;
        boolean t34LateFound = false;
        boolean kv1Found = false;
        boolean su122Found = false;
        boolean su152Found = false;
        
        for (EquippedTank replacementTank : equipment.getAvailableDepotTanks().values())
        {
            if (replacementTank.getType().equals(TankAttributeMapping.T34_EARLY.getTankType()))
            {
                t34EarlyFound = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.T34_LATE.getTankType()))
            {
                t34LateFound = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.KV1_S.getTankType()))
            {
                kv1Found = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.SU122.getTankType()))
            {
                su122Found = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.SU152.getTankType()))
            {
                su152Found = true;
            }
        }
        
        assert(t34EarlyFound);
        assert(kv1Found);
        assert(!t34LateFound);
        assert(!su122Found);
        assert(!su152Found);
    }

    @Test
    public void testEquipRussianReplacementsMid() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430901"));
        Mockito.when(campaign.getSerialNumber()).thenReturn(serialNumber);
        Mockito.when(campaign.getCampaignMap()).thenReturn(FrontMapIdentifier.KUBAN_MAP);
        PWCGContext.getInstance().setCampaign(campaign);
        
        TCServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        ArmedService luftwaffe = serviceManager.getArmedService(TCServiceManager.SSV);
        
        EquipmentDepotInitializer replacementEquipper = new EquipmentDepotInitializer(campaign, luftwaffe);
        Equipment equipment = replacementEquipper.createReplacementPoolForService();
        assert(equipment.getAvailableDepotTanks().size() == 14);
        
        boolean t34EarlyFound = false;
        boolean t34LateFound = false;
        boolean kv1Found = false;
        boolean su122Found = false;
        boolean su152Found = false;
        
        for (EquippedTank replacementTank : equipment.getAvailableDepotTanks().values())
        {
            if (replacementTank.getType().equals(TankAttributeMapping.T34_EARLY.getTankType()))
            {
                t34EarlyFound = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.T34_LATE.getTankType()))
            {
                t34LateFound = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.KV1_S.getTankType()))
            {
                kv1Found = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.SU122.getTankType()))
            {
                su122Found = true;
            }
            else if (replacementTank.getType().equals(TankAttributeMapping.SU152.getTankType()))
            {
                su152Found = true;
            }
        }
        
        assert(t34EarlyFound || t34LateFound);
        assert(kv1Found);
        assert(su122Found || su152Found);
    }
}
