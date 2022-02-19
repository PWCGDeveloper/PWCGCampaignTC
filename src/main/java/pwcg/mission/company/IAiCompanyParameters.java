package pwcg.mission.company;

import java.util.List;

import pwcg.core.location.Coordinate;

public interface IAiCompanyParameters
{

    List<String> getInfantryDivisionNames();

    List<String> getArmoredDivisionNames();

    List<String> getTankDestroyerDivisionNames();

    List<String> getCompanyNames();

    int getStartingCompanyId();

    Coordinate getCompanyPosition();

    int getNumberOfTank();

    int getNumberOfInfantry();

    int getNumberOfTankDestroyer();
}