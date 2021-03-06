package model.cards.spells;

import model.cards.Rarity;
import model.cards.minions.Minion;

public class DivineSpirit extends Spell implements MinionTargetSpell {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1144037182193261764L;

	public DivineSpirit() {
		super("Divine Spirit", 3, Rarity.BASIC);

	}

	@Override
	public void performAction(Minion m) {
		m.setMaxHP(m.getMaxHP() * 2);
		m.setCurrentHP(m.getCurrentHP() * 2);

	}

}
