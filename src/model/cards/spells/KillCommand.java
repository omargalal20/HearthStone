package model.cards.spells;

import model.cards.Rarity;
import model.cards.minions.Minion;
import model.heroes.Hero;

public class KillCommand extends Spell implements MinionTargetSpell, HeroTargetSpell {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3215529096970139508L;

	public KillCommand() {
		super("Kill Command", 3, Rarity.COMMON);

	}

	@Override
	public void performAction(Hero h) {
		h.setCurrentHP(h.getCurrentHP() - 3);

	}

	@Override
	public void performAction(Minion m) {
		if (m.isDivine())
			m.setDivine(false);
		else
			m.setCurrentHP(m.getCurrentHP() - 5);

	}
}
