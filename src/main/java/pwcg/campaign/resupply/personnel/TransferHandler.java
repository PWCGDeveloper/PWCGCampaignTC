package pwcg.campaign.resupply.personnel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.resupply.CompanyNeedFactory.CompanyNeedType;
import pwcg.campaign.resupply.ICompanyNeed;
import pwcg.campaign.resupply.ResupplyNeedBuilder;
import pwcg.campaign.resupply.ServiceResupplyNeed;
import pwcg.core.exception.PWCGException;

public class TransferHandler
{
    private Campaign campaign;
    private ResupplyNeedBuilder transferNeedBuilder;

    private CompanyTransferData companyTransferData = new CompanyTransferData();
    
    public TransferHandler(Campaign campaign, ResupplyNeedBuilder transferNeedBuilder)
    {
        this.campaign = campaign;
        this.transferNeedBuilder = transferNeedBuilder;
    }
    
    public CompanyTransferData determineCrewMemberTransfers(ArmedService armedService) throws PWCGException
    {
        ServiceResupplyNeed serviceTransferNeed = transferNeedBuilder.determineNeedForService(CompanyNeedType.PERSONNEL);
        PersonnelReplacementsService serviceReplacements =  campaign.getPersonnelManager().getPersonnelReplacementsService(armedService.getServiceId());
        replaceForService(serviceTransferNeed, serviceReplacements);
        return companyTransferData;
    }

    private void replaceForService(ServiceResupplyNeed serviceTransferNeed, PersonnelReplacementsService serviceReplacements) throws PWCGException
    {
        while (serviceTransferNeed.hasNeedyCompany())
        {
            ICompanyNeed selectedCompanyNeed = serviceTransferNeed.chooseNeedyCompany();
            if (serviceReplacements.hasReplacements())
            {
                CrewMember replacement = serviceReplacements.findReplacement();        
                TransferRecord transferRecord = new TransferRecord(replacement, Company.REPLACEMENT, selectedCompanyNeed.getCompanyId());
                companyTransferData.addTransferRecord(transferRecord);
                serviceTransferNeed.noteResupply(selectedCompanyNeed);
            }
            else
            {
                break;
            }
        }        
    }
}
