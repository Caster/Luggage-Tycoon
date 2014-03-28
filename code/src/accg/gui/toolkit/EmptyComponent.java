package accg.gui.toolkit;

/**
 * Component that displays nothing.
 * 
 * This is used instead of <code>null</code> if we do not want a container
 * to have children, to make the code easier.
 */
public class EmptyComponent extends Component {

	@Override
	public int getPreferredWidth() {
		return 0;
	}

	@Override
	public int getPreferredHeight() {
		return 0;
	}

	@Override
	public void draw() {
		// draw nothing
	}

	@Override
	public String getComponentName() {
		return "EmptyComponent";
	}
}
