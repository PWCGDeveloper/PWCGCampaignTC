package pwcg.campaign.crewmember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class CrewMembers
{
    private Map<Integer, CrewMember> companyMemberCollection = new ConcurrentHashMap<>();

    public Map<Integer, CrewMember> getCrewMemberCollection()
    {
        return companyMemberCollection;
    }

    public List<CrewMember> getCrewMemberList()
    {
        return new ArrayList<>(companyMemberCollection.values());
    }

    public void setCrewMemberCollection(Map<Integer, CrewMember> crewMembers)
    {
        this.companyMemberCollection = crewMembers;
    }

    public void addToCrewMemberCollection(CrewMember crewMember)
    {
        companyMemberCollection.put(crewMember.getSerialNumber(), crewMember);
    }

    public void addCrewMembers(CrewMembers newCrewMembers)
    {
        companyMemberCollection.putAll(newCrewMembers.companyMemberCollection);
    }

    public boolean isCrewMember(Integer serialNumber)
    {
        return companyMemberCollection.containsKey(serialNumber);
    }

    public CrewMember findCrewMember() throws PWCGException
    {
        List<Integer> serialNumbers = new ArrayList<>(companyMemberCollection.keySet());
        int index = RandomNumberGenerator.getRandom(serialNumbers.size());
        int serialNumber = serialNumbers.get(index);
        CrewMember crewMember = companyMemberCollection.get(serialNumber);
        return crewMember;
    }

    public CrewMember removeCrewMember(int serialNumber) throws PWCGException
    {
        CrewMember crewMember = companyMemberCollection.remove(serialNumber);
        return crewMember;
    }

    public int getActiveCount(Date date) throws PWCGException
    {
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAcesNoWounded(companyMemberCollection, date);
        return activeCrewMembers.getCrewMemberList().size();
    }
    
    public CrewMember getCrewMember(int serialNumber)
    {
        CrewMember crewMember = companyMemberCollection.get(serialNumber);
        return crewMember;
    }
    
    public void clear()
    {
        companyMemberCollection.clear();
    }

    public CrewMember getCrewMemberByName(String name) throws PWCGException
    {
        for (CrewMember crewMember : companyMemberCollection.values())
        {
            if (crewMember.getName().equals(name))
            {
                return crewMember;
            }
        }
        
        throw new PWCGException("No companymember found for name " + name);
    }
    
    public List<CrewMember> sortCrewMembers(Date date) throws PWCGException 
    {
        Map<String, CrewMember> sortedCrewMembers = new TreeMap<>();
        for (CrewMember crewMember : companyMemberCollection.values())
        {            
            IRankHelper rankObj = RankFactory.createRankHelper();
            int rankPos = rankObj.getRankPosByService(crewMember.getRank(), crewMember.determineService(date));
            String keyVal = new String("" + rankPos + crewMember.getName());
            sortedCrewMembers.put(keyVal, crewMember);
        }
        
        return new ArrayList<>(sortedCrewMembers.values());
    }
}
