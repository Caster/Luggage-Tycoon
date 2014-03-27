package accg.gui;

import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.Button;
import accg.gui.toolkit.Component;
import accg.gui.toolkit.Dialog;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Label;
import accg.gui.toolkit.List;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.MenuBar;
import accg.gui.toolkit.MenuStack;
import accg.gui.toolkit.TextField;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.io.SavedGameManager;

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
					Component body;
					String[] savedGames = SavedGameManager.getSavedGames();
					if (savedGames.length > 0) {
						List l = new List(40, 6);
						l.addElements(savedGames);
						body = l;
					} else {
						body = new Label("You did not save any games yet.");
					}
					Button okButton = new Button("OK", s.textures.iconOk);
					Button cancelButton = new Button("Cancel",
							s.textures.iconExit);
					final Dialog dialog = new Dialog("Open", body, okButton,
							cancelButton);
					okButton.addListener(new Listener() {
						@Override
						public void event(Event e) {
							if (e instanceof MouseClickEvent) {
								dialog.setVisible(false);
								// TODO: Actually load selected game.
							}
						}
					});
					cancelButton.addListener(new Listener() {
						@Override
						public void event(Event e) {
							if (e instanceof MouseClickEvent) {
								dialog.setVisible(false);
							}
						}
					});
					s.gui.add(dialog);
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
					s.gui.add(dialog);
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
