package accg.gui;

import accg.ACCGProgram;
import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.Button;
import accg.gui.toolkit.Component;
import accg.gui.toolkit.Dialog;
import accg.gui.toolkit.EmptyComponent;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Label;
import accg.gui.toolkit.List;
import accg.gui.toolkit.Listener;
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
		Button okButton = new Button(Messages.get("ChooseLevelDialog.ok"), s.textures.iconOk); //$NON-NLS-1$
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
							ACCGProgram.setLoadedLevel(level.getLevelName());
						} catch (Exception levelException) {
							
							// something went wrong; go back to the old mode
							s.programMode = oldMode;
							s.gui.updateItems();
							
							// show the error message
							levelException.printStackTrace();
							Button closeButton = new Button(Messages.get("ChooseLevelDialog.close"), s.textures.iconExit); //$NON-NLS-1$
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
		Button cancelButton = new Button(Messages.get("ChooseLevelDialog.cancel"), //$NON-NLS-1$
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
