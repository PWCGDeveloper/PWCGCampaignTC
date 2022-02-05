package pwcg.campaign.battle;

import java.util.ArrayList;
import java.util.List;

public class Battles
{
	private List<HistoricalBattle> battles = new ArrayList<>();

	public List<HistoricalBattle> getBattles()
	{
		return battles;
	}

	public void addBattle(HistoricalBattle battle)
	{
		battles.add(battle);
	}
}
