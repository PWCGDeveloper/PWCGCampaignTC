
package pwcg.mission.mcu;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;

public class McuLanding extends BaseFlightMcu
{
	private int goalType = 0;

	public McuLanding ()
	{
		super();

		name = "command Land";
		desc = "command Land";
	}

	public void write(BufferedWriter writer) throws PWCGException
	{
		try
        {
            writer.write("MCU_CMD_Land");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            super.write(writer);

            writeMCUGoal(writer, goalType);

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}	
	
	   
    public McuLanding copy ()
    {
        McuLanding clone = new McuLanding();

        super.clone(clone);
        
        clone.name = "command Land";
        clone.desc = "command Land";

        return clone;
    }
}
