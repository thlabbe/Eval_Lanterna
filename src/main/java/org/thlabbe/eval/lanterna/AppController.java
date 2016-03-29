package org.thlabbe.eval.lanterna;

import org.thlabbe.eval.lanterna.datas.DSDate;
import org.thlabbe.eval.lanterna.datas.DSItem;
import org.thlabbe.eval.lanterna.datas.DSJob;
import org.thlabbe.eval.lanterna.datas.DSRegion;

import com.typesafe.config.Config;

public class AppController {
	private AppModel model;
	private MyApp view;
	
	public AppController(MyApp view, Config config) {
		this.model = new AppModel(config);
		this.view = view;
	}
	
	public AppModel getModel() {
		return model;
	}
	public MyApp getView() {
		return view;
	}

	/**
	 * compare the oldValue to the newItem then update the model and the view if needed
1	 * @param oldVzlue previous item's name
	 * @param newItem selected item
	 */
	public void update(String oldVzlue, DSItem newItem) {
		if ( newItem instanceof DSDate) {
			
			int idx = model.getDSDates().indexOf(newItem);
			if (!newItem.getName().equals(oldVzlue)) {
				model.setState(model.getState().setCurrentDate(idx));
				view.idDate.setText(newItem.getName());
				view.idJob.setText(MyApp.UNDEF);
				view.idRegion.setText(MyApp.UNDEF);
			}
		} else if ( newItem instanceof DSJob) {
			int idx = model.getDSJobs().indexOf(newItem);
			if (!newItem.getName().equals(oldVzlue)) {
				model.setState(model.getState().setCurrentJob(idx));
				view.idJob.setText(newItem.getName());
				view.idRegion.setText(MyApp.UNDEF);
			}
		} else if ( newItem instanceof DSRegion) {
			int idx = model.getDSRegions().indexOf(newItem);
			if (!newItem.getName().equals(oldVzlue)) {
				model.setState(model.getState().setCurrentRegion(idx));
				view.idRegion.setText(newItem.getName());
				//view.idRegion.setText(MyApp.UNDEF);
			}
		}
	}

}
