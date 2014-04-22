package accg.gui;

import accg.State;
import accg.gui.toolkit.Event;
import accg.gui.toolkit.Listener;
import accg.gui.toolkit.components.Button;
import accg.gui.toolkit.components.SliderMenuBarItem;
import accg.gui.toolkit.containers.MenuBar;
import accg.gui.toolkit.containers.MenuStack;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.gui.toolkit.event.ValueChangeEvent;

/**
 * Menu bar containing the settings.
 */
public class SettingsMenuBar extends MenuBar {
	
	Button localeItem;
	Button positionItem;
	Button alignmentItem;
	Button presentationItem;
	SliderMenuBarItem sensitivityItem;
	
	public SettingsMenuBar(final MenuStack stack, final State s) {

		localeItem = new Button("SettingsMenuBar.locale", null, s.textures.iconLocale); //$NON-NLS-1$
		localeItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(SettingsMenuBar.this, MainStack.LOCALE_MENU);
				}
			}
		});
		add(localeItem);
		
		positionItem = new Button("SettingsMenuBar.position", null, s.textures.iconConfigure); //$NON-NLS-1$
		positionItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(SettingsMenuBar.this, MainStack.POSITION_MENU);
				}
			}
		});
		add(positionItem);

		alignmentItem = new Button("SettingsMenuBar.alignment", null, s.textures.iconConfigure); //$NON-NLS-1$
		alignmentItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(SettingsMenuBar.this, MainStack.ALIGNMENT_MENU);
				}
			}
		});
		add(alignmentItem);

		presentationItem = new Button("SettingsMenuBar.presentation", null, s.textures.iconConfigure); //$NON-NLS-1$
		presentationItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(SettingsMenuBar.this, MainStack.PRESENTATION_MENU);
				}
			}
		});
		add(presentationItem);

		sensitivityItem = new SliderMenuBarItem("SettingsMenuBar.sensitivity", null, s.textures.iconMouse, 0.1f, 2.0f, s.mouseSensitivityFactor); //$NON-NLS-1$
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
