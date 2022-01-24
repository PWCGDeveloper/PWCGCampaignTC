package pwcg.campaign.tank.payload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.platoon.ITankPlatoon;

public abstract class TankPayload implements ITankPayload
{
    private TankPayloads payloads = new TankPayloads();
    private TankModifications modifications;
    private TankTypeInformation tankType;
    private Date date;

	public TankPayload(TankTypeInformation tankType, Date date)
	{
	    this.tankType = tankType;
        this.date = date;

        modifications = new TankModifications(tankType);
        
	    initialize();
	    createStandardWeaponsPayload();
	}

    @Override()
	public int createWeaponsPayload(ITankPlatoon unit) throws PWCGException
	{
	    int selectedPayloadId = 0;
	    return selectedPayloadId;
	}
	
    @Override
    public List<TankPayloadDesignation> getAvailablePayloadDesignations(ITankPlatoon unit) throws PWCGException
    {
        return new ArrayList<TankPayloadDesignation>();
    }

    @Override
    public TankPayloadDesignation getSelectedPayloadDesignation() throws PWCGException
    {
        return payloads.getSelectedPayloadDesignation();
    }

    protected List<TankPayloadDesignation> getAvailablePayloadDesignationsForTank(ITankPlatoon unit) throws PWCGException
    {
        return payloads.getPayloadDesignations().getAllAvailablePayloadDesignations();
    }

    protected List<TankPayloadDesignation> getAvailablePayloadDesignations(List<Integer> availablePayloadIds)
    {
        return payloads.getPayloadDesignations().getAvailablePayloadDesignations(availablePayloadIds);
    }

    protected ITankPayload copy(TankPayload target)
    {
        target.payloads = this.payloads.copy();
        target.modifications = this.modifications.copy();
        target.tankType = this.tankType;        
        target.date = this.date;        
        return target;
    }

    protected int getSelectedPayload()
    {
        return payloads.getSelectedPayloadId();
    }

    protected boolean isSelectedPayload(int payloadId)
    {
        return payloads.isSelectedPayload(payloadId);
    }

    protected void setSelectedPayload(int payloadId)
    {
        payloads.setSelectedPayloadId(payloadId);
    }

    @Override
    public int getPayloadIdByDescription(String payloadDescription)
    {
        return payloads.getPayloadDesignations().getPayloadIdByDescription(payloadDescription);
    }
    
    @Override
    public String getPayloadMaskByDescription(String payloadDescription)
    {
        return payloads.getPayloadDesignations().getPayloadMaskByDescription(payloadDescription);
    }

    @Override
    public void createStandardWeaponsPayload()
    {
        payloads.createStandardWeaponsPayload(0);
    }

    @Override
    public int getSelectedPayloadId() 
    {
        return payloads.getSelectedPayloadId();
    }
    
    @Override
    public void setSelectedPayloadId(int selectedPrimaryPayloadId) 
    {
        payloads.setSelectedPayloadId(selectedPrimaryPayloadId);
    }
 
    @Override
    public void selectModification(TankPayloadElement payloadElement)
    {
        modifications.selectModification(payloadElement);
    }

    @Override
    public List<TankPayloadElement> getSelectedModifications() throws PWCGException
    {
        return modifications.getSelectedModificationElements();
    }
    
    @Override
    public void clearModifications()
    {
        modifications.clearModifications();
    }
    
    @Override
    public List<TankPayloadDesignation> getOptionalPayloadModifications()
    {
        return modifications.getOptionalPayloadModifications();
    }

    @Override
    public String generateFullModificationMask() throws PWCGException
    {
        String fullModificationMask = payloads.getSelectedPayloadDesignation().getModMask();
        
        for (TankPayloadDesignation modificationDesignation : modifications.getSelectedModifications())
        {            
            String additionalModificationsMask = modificationDesignation.getModMask();
            int additionalModMaskValue = Integer.parseInt(additionalModificationsMask, 2);
            int fullModificationMaskValue = Integer.parseInt(fullModificationMask, 2);
            int newModmaskValue = fullModificationMaskValue + additionalModMaskValue;
            fullModificationMask = MathUtils.numberToBinaryForm(newModmaskValue);            
        }
        
        return fullModificationMask;
    }    

    @Override
    public TankPayloads getPayloads()
    {
        return payloads;
    }

    @Override
    public TankModifications getModifications()
    {
        return modifications;
    }

    protected void setAvailablePayload(int payloadId, String modMask, TankPayloadElement ... requestedPayloadElements)
    {
        if (payloadId >= 0)
        {
            payloads.addPayload(payloadId, modMask, requestedPayloadElements);
        }
        else
        {
            modifications.addModification(payloadId, modMask, requestedPayloadElements);
        }
    }

    protected TankTypeInformation getTankType()
    {
        return tankType;
    }

    protected Date getDate()
    {
        return date;
    }

    protected abstract int createWeaponsPayloadForTank(ITankPlatoon unit) throws PWCGException;
    protected abstract void initialize();
}
