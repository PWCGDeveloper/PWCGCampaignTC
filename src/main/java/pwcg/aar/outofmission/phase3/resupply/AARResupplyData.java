package pwcg.aar.outofmission.phase3.resupply;

import pwcg.campaign.resupply.equipment.EquipmentResupplyData;
import pwcg.campaign.resupply.personnel.CompanyTransferData;

public class AARResupplyData
{
    private CompanyTransferData acesTransferred = new CompanyTransferData();
    private CompanyTransferData companyTransferData = new CompanyTransferData();
    private EquipmentResupplyData equipmentResupplyData = new EquipmentResupplyData();

    public CompanyTransferData getAcesTransferred()
    {
        return acesTransferred;
    }

    public void setAcesTransferred(CompanyTransferData acesTransferred)
    {
        this.acesTransferred = acesTransferred;
    }

    public CompanyTransferData getCompanyTransferData()
    {
        return companyTransferData;
    }

    public void setCompanyTransferData(CompanyTransferData companyTransferData)
    {
        this.companyTransferData = companyTransferData;
    }

    public void merge(AARResupplyData transferData)
    {
        acesTransferred.merge(transferData.getAcesTransferred());
        companyTransferData.merge(transferData.getCompanyTransferData());        
        equipmentResupplyData.merge(transferData.getEquipmentResupplyData());        
    }

    public void setEquipmentResupplyData(EquipmentResupplyData equipmentResupplyData)
    {
        this.equipmentResupplyData = equipmentResupplyData;        
    }

    public EquipmentResupplyData getEquipmentResupplyData()
    {
        return equipmentResupplyData;
    }
}
