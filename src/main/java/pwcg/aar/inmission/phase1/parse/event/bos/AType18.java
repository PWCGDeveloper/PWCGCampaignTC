package pwcg.aar.inmission.phase1.parse.event.bos;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.aar.inmission.phase1.parse.event.ATypeBase;
import pwcg.aar.inmission.phase1.parse.event.IAType18;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;


// T:66199 AType:18 BOTID:51199 PARENTID:50175 POS(75142.617,169.053,188263.078)
public class AType18 extends ATypeBase implements IAType18
{
    private String botId;
    private String vehicleId;
    private Coordinate location;

    public AType18(String line) throws PWCGException
    {
        super();
        parse(line);
    }

    private void parse (String line) throws PWCGException
    {
        botId = getString(line, "AType:18 BOTID:", " PARENTID:");
        vehicleId = getString(line, "PARENTID:", " POS(");
        location = findCoordinate(line, "POS(");
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGIOException
    {
        try
        {
            String format = "T:14605 AType:18 BOTID:%s PARENTID:%s POS(%.1f,%.1f,%.1f)";

            String atype = String.format(format, botId, vehicleId, location.getXPos(),location.getYPos(), location.getZPos());
            writer.write(atype);
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }

    @Override
    public String getBotId()
    {
        return botId;
    }

    @Override
    public String getVehicleId()
    {
        return vehicleId;
    }

    @Override
    public Coordinate getLocation()
    {
        return location;
    }
}
