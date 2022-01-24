package pwcg.campaign.tank.payload;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.ITankTypeFactory;
import pwcg.campaign.tank.TankAttributeFactory;
import pwcg.campaign.tank.TankAttributeMapping;
import pwcg.campaign.tank.TankTypeInformation;
import pwcg.core.exception.PWCGException;
import pwcg.mission.playerunit.payload.tank.DefaultPayload;
import pwcg.mission.playerunit.payload.tank.GAZAAAPayload;
import pwcg.mission.playerunit.payload.tank.KV1SPayload;
import pwcg.mission.playerunit.payload.tank.M4A2Payload;
import pwcg.mission.playerunit.payload.tank.PantherDPayload;
import pwcg.mission.playerunit.payload.tank.PzkwIIILPayload;
import pwcg.mission.playerunit.payload.tank.PzkwIIIMPayload;
import pwcg.mission.playerunit.payload.tank.PzkwIVGPayload;
import pwcg.mission.playerunit.payload.tank.Sdkfz10Payload;
import pwcg.mission.playerunit.payload.tank.Sdkfz184Payload;
import pwcg.mission.playerunit.payload.tank.Su122Payload;
import pwcg.mission.playerunit.payload.tank.Su152payload;
import pwcg.mission.playerunit.payload.tank.T34EarlyPayload;
import pwcg.mission.playerunit.payload.tank.T34LatePayload;
import pwcg.mission.playerunit.payload.tank.TigerIPayload;

public class TankPayloadFactory
{
	public ITankPayload createPayload(String tankeTypeName, Date date) throws PWCGException 
	{
	    ITankTypeFactory tankTypeFactory = PWCGContext.getInstance().getPlayerTankTypeFactory();
		TankTypeInformation tankType = tankTypeFactory.createTankTypeByAnyName(tankeTypeName);
		TankAttributeMapping attributeMapping = TankAttributeFactory.createTankAttributeMap(tankeTypeName);
	    
        if (attributeMapping == TankAttributeMapping.PZKW_III_L)
        {
            return new PzkwIIILPayload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.PZKW_III_M)
        {
            return new PzkwIIIMPayload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.PZKW_IV_G)
        {
            return new PzkwIVGPayload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.PANTHER_D)
        {
            return new PantherDPayload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.TIGER_I)
        {
            return new TigerIPayload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.SDKFZ_10_AAA)
        {
            return new Sdkfz10Payload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.ELEFANT)
        {
            return new Sdkfz184Payload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.T34_EARLY)
        {
            return new T34EarlyPayload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.T34_LATE)
        {
            return new T34LatePayload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.KV1_S)
        {
            return new KV1SPayload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.SU122)
        {
            return new Su122Payload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.SU152)
        {
            return new Su152payload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.GAZ_AAA)
        {
            return new GAZAAAPayload(tankType, date);
        }
        else if (attributeMapping == TankAttributeMapping.SHERMAN_M4A2)
        {
            return new M4A2Payload(tankType, date);
        }
        else
        {
            return new DefaultPayload(tankType, date);
        } 
	}

    public TankPayloadDesignation getTankPayloadDesignation(String tankTypeName, int selectedPrimaryPayloadId, Date date) throws PWCGException
    {
        ITankPayload payload = createPayload(tankTypeName, date);
        payload.setSelectedPayloadId(selectedPrimaryPayloadId);
        return payload.getSelectedPayloadDesignation();
    }
}
