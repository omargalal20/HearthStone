package model.cards.spells;

import model.cards.Rarity;
import model.cards.minions.Minion;

public class SealOfChampions extends Spell implements MinionTargetSpell {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7162607735133214602L;

	public SealOfChampions() {
		super("Seal of Champions", 3, Rarity.COMMON);

	}

	@Override
	public void performAction(Minion m) {
		m.setAttack(m.getAttack() + 3);
		m.setDivine(true);

	}

}
