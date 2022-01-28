package pwcg.aar.outofmission.phase3.resupply;

import pwcg.aar.data.AARContext;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.equipment.EquipmentReplacementHandler;
import pwcg.campaign.resupply.equipment.EquipmentResupplyData;
import pwcg.campaign.resupply.equipment.EquipmentUpgradeHandler;
import pwcg.campaign.resupply.equipment.WithdrawnEquipmentReplacer;
import pwcg.campaign.resupply.personnel.CompanyTransferData;
import pwcg.campaign.resupply.personnel.TransferHandler;
import pwcg.campaign.tank.Equipment;
import pwcg.core.exception.PWCGException;
import pwcg.product.bos.country.TCServiceManager;

public class AARResupplyCoordinator
{
    private Campaign campaign;
    private AARContext aarContext;
    private AARResupplyData resupplyData = new AARResupplyData();

    public AARResupplyCoordinator(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    
    public AARResupplyData handleResupply() throws PWCGException 
    {
        handleAceTransfers();
        companyTransfers();
        equipmentResupply();
        return resupplyData;
    }

    private void handleAceTransfers() throws PWCGException
    {
        HistoricalAceTransferHandler aceTransferHandler = new HistoricalAceTransferHandler(campaign, aarContext.getNewDate());
        CompanyTransferData acesTransferred =  aceTransferHandler.determineAceTransfers();
        resupplyData.setAcesTransferred(acesTransferred);
    }
    
    private void companyTransfers() throws PWCGException
    {
        TCServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        for (ArmedService armedService : serviceManager.getAllActiveArmedServices(campaign.getDate()))
        {
            ResupplyNeedBuilder transferNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
            TransferHandler companyTransferHandler = new TransferHandler(campaign, transferNeedBuilder);
            CompanyTransferData companyTransferData = companyTransferHandler.determineCrewMemberTransfers(armedService);
            resupplyData.getCompanyTransferData().merge(companyTransferData);
        }
    }

    private void equipmentResupply() throws PWCGException
    {
        TCServiceManager serviceManager = ArmedServiceFactory.createServiceManager();
        for (ArmedService armedService : serviceManager.getAllActiveArmedServices(campaign.getDate()))
        {
            replaceWithdrawnTanks(armedService);
            replaceLostTanks(armedService);
            upgradeTanks(armedService);
        }
    }

    private void replaceWithdrawnTanks(ArmedService armedService) throws PWCGException
    {
        for (CompanyPersonnel companyPersonnel : campaign.getPersonnelManager().getAllCompanyPersonnel())
        {
            int serviceIdForCompany = companyPersonnel.getCompany().determineServiceForCompany(campaign.getDate()).getServiceId();
            if (armedService.getServiceId() == serviceIdForCompany)
            {
                Equipment equipment = campaign.getEquipmentManager().getEquipmentForCompany(companyPersonnel.getCompany().getCompanyId());
                WithdrawnEquipmentReplacer withdrawnEquipmentReplacer = new WithdrawnEquipmentReplacer(campaign, equipment, companyPersonnel.getCompany());
                withdrawnEquipmentReplacer.replaceWithdrawnEquipment();
            }
        }        
    }

    private void replaceLostTanks(ArmedService armedService) throws PWCGException
    {
        ResupplyNeedBuilder equipmentNeedBuilder = new ResupplyNeedBuilder(campaign, armedService);
        EquipmentReplacementHandler equipmentResupplyHandler = new EquipmentReplacementHandler(campaign, equipmentNeedBuilder);
        EquipmentResupplyData equipmentResupplyData = equipmentResupplyHandler.resupplyForLosses(armedService);
        resupplyData.getEquipmentResupplyData().merge(equipmentResupplyData);
    }
    
    private void upgradeTanks(ArmedService armedService) throws PWCGException
    {
        EquipmentUpgradeHandler equipmentUpgradeHandler = new EquipmentUpgradeHandler(campaign);
        EquipmentResupplyData equipmentResupplyData = equipmentUpgradeHandler.upgradeEquipment(armedService);
        resupplyData.getEquipmentResupplyData().merge(equipmentResupplyData);
    }
}
