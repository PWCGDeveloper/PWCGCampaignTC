package pwcg.campaign.crewmember;

public class SerialNumber
{
    public static final int NO_SERIAL_NUMBER = 0;
    public static final int ACE_STARTING_SERIAL_NUMBER = 20000;
    public static final int PLAYER_STARTING_SERIAL_NUMBER = 1000000;
    public static final int AI_STARTING_SERIAL_NUMBER = 3000000;
    public static final int TANK_STARTING_SERIAL_NUMBER = 5000000;
    
    private int nextPlayerSerialNumber = SerialNumber.PLAYER_STARTING_SERIAL_NUMBER;
    private int nextCrewMemberSerialNumber = SerialNumber.AI_STARTING_SERIAL_NUMBER;
    private int nextPlaneSerialNumber = SerialNumber.TANK_STARTING_SERIAL_NUMBER;

    public enum SerialNumberClassification
    {
        NONE,
        PLAYER,
        ACE,
        AI, 
        TANK
    }

    public static SerialNumberClassification getSerialNumberClassification (int serialNumber)
    {
        if (serialNumber == NO_SERIAL_NUMBER)
        {
            return SerialNumberClassification.NONE;
        }
        else if (serialNumber < PLAYER_STARTING_SERIAL_NUMBER)
        {
            return SerialNumberClassification.ACE;
        }
        else if ((serialNumber >= PLAYER_STARTING_SERIAL_NUMBER) && (serialNumber < AI_STARTING_SERIAL_NUMBER))
        {
            return SerialNumberClassification.PLAYER;
        }
        else if ((serialNumber >= AI_STARTING_SERIAL_NUMBER) && (serialNumber < TANK_STARTING_SERIAL_NUMBER))
        {
            return SerialNumberClassification.AI;
        }
        else
        {
            return SerialNumberClassification.TANK;
        }
    }

    public int getNextCrewMemberSerialNumber()
    {
        ++nextCrewMemberSerialNumber;
        return nextCrewMemberSerialNumber;
    }

    public int getLastPlayerSerialNumber()
    {
        ++nextPlayerSerialNumber;
        return nextPlayerSerialNumber;
    }

    public int getNextTankSerialNumber()
    {
        ++nextPlaneSerialNumber;
        return nextPlaneSerialNumber;
    }
}
