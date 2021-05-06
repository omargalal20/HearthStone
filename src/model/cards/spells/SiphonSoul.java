package model.cards.spells;

import model.cards.Rarity;
import model.cards.minions.Minion;

public class SiphonSoul extends Spell implements LeechingSpell {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1392660954720104987L;

	public SiphonSoul() {
		super("Siphon Soul", 6, Rarity.RARE);
	}

	@Override
	public int performAction(Minion m) {
		m.setCurrentHP(0);
		return 3;
	}

}
