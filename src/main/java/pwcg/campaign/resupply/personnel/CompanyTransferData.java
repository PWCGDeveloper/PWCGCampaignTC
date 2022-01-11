package pwcg.campaign.resupply.personnel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyTransferData
{
    private Map<Integer, List<TransferRecord>> crewMembersTransferred = new HashMap<>();
    
    public void addTransferRecord(TransferRecord transferRecord)
    {
        int serialNumber = transferRecord.getCrewMember().getSerialNumber();
        if (!crewMembersTransferred.containsKey(serialNumber))
        {
            List<TransferRecord> newTransfeRecordSet = new ArrayList<>();
            crewMembersTransferred.put(serialNumber, newTransfeRecordSet);
        }
        
        List<TransferRecord> transfeRecordSet = crewMembersTransferred.get(serialNumber);
        transfeRecordSet.add(transferRecord);
    }
    
    public void merge(CompanyTransferData source)
    {
        for (TransferRecord transferRecord : source.getCrewMembersTransferred())
        {
            addTransferRecord(transferRecord);
        }
    }
    
    public int getTransferCount()
    {
        return crewMembersTransferred.size();
    }
    
    public List<TransferRecord> getCrewMembersTransferred()
    {
        List<TransferRecord> allTransferrecords = new ArrayList<>();
        for (List<TransferRecord> transferRecordsForCrewMember : crewMembersTransferred.values())
        {
            for (TransferRecord transferRecord : transferRecordsForCrewMember)
            {
                allTransferrecords.add(transferRecord);
            }
        }

        return allTransferrecords;
    }
}
