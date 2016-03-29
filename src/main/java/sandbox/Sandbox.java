package sandbox;

import java.io.IOException;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialog;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class Sandbox {

	private static Label action1;
	private static Button button;
	private static ActionListDialog actionList;
	private static MultiWindowTextGUI gui;

	public static void main(String[] args) {
		test1();
		test2();
		test3();		
	}
	public static void test1() {
		long i =0;
		long var = 500_000_000L; 
		long startTime = System.currentTimeMillis();
		while ( i < var) {
			i++;
		}
		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
		System.out.println("Run time 1: " + runTime);
	}
	public static void test2() {
		long i =0;
		long var = 500_000_000L; 
		long startTime = System.currentTimeMillis();
		while ( i != var) {
			i++;
		}
		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
		System.out.println("Run time 2: " + runTime);
	}
	public static void test3() {
		long i =0;
		long var = 500_000_000L; 
		long startTime = System.currentTimeMillis();
		while ( true ) {
			if(i == var) break;
			i++;
		}
		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
		System.out.println("Run time 3: " + runTime);
	}
	public static void _main(String[] args) throws IOException {
		// Setup terminal and screen layers
		Terminal terminal = new DefaultTerminalFactory().createTerminal();
		Screen screen = new TerminalScreen(terminal);
		screen.startScreen();
		BasicWindow window = new BasicWindow();

		Panel mainPanel = buildPanel();
		window.setComponent(mainPanel);

		gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));

		gui.addWindowAndWait(window);
	}

	private static ActionListDialog buildActionListDialog() {
		return new ActionListDialogBuilder().setTitle("Action List Dialog").setDescription("Choose an item")
				.addAction("First Item", new Runnable() {
					@Override
					public void run() {
						action1.setText(" selection 1");
					}
				}).addAction("Second Item", new Runnable() {
					@Override
					public void run() {
						action1.setText(" selection 2");
					}
				}).addAction("Third Item", new Runnable() {
					@Override
					public void run() {
						action1.setText(" selection 3");
					}
				}).build();
	}

	private static Panel buildPanel() {

		Panel panel = new Panel();
		;
		action1 = new Label("action 1");
		panel.addComponent(action1);
		button = new Button("Go !", new Runnable() {
			@Override
			public void run() {
				System.out.println("button activated");
				actionList = buildActionListDialog();
				actionList.showDialog(gui);
			}
		});
		panel.addComponent(button);
		return panel;
	}

}
