package accg.gui;

import accg.State;
import accg.gui.toolkit.*;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.components.SliderMenuBarItem;
import accg.gui.toolkit.containers.MenuBar;
import accg.gui.toolkit.containers.MenuStack;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.gui.toolkit.event.ValueChangeEvent;
import accg.i18n.Messages;

/**
 * Menu bar containing the settings.
 */
public class SettingsMenuBar extends MenuBar {
	
	Button positionItem;
	Button alignmentItem;
	Button presentationItem;
	SliderMenuBarItem sensitivityItem;
	
	public SettingsMenuBar(final MenuStack stack, final State s) {

		positionItem = new Button(Messages.get("SettingsMenuBar.position"), s.textures.iconConfigure); //$NON-NLS-1$
		positionItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(SettingsMenuBar.this, MainStack.POSITION_MENU);
				}
			}
		});
		add(positionItem);

		alignmentItem = new Button(Messages.get("SettingsMenuBar.alignment"), s.textures.iconConfigure); //$NON-NLS-1$
		alignmentItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(SettingsMenuBar.this, MainStack.ALIGNMENT_MENU);
				}
			}
		});
		add(alignmentItem);

		presentationItem = new Button(Messages.get("SettingsMenuBar.presentation"), s.textures.iconConfigure); //$NON-NLS-1$
		presentationItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(SettingsMenuBar.this, MainStack.PRESENTATION_MENU);
				}
			}
		});
		add(presentationItem);

		sensitivityItem = new SliderMenuBarItem(Messages.get("SettingsMenuBar.sensitivity"), s.textures.iconMouse, 0.1f, 2.0f, s.mouseSensitivityFactor); //$NON-NLS-1$
		sensitivityItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof ValueChangeEvent) {
					s.mouseSensitivityFactor = sensitivityItem.getValue();
					s.prefs.putFloat("mouse.sensitivity", sensitivityItem.getValue()); //$NON-NLS-1$
				}
			}
		});
		sensitivityItem.setValue(s.mouseSensitivityFactor);
		add(sensitivityItem);
	}
}
