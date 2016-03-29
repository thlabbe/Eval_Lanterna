package org.thlabbe.eval.lanterna;

import java.io.IOException;
import java.util.List;

import org.thlabbe.eval.lanterna.datas.DSDate;
import org.thlabbe.eval.lanterna.datas.DSItemLeaf;
import org.thlabbe.eval.lanterna.datas.DSJob;
import org.thlabbe.eval.lanterna.datas.DSProject;
import org.thlabbe.eval.lanterna.datas.DSRegion;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialog;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class MyApp {
	Config myAppConf;
	private Panel mainPanel;
	private Panel tablePanel;

	private Screen screen;
	private MultiWindowTextGUI gui;
	private BasicWindow window;

	 Label idProject;
	private Button btnProject;

	 Label idDate;
	private Button btnDate;

	 Label idJob;
	private Button btnJob;

	 Label idRegion;
	private Button btnRegion;

	 final static String UNDEF = "<undefined>";
	 Table<String> table;
	private AppController controller;

	public static void main(String[] args) {
		MyApp myapp = new MyApp();
		try {
			myapp.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() throws IOException {

		myAppConf = ConfigFactory.load("myApp");
		controller = new AppController(this, myAppConf);
			
		Terminal terminal = new DefaultTerminalFactory().createTerminal();
		screen = new TerminalScreen(terminal);
		screen.startScreen();
		
		tablePanel = new Panel();
		tablePanel.withBorder(Borders.doubleLine("Content"));
		tablePanel.setLayoutManager(new LinearLayout());

		table = new Table<>("chargement en cours...");
		tablePanel.addComponent(table);
		
		mainPanel = new Panel();
		mainPanel.setLayoutManager(new BorderLayout());
		mainPanel.setSize(new TerminalSize(80,32));
		mainPanel.addComponent(workspacePanel(/*model*/), BorderLayout.Location.TOP);
		mainPanel.addComponent(tablePanel.withBorder(Borders.doubleLine("Content")), BorderLayout.Location.CENTER);
		
		window = new BasicWindow();
		window.setComponent(mainPanel);

		gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
		gui.addWindowAndWait(window);
	}

	public Panel workspacePanel() {
		Panel panel = new Panel();
		panel.setLayoutManager(new GridLayout(3));
		Label lbProject = new Label("Project :");
		final AppModel model = controller.getModel();
		idProject = new Label(model.getDSProjects().get(model.getState().getCurrentProject()).getName());
		btnProject = new Button("Change...", new Runnable() {
			private ActionListDialog list;
			@Override
			public void run() {
				list = updateActionListProjects();
				list.showDialog(gui);
			}
		});

		panel.addComponent(lbProject);
		panel.addComponent(idProject);
		panel.addComponent(btnProject);

		Label lbDate = new Label("Date :");
		System.out.println(model.getState().toString());
		idDate = new Label(model.getDSDates().get(model.getState().getCurrentDate()).getName());
		btnDate = new Button("Change...", new Runnable() {
			private ActionListDialog list;
			@Override
			public void run() {
				list = updateActionListDates();
				list.showDialog(gui);
			}
		});

		panel.addComponent(lbDate);
		panel.addComponent(idDate);
		panel.addComponent(btnDate);

		Label lbJob = new Label("Job :");
		idJob = new Label(model.getDSJobs().get(model.getState().getCurrentJob()).getName());
		btnJob = new Button("Change...", new Runnable() {
			private ActionListDialog list;
			@Override
			public void run() {
				list = updateActionListJobs();
				list.showDialog(gui);
			}
		});

		panel.addComponent(lbJob);
		panel.addComponent(idJob);
		panel.addComponent(btnJob);

		Label lbRegion = new Label("Region :");
		idRegion = new Label(model.getDSRegions().get(model.getState().getCurrentRegion()).getName());
		btnRegion = new Button("Change...", new Runnable() {
			private ActionListDialog list;
			@Override
			public void run() {
				list = updateActionListRegions();
				list.showDialog(gui);
				// after a region chage then update the table 
				tablePanel.removeComponent(table);
				table = updateItemList();
				tablePanel.addComponent(table);
			}
		});

		panel.addComponent(lbRegion);
		panel.addComponent(idRegion);
		panel.addComponent(btnRegion);

		table = updateItemList(/*model*/);
		tablePanel.addComponent(new EmptySpace());
		tablePanel.addComponent(table);
		return panel;
	}

	private Table<String> updateItemList() {
		
		Table<String> _table = new Table<>("#", "name", "timestamp");
		List<DSItemLeaf> items = controller.getModel().getDSItemLeaves();

		if ( items.size() > 0) {
			for (int i = 0; i < items.size(); i++) {
				_table.getTableModel().addRow(String.valueOf(i), items.get(i).getName(), items.get(i).getTimestamp());
			}
		} else {
			_table.getTableModel().addRow("", "... no item found ...","...");
			
		}
		return _table;
	}

	public ActionListBox updateDates(final AppModel model) {
		final ActionListBox ALBDate = new ActionListBox();
		ALBDate.setPreferredSize(new TerminalSize(12, 3));
		for (String d : Lists.transform(model.getDSDates(), 
				new Function<DSDate,String>(){
					@Override
					public String apply(DSDate input) {
						return input.getName().toString();
					}})) 
		{
			ALBDate.addItem(d, new Runnable() {
				@Override
				public void run() {
					model.setState(model.getState().setCurrentDate(ALBDate.getSelectedIndex()));
				}
			});
		}
		ALBDate.setSelectedIndex(model.getState().getCurrentDate());
		return ALBDate;
	}

	private ActionListDialog updateActionListProjects() {
		return new ActionListDialogBuilder()
				.setTitle("Projects")
				.setDescription("Select a Project.")
				.addActions(projectActions().toArray(new Runnable[0]))
				.build(); 
	}

	private ActionListDialog updateActionListDates() {
		return new ActionListDialogBuilder()
				.setTitle("Dates")
				.setDescription("Select a Date.")
				.addActions(dateActions().toArray(new Runnable[0]))
				.build(); 
	}

	private ActionListDialog updateActionListJobs() {
		return new ActionListDialogBuilder()
				.setTitle("Jobs")
				.setDescription("Select a Job.")
				.addActions(jobActions().toArray(new Runnable[0]))
				.build();
		}
	private ActionListDialog updateActionListRegions() {
		return new ActionListDialogBuilder()
				.setTitle("Regions")
				.setDescription("Select a Region.")
				.addActions(regionActions().toArray(new Runnable[0]))
				.build();
		}

	public List<Runnable> projectActions() {
		return Lists.transform(controller.getModel().getDSProjects(), new Function<DSProject, Runnable>() {
			@Override
			public Runnable apply(final DSProject input) {
				return new Runnable() {
					@Override 
					public String toString() {
						return input.getName();
					}
					@Override
					public void run() {
						controller.update(idProject.getText(), input);
					}};
		}});
	}
	public List<Runnable> dateActions() {
		return Lists.transform(controller.getModel().getDSDates(), new Function<DSDate, Runnable>() {
			@Override
			public Runnable apply(final DSDate input) {
				return new Runnable() {
					@Override 
					public String toString() {
						return input.getName();
					}
					@Override
					public void run() {
						controller.update(idDate.getText(), input);
					}};
		}});
	}
	public List<Runnable> jobActions() {
		return Lists.transform(controller.getModel().getDSJobs(), new Function<DSJob, Runnable>() {
			@Override
			public Runnable apply(final DSJob input) {
				return new Runnable() {
					@Override 
					public String toString() {
						return input.getName();
					}
					@Override
					public void run() {
						controller.update(idJob.getText(), input);
					}};
		}});
	}
	public List<Runnable> regionActions() {
		return Lists.transform(controller.getModel().getDSRegions(), new Function<DSRegion, Runnable>() {
			@Override
			public Runnable apply(final DSRegion input) {
				return new Runnable() {
					@Override 
					public String toString() {
						return input.getName();
					}
					@Override
					public void run() {
						controller.update(idRegion.getText(), input);
					}};
		}});
	}
}
