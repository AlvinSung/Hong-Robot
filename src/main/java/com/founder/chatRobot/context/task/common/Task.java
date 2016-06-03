package com.founder.chatRobot.context.task.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.founder.chatRobot.common.task.ResultBean;
import com.founder.chatRobot.context.fact.common.Fact;
import com.founder.chatRobot.context.fact.common.FactType;
import com.founder.chatRobot.context.session.common.Session;
import com.founder.chatRobot.knowledgeMap.conception.status.common.Status;
import com.founder.chatRobot.knowledgeMap.conception.taskType.common.TaskConception;
import com.founder.chatRobot.recognition.common.RecognitionManager;
import com.founder.chatRobot.recognition.common.interfaces.old.Recognition;
import com.founder.chatRobot.recognition.confirmRecognition.ConfirmRecognition;
import com.founder.chatRobot.recognition.manage.InternalRecognitionManager;
import com.founder.chatRobot.task.treader.common.TaskTreader;

public abstract class Task {
	private TaskTreader treader;
	private short reloadTimes;
	private Session session;
	private Map<String, Object> parameters = new HashMap<String, Object>();
	private boolean isNeededConfirm;
	private RecognitionManager recognitionManager;
	private int age = 0;
	private boolean isReload = false;

	public Task(TaskConception type, Session session, boolean neededConfirm) {
		this.treader = type.getTreader();
		this.reloadTimes = 0;
		this.session = session;
		this.isNeededConfirm = neededConfirm;
		this.recognitionManager = new InternalRecognitionManager();
	}

	/*
	 * public Set<FactType> checkFacts() { Set<FactType> result = new
	 * HashSet<FactType>();
	 * 
	 * @SuppressWarnings("rawtypes") Collection<Fact> factSet;
	 * 
	 * factSet = this.getSession().getFacts();
	 * 
	 * this.cleanFactList(); for (FactType factType :
	 * this.getTaskType().getNeededFacts().values()) { String facTypeName =
	 * factType.getTypeName(); boolean hasFound = false; for
	 * (@SuppressWarnings("rawtypes") Fact fact : factSet) { if
	 * (fact.getType().getTypeName().equals(facTypeName) && !fact.isName()) {
	 * this.addFact(fact); hasFound = true; break; } }
	 * 
	 * if (!hasFound) { result.add(factType); } }
	 * 
	 * return result;
	 * 
	 * }
	 */

	public Set<FactType> checkFacts(boolean needOldFact) {
		Set<FactType> result = new HashSet<FactType>();
		@SuppressWarnings("rawtypes")
		Collection<Fact> factSet;

		this.cleanFactList();

		if (this.isReload) {
			factSet = this.getSession().getFacts();
		} else {
			factSet = this.getSession().getAllFacts(needOldFact);
		}

		this.cleanFactList();
		for (FactType factType : this.getTaskType().getNeededFacts().values()) {
			String facTypeName = factType.getTypeName();
			boolean hasFound = false;
			for (@SuppressWarnings("rawtypes")
			Fact fact : factSet) {
				if (fact.getType().getTypeName().equals(facTypeName)
						&& !fact.isName()) {
					//Controler.debugLoger.debug("tgvf : " + fact);
					this.addFact(fact);
					hasFound = true;
					break;
				}
			}

			if (!hasFound) {
				result.add(factType);
			}
		}

		return result;

	}

	public ResultBean doTask() {
		ResultBean result = new ResultBean();
		Status notMeetStatus;

		this.age++;
		/*
		 * if (this.isNeededConfirm) { this.removeRecognition("ConfirmInfo"); if
		 * (!this.session.isConfirm()) { result.setAnswer("请问还有什么可以帮您的？");
		 * result.setFinished(); return result; } else { this.isNeededConfirm =
		 * false; } }
		 */

		if ((notMeetStatus = this.getTaskType().checkPreCondition(
				this.getSession())) != null) {
			result.setNewTask(notMeetStatus.getTreaderTask() != null);
			result.setAnswer(notMeetStatus.getUnmeetedInfo());
			if (notMeetStatus.getTreaderTask() != null) {
				result.setFinished();
			}
			return result;
		}

		// 检查后置状态是否满足
		if (!this.getTaskType().checkPosCondition(this.getSession())) {
			result.setAnswer(this.getTaskType().getChinessName()
					+ "已经完成，不需要再进行操作。");
			result.setFinished();
			return result;
		}

		List<String> lostFacts = this.treader.checkFact(this);
		String answer = "";

		if (lostFacts.get(0).equals(TaskTreader.DOTASK)) {
			result = this.treader.tread(this.getFactList(), this);
		}

		if (!result.isFinished()) {
			for (int i = 1; i < lostFacts.size(); i++) {
				answer += lostFacts.get(i) + "\n";
			}
			result.setAnswer(result.getAnswer() + answer);
		} else {
			this.setReload();
		}

		return result;
	}

	public ResultBean reload(Session session) {

		if (this.getReloadInfo() != null) {
			ResultBean just = new ResultBean();
			just.setAnswer(this.getReloadInfo());
			//Controler.debugLoger.debug("adding confirmInfo : ");
			this.cleanRecognition();
			Recognition recognition = new ConfirmRecognition();
			this.addRecognition("ConfirmInfo", recognition);
			return just;
		}

		if (this.getTaskType().checkPreCondition(this.getSession()) != null) {
			session.releaseTask();
			return null;
		}

		if (this.reloadTimes < 2) {
			this.reloadTimes++;
			return this.treader.reload(this.getFactList(), this);
		} else {
			session.releaseTask();
			return null;
		}

	}

	public String getReloadInfo() {
		if (this.age == 0)
			return this.treader.getLoadInfo();
		else
			return this.treader.getRelaodInfo();
	}

	public void addSubsequentTask(String taskTypeName) {
		this.session.addSubsequentTask(taskTypeName);
	}

	public Session getSession() {
		return this.session;
	}

	public Object getParameter(String parameterName) {
		return this.parameters.get(parameterName);
	}

	public Object addParameter(String parameterName, Object parameter) {
		return this.parameters.put(parameterName, parameter);
	}

	public boolean isNeededConfirm() {
		return isNeededConfirm;
	}

	public void setNeededConfirm() {
		this.isNeededConfirm = true;
	}

	public void confirmed() {
		if (this.isNeededConfirm) {
			this.removeRecognition("ConfirmInfo");
		}
		this.isNeededConfirm = false;
	}

	public void cleanRecognition() {
		this.recognitionManager = new InternalRecognitionManager();
	}

	public void addRecognition(String name, Recognition recognition) {
		this.recognitionManager.addRecognition(this.getTaskType().getTypeName()
				+ name, recognition);
		this.recognitionManager.activeRecognition(this.getTaskType()
				.getTypeName() + name);
	}

	public void removeRecognition(String name) {
		this.recognitionManager.disactiveRecognition(this.getTaskType()
				.getTypeName() + name);
		this.recognitionManager.removeRecognition(this.getTaskType()
				.getTypeName() + name);
	}

	public RecognitionManager getRecognitionManager() {
		return this.recognitionManager;
	}

	public int getAge() {
		return age;
	}

	public boolean isReload() {
		return isReload;
	}

	private void setReload() {
		this.isReload = true;
	}

	public abstract void removeFact(@SuppressWarnings("rawtypes") Fact fact);

	/*
	 * public void setNeededConfirm() { this.neededConfirm = false; }
	 */

	public abstract void addFact(@SuppressWarnings("rawtypes") Fact fact);

	public abstract void addFacts(@SuppressWarnings("rawtypes") List<Fact> facts);

	@SuppressWarnings("rawtypes")
	public abstract List<Fact> getFactList();

	public abstract TaskConception getTaskType();

	public abstract void cleanFactList();

}
