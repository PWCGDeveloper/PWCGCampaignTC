package pwcg.mission.flight.factory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;

@RunWith(MockitoJUnitRunner.class)
public class NightFlightTypeConverterTest
{
    @Mock Mission mission;

    @Test
    public void testConvertInvalidOnNightFlight() throws PWCGException
    {
        Mockito.when(mission.isNightMission()).thenReturn(true);
        
        FlightTypes flightType = FlightTypes.ARTILLERY_SPOT;
        FlightTypes convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.GROUND_ATTACK);
        
        flightType = FlightTypes.DIVE_BOMB;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.GROUND_ATTACK);
        
        flightType = FlightTypes.OFFENSIVE;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.GROUND_ATTACK);
        
        flightType = FlightTypes.BALLOON_BUST;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.GROUND_ATTACK);
        
        flightType = FlightTypes.ANTI_SHIPPING_DIVE_BOMB;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.ANTI_SHIPPING_ATTACK);
    }

    @Test
    public void testKeepValidOnNightFlight() throws PWCGException
    {
        Mockito.when(mission.isNightMission()).thenReturn(true);
        
        FlightTypes flightType = FlightTypes.BOMB;
        FlightTypes convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.BOMB);
        
        flightType = FlightTypes.PATROL;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.PATROL);
        
        flightType = FlightTypes.GROUND_ATTACK;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.GROUND_ATTACK);
        
        flightType = FlightTypes.TRANSPORT;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.TRANSPORT);
    }

    @Test
    public void testKeepOriginalOnDayFlight() throws PWCGException
    {
        Mockito.when(mission.isNightMission()).thenReturn(false);
        
        FlightTypes flightType = FlightTypes.ARTILLERY_SPOT;
        FlightTypes convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.ARTILLERY_SPOT);
        
        flightType = FlightTypes.DIVE_BOMB;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.DIVE_BOMB);
        
        flightType = FlightTypes.OFFENSIVE;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.OFFENSIVE);
        
        flightType = FlightTypes.BALLOON_BUST;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.BALLOON_BUST);
        
        flightType = FlightTypes.ANTI_SHIPPING_DIVE_BOMB;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.ANTI_SHIPPING_DIVE_BOMB);
        
        flightType = FlightTypes.BOMB;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.BOMB);
        
        flightType = FlightTypes.PATROL;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.PATROL);
        
        flightType = FlightTypes.GROUND_ATTACK;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.GROUND_ATTACK);
        
        flightType = FlightTypes.TRANSPORT;
        convertedFlightType = NightFlightTypeConverter.getFlightType(mission, flightType);
        assert(convertedFlightType == FlightTypes.TRANSPORT);
    }
}