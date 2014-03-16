package accg.gui;

import accg.State;
import accg.gui.toolkit.*;
import accg.gui.toolkit.event.MouseClickEvent;
import accg.gui.toolkit.event.ValueChangeEvent;

/**
 * Menu bar containing the settings.
 */
public class SettingsMenuBar extends MenuBar {
	
	MenuBarItem positionItem;
	MenuBarItem alignmentItem;
	MenuBarItem presentationItem;
	SliderMenuBarItem sensitivityItem;
	
	public SettingsMenuBar(final State s) {

		positionItem = new MenuBarItem("Menu position", s.textures.iconConfigure);
		positionItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					// TODO ask the MenuCollection to open the PositionMenuBar
				}
			}
		});
		add(positionItem);

		alignmentItem = new MenuBarItem("Menu alignment", s.textures.iconConfigure);
		alignmentItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					// TODO ask the MenuCollection to open the AlignmentMenuBar
				}
			}
		});
		add(alignmentItem);

		presentationItem = new MenuBarItem("Menu presentation", s.textures.iconConfigure);
		presentationItem.addListener(new Listener() {
			
			@Override
			public void event(Event e) {
				if (e instanceof MouseClickEvent) {
					// TODO ask the MenuCollection to open the PresenationMenuBar
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
		add(sensitivityItem);
	}
}
