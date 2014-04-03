package accg.gui;

import java.io.IOException;

import accg.State;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.components.EmptyComponent;
import accg.gui.toolkit.components.Label;
import accg.gui.toolkit.components.TextField;
import accg.gui.toolkit.containers.Dialog;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.i18n.Messages;
import accg.io.Level;
import accg.io.SavedGameManager;

public class SaveDialog extends Dialog {

	public SaveDialog(final State s) {
		super(Messages.get("SaveDialog.header"), new EmptyComponent()); //$NON-NLS-1$
		
		final TextField tf = new TextField(40);
		tf.requestFocus();
		setBody(tf);
		
		Button okButton = new Button(Messages.get("SaveDialog.save"), s.textures.iconOk); //$NON-NLS-1$
		okButton.addListener(new Listener() {
			@Override
			public void event(Event event) {
				if (event instanceof MouseClickEvent) {
					try {
						SaveDialog.this.setVisible(false);
						SavedGameManager.saveGame(tf.getText(), new Level(s));
					} catch (IOException levelException) {
						// show the error message
						levelException.printStackTrace();
						Button closeButton = new Button(Messages.get("SaveDialog.close"), s.textures.iconExit); //$NON-NLS-1$
						final Dialog errorDialog = new Dialog(Messages.get("SaveDialog.error"), //$NON-NLS-1$
								new Label(Messages.get("SaveDialog.couldNotSaveFile") //$NON-NLS-1$
								 + levelException.getMessage()), //$NON-NLS-1$
								closeButton);
						closeButton.addListener(new Listener() {
							@Override
							public void event(Event event2) {
								if (event2 instanceof MouseClickEvent) {
									errorDialog.setVisible(false);
								}
							}
						});
						s.gui.add(errorDialog);
					}
				}
			}
		});
		addButton(okButton);
		
		Button cancelButton = new Button(Messages.get("SaveDialog.cancel"), s.textures.iconExit); //$NON-NLS-1$
		cancelButton.addListener(new Listener() {
			@Override
			public void event(Event event) {
				if (event instanceof MouseClickEvent) {
					SaveDialog.this.setVisible(false);
				}
			}
		});
		addButton(cancelButton);
	}
}
