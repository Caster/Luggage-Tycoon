package accg.gui;

import accg.State;
import accg.gui.toolkit.*;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.io.Level;
import accg.io.SavedGameManager;

public class OpenDialog extends Dialog {
	
	public OpenDialog(final State s) {
		
		super("Open", new EmptyComponent());
		
		// the body
		final Component body;
		String[] savedGames = SavedGameManager.getSavedGames();
		if (savedGames.length > 0) {
			List l = new List(40, 6);
			l.addElements(savedGames);
			body = l;
		} else {
			body = new Label("You did not save any games yet.");
		}
		setBody(body);
		
		// OK button
		Button okButton = new Button("OK", s.textures.iconOk);
		okButton.addListener(new Listener() {
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					OpenDialog.this.setVisible(false);
					
					if (body instanceof List) {
						List l = (List) body;
						try {
							Level level = SavedGameManager.loadSavedGame(
									l.getSelectedElement());
							level.loadInState(s);
						} catch (Exception levelException) {
							levelException.printStackTrace();
							Button closeButton = new Button("Close", s.textures.iconExit);
							final Dialog errorDialog = new Dialog("Error", new Label("Well, "
									+ "this is embarrassing. We could not open "
									+ "the file. The following error is all we "
									+ "have: " + levelException.getMessage() + "."),
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
		Button cancelButton = new Button("Cancel",
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
