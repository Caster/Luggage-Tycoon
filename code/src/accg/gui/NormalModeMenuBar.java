package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.*;
import accg.gui.toolkit.event.MouseClickEvent;

/**
 * Menu bar for the normal mode.
 */
public class NormalModeMenuBar extends MenuBar {
	
	public NormalModeMenuBar(final MenuStack stack, final State s) {
		
		Button simulateItem = new Button("Simulate", s.textures.iconStart);
		simulateItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.simulation.skipToTime(s.time);
					s.programMode = ProgramMode.SIMULATION_MODE;
					s.gui.updateItems();
				}
			}
		});
		add(simulateItem);
		
		Button buildItem = new Button("Build", s.textures.iconZoomIn);
		buildItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.programMode = ProgramMode.BUILDING_MODE;
					s.gui.updateItems();
				}
			}
		});
		add(buildItem);
		
		Button openItem = new Button("Open", s.textures.iconOpen);
		openItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					List l = new List(40, 6);
					for (int i = 1; i <= 20; i++) {
						l.addElement("File " + i);
					}
					Dialog dialog = new Dialog("Open", l,
							new Button("OK", s.textures.iconOk),
							new Button("Cancel", s.textures.iconExit));
					s.gui.add(new Panel(dialog));
				}
			}
		});
		add(openItem);
		
		Button saveItem = new Button("Save", s.textures.iconSave);
		saveItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					TextField tf = new TextField(40);
					tf.requestFocus();
					Dialog dialog = new Dialog("Save", tf,
							new Button("OK", s.textures.iconOk),
							new Button("Cancel", s.textures.iconExit));
					s.gui.add(new Panel(dialog));
				}
			}
		});
		add(saveItem);
		
		Button settingsItem = new Button("Settings", s.textures.iconConfigure);
		settingsItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(NormalModeMenuBar.this, MainStack.SETTINGS_MENU);
				}
			}
		});
		add(settingsItem);
		
		Button quitItem = new Button("Quit", s.textures.iconExit);
		quitItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					s.escPressed = true;
				}
			}
		});
		add(quitItem);
	}
}
