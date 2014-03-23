package accg.gui;

import accg.State;
import accg.gui.toolkit.*;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.gui.toolkit.event.ValueChangeEvent;

/**
 * Menu bar containing the settings.
 */
public class SettingsMenuBar extends MenuBar {
	
	Button positionItem;
	Button alignmentItem;
	Button presentationItem;
	SliderMenuBarItem sensitivityItem;
	
	public SettingsMenuBar(final MenuStack stack, final State s) {

		positionItem = new Button("Menu position", s.textures.iconConfigure);
		positionItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(SettingsMenuBar.this, MainStack.POSITION_MENU);
				}
			}
		});
		add(positionItem);

		alignmentItem = new Button("Menu alignment", s.textures.iconConfigure);
		alignmentItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(SettingsMenuBar.this, MainStack.ALIGNMENT_MENU);
				}
			}
		});
		add(alignmentItem);

		presentationItem = new Button("Menu presentation", s.textures.iconConfigure);
		presentationItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					stack.addMenuBelowOrClose(SettingsMenuBar.this, MainStack.PRESENTATION_MENU);
				}
			}
		});
		add(presentationItem);

		sensitivityItem = new SliderMenuBarItem("Mouse sensitivity", s.textures.iconMouse, 0.1f, 2.0f, s.mouseSensitivityFactor);
		sensitivityItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof ValueChangeEvent) {
					s.mouseSensitivityFactor = sensitivityItem.getValue();
					s.prefs.putFloat("mouse.sensitivity", sensitivityItem.getValue());
				}
			}
		});
		sensitivityItem.setValue(s.mouseSensitivityFactor);
		add(sensitivityItem);
	}
}
