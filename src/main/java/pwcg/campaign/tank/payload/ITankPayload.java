package pwcg.campaign.tank.payload;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.platoon.ITankPlatoon;

public interface ITankPayload
{
    ITankPayload copy();
    TankPayloads getPayloads();
    TankModifications getModifications();

    TankPayloadDesignation getSelectedPayloadDesignation() throws PWCGException;
    List<TankPayloadDesignation> getAvailablePayloadDesignations(ITankPlatoon unit) throws PWCGException;
    int getPayloadIdByDescription(String payloadDescription);
    String getPayloadMaskByDescription(String payloadDescription);
    public int createWeaponsPayload(ITankPlatoon unit) throws PWCGException;
    void createStandardWeaponsPayload();
    int getSelectedPayloadId();
    void setSelectedPayloadId(int selectedPrimaryPayloadId);

    void selectModification(TankPayloadElement payloadElement);
    void clearModifications();
    List<TankPayloadElement> getSelectedModifications() throws PWCGException;
    String generateFullModificationMask() throws PWCGException;
    List<TankPayloadDesignation> getOptionalPayloadModifications();
}
