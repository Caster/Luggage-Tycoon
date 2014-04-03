package accg.gui;

import accg.ACCGProgram;
import accg.State;
import accg.State.ProgramMode;
import accg.gui.toolkit.*;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.components.EmptyComponent;
import accg.gui.toolkit.components.Label;
import accg.gui.toolkit.components.List;
import accg.gui.toolkit.containers.Dialog;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.i18n.Messages;
import accg.io.Level;
import accg.io.SavedGameManager;

public class OpenDialog extends Dialog {
	
	public OpenDialog(final State s) {
		
		super(Messages.get("OpenDialog.open"), new EmptyComponent()); //$NON-NLS-1$
		
		// the body
		final Component body;
		final List savedGamesList = new List(40, 6);
		String[] savedGames = SavedGameManager.getSavedGames();
		if (savedGames.length > 0) {
			savedGamesList.addElements(savedGames);
			body = savedGamesList;
		} else {
			body = new Label(Messages.get("OpenDialog.noSavedGames")); //$NON-NLS-1$
		}
		setBody(body);
		
		// Remove button
		if (body == savedGamesList) {
			final Button removeButton = new Button(Messages.get("OpenDialog.remove"), s.textures.iconBomb); //$NON-NLS-1$
			removeButton.addListener(new Listener() {
				@Override
				public void event(Event event) {
					if (event instanceof MouseClickEvent) {
						Button yesButton = new Button(Messages.get("OpenDialog.yes"), s.textures.iconOk); //$NON-NLS-1$
						Button noButton = new Button(Messages.get("OpenDialog.no"), s.textures.iconExit); //$NON-NLS-1$
						final Dialog confirmDialog = new Dialog(Messages.get("OpenDialog.confirm"),
								new Label(String.format(Messages.get("OpenDialog.removeConfirm"),
										savedGamesList.getSelectedElement())),
								yesButton, noButton);
						yesButton.addListener(new Listener() {
							@Override
							public void event(Event event) {
								if (event instanceof MouseClickEvent) {
									confirmDialog.setVisible(false);
									try {
										SavedGameManager.removeSavedGame(savedGamesList.getSelectedElement());
										savedGamesList.removeSelectedElement();
										if (savedGamesList.getElementCount() == 0) {
											removeButton.setVisible(false);
											OpenDialog.this.setBody(new Label(Messages.get("OpenDialog.noSavedGames"))); //$NON-NLS-1$
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						});
						noButton.addListener(new Listener() {
							@Override
							public void event(Event event) {
								if (event instanceof MouseClickEvent) {
									confirmDialog.setVisible(false);
								}
							}
						});
						s.gui.add(confirmDialog);
					}
				}
			});
			addButton(removeButton);
		}
		
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
