package org.thlabbe.eval.lanterna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.thlabbe.eval.lanterna.datas.DSDate;
import org.thlabbe.eval.lanterna.datas.DSItem;
import org.thlabbe.eval.lanterna.datas.DSItemLeaf;
import org.thlabbe.eval.lanterna.datas.DSJob;
import org.thlabbe.eval.lanterna.datas.DSRegion;
import org.thlabbe.eval.lanterna.datas.DSProject;
import org.thlabbe.eval.lanterna.datas.DataSetBuilder;
import org.thlabbe.eval.lanterna.datas.ModelState;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.typesafe.config.Config;

public class AppModel {

	private Config config;
	private DSProject dataset;
	private ModelState state;
	
	// utility function to downgrade target jre from  1.8 to 1.7 {no lambda allowed}
	
	private class FunctionGetName implements Function<DSItem, String> {
		@Override
		public String apply(DSItem input) {
			return input.getName();
		}		
	}
	public AppModel(Config myAppConf) {
		config = myAppConf;
		try {
			dataset = configProjects();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.state = new ModelState(this);

	}
	
	public DSProject configProjects() throws IOException {
		List<String> ps = config.getStringList("myApp.projects");
		System.out.println(ps);
		DataSetBuilder dsb = new DataSetBuilder();
		String fileName = config.getString("lsFile");
		String root = config.getString("prjRoot");
		System.out.println(fileName + " " + root);
		DSProject res = dsb.load(fileName, root, "data");

		return res;
	}

	public List<DSProject> getDSProjects() {
// FIXME only 1 project */
		List<DSProject> projects = new ArrayList<>();
		projects.add(this.dataset);
		return projects;
	}

	public List<DSRegion> getDSRegions() {
		return Lists.transform(
				dataset.getContent()
				.get(state.getCurrentDate()).getContent()
				.get(state.getCurrentJob()).getContent()
				, new Function<DSItem, DSRegion>(){
			@Override
			public DSRegion apply(DSItem input) {
				return (DSRegion) input;
			}});
	}
	public List<DSItemLeaf> getDSItemLeaves() {
		return Lists.transform(
				dataset.getContent()
				.get(state.getCurrentDate()).getContent()
				.get(state.getCurrentJob()).getContent()
				.get(state.getCurrentRegion()).getContent()
				, new Function<DSItem, DSItemLeaf>(){
			@Override
			public DSItemLeaf apply(DSItem input) {
				return (DSItemLeaf) input;
			}});
	}
	
	public List<DSJob> getDSJobs() {
		return Lists.transform(dataset.getContent().get(state.getCurrentDate()).getContent(), new Function<DSItem, DSJob>(){
			@Override
			public DSJob apply(DSItem input) {
				return (DSJob) input;
			}});
	}
	public List<DSDate> getDSDates() {
		return Lists.transform(dataset.getContent(), new Function<DSItem, DSDate>(){
			@Override
			public DSDate apply(DSItem input) {
				return (DSDate) input;
			}});
	}

	
	public ModelState getState() {
		return state;
	}
	
	public void setState(ModelState state) {
		this.state = state;
	}
	
}
