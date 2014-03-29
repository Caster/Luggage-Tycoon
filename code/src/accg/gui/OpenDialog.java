package accg.gui;

import accg.ACCGProgram;
import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.*;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.i18n.Messages;
import accg.io.Level;
import accg.io.SavedGameManager;

public class OpenDialog extends Dialog {
	
	public OpenDialog(final State s) {
		
		super(Messages.get("OpenDialog.open"), new EmptyComponent()); //$NON-NLS-1$
		
		// the body
		final Component body;
		String[] savedGames = SavedGameManager.getSavedGames();
		if (savedGames.length > 0) {
			List l = new List(40, 6);
			l.addElements(savedGames);
			body = l;
		} else {
			body = new Label(Messages.get("OpenDialog.noSavedGames")); //$NON-NLS-1$
		}
		setBody(body);
		
		// OK button
		Button okButton = new Button(Messages.get("OpenDialog.ok"), s.textures.iconOk); //$NON-NLS-1$
		okButton.addListener(new Listener() {
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					OpenDialog.this.setVisible(false);
					
					// switch to normal mode
					ProgramMode oldMode = s.programMode;
					s.programMode = ProgramMode.NORMAL_MODE;
					s.gui.updateItems();
					
					if (body instanceof List) {
						List l = (List) body;
						try {
							Level level = SavedGameManager.loadSavedGame(
									l.getSelectedElement());
							level.loadInState(s);
							ACCGProgram.setLoadedLevel(l.getSelectedElement() + ".lt");
						} catch (Exception levelException) {
							
							// something went wrong; go back to the old mode
							s.programMode = oldMode;
							s.gui.updateItems();
							
							// show the error message
							levelException.printStackTrace();
							Button closeButton = new Button(Messages.get("OpenDialog.close"), s.textures.iconExit); //$NON-NLS-1$
							final Dialog errorDialog = new Dialog(Messages.get("OpenDialog.error"),
									new Label(Messages.get("OpenDialog.couldNotOpenFile") //$NON-NLS-1$ //$NON-NLS-2$
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
		Button cancelButton = new Button(Messages.get("OpenDialog.cancel"), //$NON-NLS-1$
				s.textures.iconExit);
		addButton(cancelButton);
		cancelButton.addListener(new Listener() {
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					OpenDialog.this.setVisible(false);
				}
			}
		});
	}
}