package pwcg.aar.outofmission.phase2.resupply;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignEquipmentManager;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.resupply.equipment.CompanyEquipmentNeed;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class CompanyEquipmentNeedTest
{
    @Mock private Campaign campaign;
    @Mock private Company company;
    @Mock private CampaignEquipmentManager campaignEquipmentManager;
    @Mock private Equipment equipment;
    @Mock private AARPersonnelLosses lossesInMissionData;
    @Mock private CompanyPersonnel companyPersonnel;
    @Mock private EquippedTank equippedTank;

    private Map<Integer, EquippedTank> activeEquippedPlaneCollection = new HashMap<>();
    private Map<Integer, EquippedTank> inactiveEquippedPlaneCollection = new HashMap<>();
    
    SerialNumber serialNumber = new SerialNumber();
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        activeEquippedPlaneCollection.clear();
        inactiveEquippedPlaneCollection.clear();
        
        
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420430"));
        Mockito.when(campaign.getEquipmentManager()).thenReturn(campaignEquipmentManager);
        Mockito.when(campaignEquipmentManager.getEquipmentForCompany(ArgumentMatchers.any())).thenReturn(equipment);

        Mockito.when(equipment.getActiveEquippedTanks()).thenReturn(activeEquippedPlaneCollection);
        Mockito.when(equipment.getRecentlyInactiveEquippedTanks(ArgumentMatchers.any())).thenReturn(inactiveEquippedPlaneCollection);
     }

    @Test
    public void testResupplyWithNoEquipment() throws PWCGException
    {
        CompanyEquipmentNeed companyTransferNeed = new CompanyEquipmentNeed(campaign, company);
        companyTransferNeed.determineResupplyNeeded();
        Assertions.assertTrue (companyTransferNeed.needsResupply() == true);
        
        for (int i = 0; i < Company.COMPANY_EQUIPMENT_SIZE - 1; ++i)
        {
            companyTransferNeed.noteResupply();
            Assertions.assertTrue (companyTransferNeed.needsResupply() == true);
        }

        companyTransferNeed.noteResupply();
        Assertions.assertTrue (companyTransferNeed.needsResupply() == false);
    }
    

    @Test
    public void testResupplyWithActiveEquipment() throws PWCGException
    {
        for (int i = 0; i < 11; ++i)
        {
            activeEquippedPlaneCollection.put(serialNumber.getNextPlaneSerialNumber(), equippedTank);
        }
        
        CompanyEquipmentNeed companyResupplyNeed = new CompanyEquipmentNeed(campaign, company);
        companyResupplyNeed.determineResupplyNeeded();
        Assertions.assertTrue (companyResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < 2; ++i)
        {
            companyResupplyNeed.noteResupply();
            Assertions.assertTrue (companyResupplyNeed.needsResupply() == true);
        }

        companyResupplyNeed.noteResupply();
        Assertions.assertTrue (companyResupplyNeed.needsResupply() == false);
    }
    

    @Test
    public void testResupplyWithActiveAndInactiveEquipment() throws PWCGException
    {
        for (int i = 0; i < 9; ++i)
        {
            activeEquippedPlaneCollection.put(serialNumber.getNextPlaneSerialNumber(), equippedTank);
        }
        
        for (int i = 0; i < 2; ++i)
        {
            inactiveEquippedPlaneCollection.put(serialNumber.getNextPlaneSerialNumber(), equippedTank);
        }

        CompanyEquipmentNeed companyResupplyNeed = new CompanyEquipmentNeed(campaign, company);
        companyResupplyNeed.determineResupplyNeeded();
        Assertions.assertTrue (companyResupplyNeed.needsResupply() == true);
        
        for (int i = 0; i < 2; ++i)
        {
            companyResupplyNeed.noteResupply();
            Assertions.assertTrue (companyResupplyNeed.needsResupply() == true);
        }

        companyResupplyNeed.noteResupply();
        Assertions.assertTrue (companyResupplyNeed.needsResupply() == false);
    }

    @Test
    public void testNoResupplyNeeded() throws PWCGException
    {
        for (int i = 0; i < 10; ++i)
        {
            activeEquippedPlaneCollection.put(serialNumber.getNextPlaneSerialNumber(), equippedTank);
        }
        
        for (int i = 0; i < 4; ++i)
        {
            inactiveEquippedPlaneCollection.put(serialNumber.getNextPlaneSerialNumber(), equippedTank);
        }

        CompanyEquipmentNeed companyResupplyNeed = new CompanyEquipmentNeed(campaign, company);
        companyResupplyNeed.determineResupplyNeeded();
        Assertions.assertTrue (companyResupplyNeed.needsResupply() == false);
    }

}
