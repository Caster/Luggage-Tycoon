package accg.gui;

import accg.ACCGProgram;
import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.Component;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.components.EmptyComponent;
import accg.gui.toolkit.components.Label;
import accg.gui.toolkit.components.List;
import accg.gui.toolkit.containers.Dialog;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.i18n.Messages;
import accg.io.Level;
import accg.io.SavedGameManager;

public class ChooseLevelDialog extends Dialog {
	
	public ChooseLevelDialog(final State s) {
		
		super(Messages.get("ChooseLevelDialog.chooseLevel"), new EmptyComponent()); //$NON-NLS-1$
		
		// the body
		final Component body;
		String[] unlockedLevels = SavedGameManager.getUnlockedLevels(s);
		if (unlockedLevels.length > 0) {
			List l = new List(40, 6);
			l.addElements(unlockedLevels);
			body = l;
		} else {
			body = new Label(Messages.get("ChooseLevelDialog.noSavedGames")); //$NON-NLS-1$
		}
		setBody(body);
		
		// OK button
		Button okButton = new Button("ChooseLevelDialog.ok", null, s.textures.iconOk); //$NON-NLS-1$
		okButton.addListener(new Listener() {
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					ChooseLevelDialog.this.setVisible(false);
					
					// switch to normal mode
					ProgramMode oldMode = s.programMode;
					s.programMode = ProgramMode.NORMAL_MODE;
					s.gui.updateItems();
					
					if (body instanceof List) {
						List l = (List) body;
						try {
							Level level = SavedGameManager.loadLevelByName(
									l.getSelectedElement());
							level.loadInState(s);
							s.loadedBuiltinLevel = true;
							ACCGProgram.setLoadedLevel(level.getLevelName());
						} catch (Exception levelException) {
							
							// something went wrong; go back to the old mode
							s.programMode = oldMode;
							s.gui.updateItems();
							
							// show the error message
							levelException.printStackTrace();
							Button closeButton = new Button("ChooseLevelDialog.close", null, s.textures.iconExit); //$NON-NLS-1$
							final Dialog errorDialog = new Dialog(Messages.get("ChooseLevelDialog.error"),
									new Label(Messages.get("ChooseLevelDialog.couldNotOpenFile") //$NON-NLS-1$ //$NON-NLS-2$
									 + levelException.getMessage()), //$NON-NLS-1$
									closeButton);
							closeButton.addListener(new Listener() {
								@Override
								public void event(Event e2) {
									if (e2 instanceof MouseClickEvent) {
										errorDialog.setVisible(false);
									}
								}
							});
							s.gui.add(errorDialog);
						}
					}
				}
			}
		});
		addButton(okButton);
		
		// cancel button
		Button cancelButton = new Button("ChooseLevelDialog.cancel", null, //$NON-NLS-1$
				s.textures.iconExit);
		addButton(cancelButton);
		cancelButton.addListener(new Listener() {
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					ChooseLevelDialog.this.setVisible(false);
				}
			}
		});
	}
}
