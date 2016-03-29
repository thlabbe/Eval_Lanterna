package org.thlabbe.eval.lanterna.datas;

import org.thlabbe.eval.lanterna.AppModel;

public class ModelState {
	private AppModel ref;
	private int currentProject;
	private int currentDate;
	private int currentJob;
	private int currentRegion;
	public ModelState(AppModel ref) {
		currentProject = 0;
		currentDate = 0;
		currentJob = 0;
		currentRegion = 0;
	}
	private ModelState(AppModel ref, int prj, int date, int job, int region) {
		this.ref = ref;
		this.currentProject = prj;
		this.currentDate = date;
		this.currentJob = job;
		this.currentRegion = region;
	}
	public int getCurrentProject() {
		return currentProject;
	}
	public int getCurrentDate() {
		return currentDate;
	}
	public int getCurrentJob() {
		return currentJob;
	}
	public int getCurrentRegion() {
		return currentRegion;
	}

	public ModelState setCurrentProject(int newProject) {
		boolean changed = (newProject != this.currentProject);
		this.currentProject = newProject;
		if (changed) {
		    this.setCurrentDate(0);
		}
		return this;
	}
	public ModelState setCurrentDate(int newDate) {
		boolean changed = (newDate != currentDate);
		this.currentDate = newDate;
		if(changed) {
			this.setCurrentJob(0);
	//		this.currentJob = 0;
	//		this.currentRegion = 0;
		}
		return this;
	}		
	public ModelState setCurrentJob(int newJob) {
		boolean changed = newJob != currentJob;
		this.currentJob = newJob;
		if( changed) {
			this.setCurrentRegion(0);
	//		this.currentRegion = 0;
		}
		return this;
	}
	public ModelState setCurrentRegion(int newRegion) {
		boolean changed = newRegion != this.currentRegion;
		this.currentRegion = newRegion;
		if(changed) {
			System.out.println("region changed");
		}
		return this;
	}
	
	@Override
	public String toString() {
		return String.format("ModelState [ref=%s, currentProject=%s, currentDate=%s, currentJob=%s, currentRegion=%s]",
				ref, currentProject, currentDate, currentJob, currentRegion);
	}
	public static void main(String[] args) {
		
		ModelState state = new ModelState(null, 5, 3, 2, 1);
		
		System.out.println("avant " + state.toString());
		state = state.setCurrentRegion(10);
		System.out.println("apres " + state.toString());
		state = state.setCurrentProject(10);
		System.out.println("apres " + state.toString());
	//	assertEquals(10, 0);
		
	}
}
