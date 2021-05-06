package model.cards.spells;

import java.io.Serializable;
import java.util.ArrayList;

import model.cards.Rarity;
import model.cards.minions.Minion;

public class CurseOfWeakness extends Spell implements AOESpell,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3843564448494716752L;

	public CurseOfWeakness() {
		super("Curse of Weakness", 2, Rarity.RARE);

	}

	@Override
	public void performAction(ArrayList<Minion> oppField, ArrayList<Minion> curField) {
		for (int i = 0; i < oppField.size(); i++) {
			oppField.get(i).setAttack(oppField.get(i).getAttack() - 2);
		}

	}

}
