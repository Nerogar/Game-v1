package de.nerogar.gameV1.ai;

import java.util.ArrayList;

public class AIContainer {

	private ArrayList<AILogic> logicList;

	public AIContainer() {
		logicList = new ArrayList<AILogic>();
	}

	public void addLogic(AILogic logic) {
		for (int i = 0; i < logicList.size(); i++) {
			if (logicList.get(i).getClass().equals(logic.getClass())) {
				logicList.remove(i);
				break;
			}
		}
		logicList.add(logic);
	}

	public void clear() {
		logicList.clear();
	}

	public void remove(AILogic logic) {
		for (int i = 0; i < logicList.size(); i++) {
			if (logicList.get(i) == logic) {
				logicList.remove(i);
				return;
			}
		}
	}

	public void update(float time) {
		for (AILogic l : logicList) {
			if (!l.isClosed()) l.update(time);
		}
	}
}
