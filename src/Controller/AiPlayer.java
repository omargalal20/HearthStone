package Controller;

import java.io.IOException;
import java.util.ArrayList;

import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;

public class AiPlayer{
    private Hero AiHero;

	public AiPlayer() throws IOException, CloneNotSupportedException{
	 ArrayList<Hero> list = new ArrayList<Hero>();
	 list.add(new Hunter());
	 list.add(new Mage());
	 list.add(new Priest());
	 list.add(new Paladin());
	 list.add(new Warlock());
	 AiHero =  list.get((int)Math.random()*list.size());
	}
	
	public Hero getAiHero() {
		return AiHero;
	}

	public void setAiHero(Hero aiHero) {
		AiHero = aiHero;
	}
	

}
