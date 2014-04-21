package accg;

import static accg.gui.toolkit.GLUtils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.Point;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.Color;

import accg.State.ProgramMode;
import accg.camera.Camera;
import accg.gui.BuildingModeMenuBar;
import accg.gui.MainGUI;
import accg.gui.toolkit.Component;
import accg.gui.toolkit.GLUtils;
import accg.gui.toolkit.GUIUtils;
import accg.i18n.Messages;
import accg.objects.Block;
import accg.objects.Floor;
import accg.objects.ShadowBlock;
import accg.objects.blocks.ConveyorBlock.ConveyorBlockType;
import accg.utils.Utils;

/**
 * The main class for the ACCG program.
 * 
 * @author Thom Castermans
 * @author Willem Sonke
 */
public class ACCGProgram {
	
	/**
	 * Name of the application.
	 */
	public static final String APP_NAME = "Luggage Tycoon";
	/**
	 * Background color of the scene.
	 */
	public static final java.awt.Color BACKGROUND_COLOR =
			new java.awt.Color(0f, 0f, 0f, 1.0f);
	
	/**
	 * If the "--saved-games-dir [dir]" parameter is given when starting the
	 * program, the argument is saved in this field. Otherwise, the value will
	 * be {@code null}.
	 */
	private static String argSavedGamesDir = null;
	
	/** Possible {@link DisplayMode} which the program can use. */
	private DisplayMode windowedMode, fullScreenMode;
	
	/** Camera that provides an easy-to-use API for changing viewpoint and such. */
	private Camera camera;
	
	/**
	 * Point where mouse was pressed. Is compared to point where mouse
	 * is released to see if it was a click or not.
	 */
	private Point clickedPoint;
	
	/**
	 * This boolean indicates if 'c' has been pressed in building mode, meaning
	 * that the user wants to change the block being built.
	 */
	private boolean changingBlock;
	
	/**
	 * Buffer in which the model matrix of OpenGL can be stored.
	 */
	private FloatBuffer modelMatrix;
	/**
	 * Buffer in which the projection matrix of OpenGL can be stored.
	 */
	private FloatBuffer projectionMatrix;
	/**
	 * Buffer in which the viewport matrix of OpenGL can be stored.
	 */
	private IntBuffer viewport;
	/**
	 * Buffers in which mouse object coordinates can be stored.
	 */
	private FloatBuffer mousePos3D;
	private Vector3f mousePos3DvectorNear;
	private Vector3f mousePos3DvectorFar;
	private Vector3f mouseViewVector;
	/**
	 * Vector that can be added to a point in the scene to undo the offset
	 * given to it.
	 */
	private Vector3f offsetVector = new Vector3f(0.5f, 0.5f, 0);
	
	/**
	 * Construct a new instance of the program.
	 */
	public ACCGProgram() {
		changingBlock = false;
		
		modelMatrix = BufferUtils.createFloatBuffer(16);
		projectionMatrix = BufferUtils.createFloatBuffer(16);
		viewport = BufferUtils.createIntBuffer(16); // 16 is minimal size...
		mousePos3D = BufferUtils.createFloatBuffer(16); // 16 is minimal size...
		mousePos3DvectorNear = new Vector3f();
		mousePos3DvectorFar = new Vector3f();
		mouseViewVector = new Vector3f();
	}
	
	/**
	 * The main method of the application.
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if ("--saved-games-dir".equals(args[i]) && i < args.length - 1) { //$NON-NLS-1$
					argSavedGamesDir = args[i + 1];
				}
			}
		}
		
		ACCGProgram p = new ACCGProgram();
		p.start();
	}
	
	/**
	 * Returns the value of the "--saved-games-dir [dir]" parameter, if given.
	 * If not given, this will return {@code null}.
	 * @return The value of the "--saved-games-dir [dir]" parameter.
	 */
	public static String getArgSavedGamesDir() {
		return argSavedGamesDir;
	}
	
	/**
	 * Change the title of the display to reflect the given loaded file/level.
	 * 
	 * @param levelName Name of the level that was loaded, or {@code null} to
	 *            indicate that no level is currently loaded anymore.
	 */
	public static void setLoadedLevel(String levelName) {
		if (levelName == null) {
			Display.setTitle(APP_NAME);
		} else {
			Display.setTitle(APP_NAME + " - " + levelName);
		}
	}
	
	/**
	 * Starts and initializes the program.
	 * 
	 * This method contains the main rendering loop.
	 */
	public void start() {
		
		windowedMode = new DisplayMode(1024, 576);
		fullScreenMode = Display.getDesktopDisplayMode();
		
		try {
			Display.setDisplayMode(windowedMode);
			Display.setFullscreen(false);
			Display.setResizable(true);
			Display.setTitle(APP_NAME); //$NON-NLS-1$
			Display.setVSyncEnabled(true);
			Display.create(new PixelFormat().withSamples(8));
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// save window size to be able to react to resize events
		int displayWidth = Display.getWidth();
		int displayHeight = Display.getHeight();
		
		// pre-initialize stuff
		State s = new State();
		s.fontMenu = MainGUI.loadFont();
		
		// show a loading message, loading textures takes some time
		glClearColor(0.8f, 0.8f, 0.77f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		GUIUtils.make2D();
		String loadingText = Messages.get("ACCGProgram.loading"); //$NON-NLS-1$
		int loadingTextWidth = s.fontMenu.getWidth(loadingText);
		int loadingTextHeight = s.fontMenu.getHeight(loadingText);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		s.fontMenu.drawString((displayWidth - loadingTextWidth) / 2,
				(displayHeight - loadingTextHeight) / 2, loadingText, Color.black);
		glDisable(GL_BLEND);
		Display.update();
		
		// initialize stuff here
		s.textures = new Textures();
		s.floor = new Floor();
		s.floor.setBackgroundColor(BACKGROUND_COLOR);
		s.shadowBlock = new ShadowBlock();
		s.startTime = (float) Sys.getTime() / Sys.getTimerResolution();
		camera = new Camera(s);
		clickedPoint = null;
		
		// intialise GUI stuff
		loadPreferences(s);
		s.gui = MainGUI.getInstance(s);
		s.gui.setWidth(displayWidth);
		s.gui.setHeight(displayHeight);
		
		// enable some GL stuff
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
		glEnable(GL_DEPTH_TEST);
		
		// initialise some GL stuff
		FloatBuffer shadowColor = BufferUtils.createFloatBuffer(4);
		shadowColor.put(new float[] {0, 0, 0, 1});
		shadowColor.flip();
		FloatBuffer shadowMatrix = BufferUtils.createFloatBuffer(16);
		shadowMatrix.put(new float[] {
				1, 0, 0, 0,
				0, 1, 0, 0,
				-0.2f, 0.2f, 0, 0,
				0, 0, 0, 1
		});
		shadowMatrix.flip();
		
		boolean isCloseRequested = false;
		while (!Display.isCloseRequested() && !isCloseRequested) {
			
			// handle resize events
			if (displayWidth != Display.getWidth() || displayHeight != Display.getHeight()) {
				displayWidth = Display.getWidth();
				displayHeight = Display.getHeight();
				s.gui.setWidth(displayWidth);
				s.gui.setHeight(displayHeight);
			}
			
			// update time
			s.prevTime = s.time;
			s.time = (float) ((double) Sys.getTime() / Sys.getTimerResolution() - s.startTime);
			
			// update simulation and belt speed, if applicable
			if (s.programMode == ProgramMode.SIMULATION_MODE) {
				s.simulation.update(s);
				s.simulation.addObjects(s);
				
				if (s.beltSpeed < 1) {
					s.beltSpeed = Math.min(s.beltSpeed + 0.025f, 1);
				}
			} else {
				if (s.beltSpeed > 0) {
					s.beltSpeed = Math.max(s.beltSpeed - 0.025f, 0);
				}
			}
			
			// advance the conveyor belts
			s.beltPosition += s.beltSpeed * (s.time - s.prevTime);
			
			// handle events
			handleKeyEvents(s);
			handlePressedKeys();
			handleScrollEvents(s);
			handleMouseEvents(s);

			// draw the scene (not if we are in the start screen)
			if (s.programMode != ProgramMode.START_MODE) {
				
				// start rendering stuff
				glClearColor(BACKGROUND_COLOR);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				glViewport(0, 0, Display.getWidth(), Display.getHeight());
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				gluPerspective(45, (float) Display.getWidth() / Display.getHeight(), 0.1f, 1000f);
				
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity();
				camera.setLookAt();
				
				updateHighlightedBlock(s);
				
				// step 1: draw floor
				s.drawingShadows = false;
				s.floor.draw(s);
				
				// step 2: draw shadows
				glDisable(GL_COLOR_MATERIAL);
				glMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, shadowColor);
				glPushMatrix();
				glMultMatrix(shadowMatrix);
				s.world.draw(s);
				if (s.programMode == ProgramMode.BUILDING_MODE &&
						s.shadowBlock.hasBlock()) {
					s.shadowBlock.draw(s);
				}
				glPopMatrix();
				glEnable(GL_COLOR_MATERIAL);
				
				// step 3: draw floor
				s.drawingShadows = true;
				s.floor.draw(s);
				
				// step 4: draw the world
				s.world.bc.draw(s);
				
				// step 5: draw the shadow block
				if (s.programMode == ProgramMode.BUILDING_MODE &&
						s.shadowBlock.hasBlock()) {
					s.shadowBlock.draw(s);
				}
				
				// step 6: draw invisible luggage
				s.drawingInvisibleLuggage = true;
				glDepthMask(false);
				glDepthFunc(GL_GREATER);
				glEnable(GL_BLEND);
				glDisable(GL_LIGHTING);
				s.world.luggage.draw(s);
				glEnable(GL_LIGHTING);
				glDisable(GL_BLEND);
				glDepthFunc(GL_LESS);
				glDepthMask(true);
				s.drawingInvisibleLuggage = false;
				
				// step 7: draw visible luggage
				s.world.luggage.draw(s);
				
			} else {
				// we are in the start screen, so draw it
				StartScreenBackground.draw(s);
			}
			
			// draw the menu bars
			GUIUtils.make2D();
			//gui.outputDebug();
			glEnable(GL_BLEND);
			s.gui.draw();
			glDisable(GL_BLEND);
			GUIUtils.make3D();
			
			// check for errors
			Util.checkGLError();
			
			glFlush(); // this is needed, else some stuff is not drawn

			Display.update();
			
			// handle escape
			if (s.escPressed) {
				switch (s.programMode) {
				case START_MODE:
					isCloseRequested = true;
					break;
				case BUILDING_MODE:
					s.programMode = ProgramMode.NORMAL_MODE;
					s.gui.updateItems();
					s.gui.setStatusBarVisible(false);
					s.removingBlocks = false;
					break;
				case NORMAL_MODE:
					s.programMode = ProgramMode.START_MODE;
					s.gui.updateItems();
					break;
				case SIMULATION_MODE:
					s.simulation.clearObjects(s);
					s.programMode = ProgramMode.NORMAL_MODE;
					s.gui.updateItems();
					s.gui.setStatusBarVisible(false);
					break;
				}
				
				s.escPressed = false;
			}
		}
		
		Display.destroy();
	}
	
	/**
	 * Handles pressed / released key events.
	 * 
	 * @param s State, used to access ShadowBlock.
	 */
	public void handleKeyEvents(State s) {		
		while (Keyboard.next()) {
			
			if (Keyboard.getEventKeyState()) {
				// perhaps the GUI wants to handle this?
				if (s.gui.handleKeyEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter())) {
					continue;
				}
				
				switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_RETURN:
					// only respond to Alt+Enter, not just Enter
					if (Keyboard.getEventKey() == Keyboard.KEY_RETURN &&
							!(Keyboard.isKeyDown(Keyboard.KEY_LMENU) ||
							  Keyboard.isKeyDown(Keyboard.KEY_RMENU))) {
						return;
					}
					
					try {
						if (Display.isFullscreen()) {
							Display.setDisplayMode(windowedMode);
							Display.setFullscreen(false);
						} else {
							Display.setDisplayMode(fullScreenMode);
							Display.setFullscreen(true);
						}
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
					break;
				case Keyboard.KEY_ESCAPE:
					s.escPressed = true;
					break;
				// *R*otate a block in building mode
				case Keyboard.KEY_R:
					if (s.programMode == ProgramMode.BUILDING_MODE &&
							s.shadowBlock.hasBlock() && s.shadowBlock.isVisible()
							&& s.shadowBlock.getOrientation() != null) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ||
								Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
							s.shadowBlock.setOrientation(
									s.shadowBlock.getOrientation().rotateLeft());
							s.gui.getStack().buildBar.rotateLeftItem.touch();
						} else {
							s.shadowBlock.setOrientation(
									s.shadowBlock.getOrientation().rotateRight());
							s.gui.getStack().buildBar.rotateRightItem.touch();
						}
					}
					break;
				// switch to *C*hanging the block being placed
				case Keyboard.KEY_C:
					if (s.programMode == ProgramMode.BUILDING_MODE) {
						changingBlock = true;
					}
					break;
				// switch to *F*lat conveyor block in building mode 
				case Keyboard.KEY_F:
					if (changingBlock) {
						// we are now not going to remove blocks anymore
						s.removingBlocks = false;
						
						s.shadowBlock.setConveyorBlockType(ConveyorBlockType.FLAT);
						s.gui.updateItems();
					}
					break;
				// switch to *A*scending conveyor block in building mode
				case Keyboard.KEY_A:
					if (changingBlock) {
						// we are now not going to remove blocks anymore
						s.removingBlocks = false;
						
						s.shadowBlock.setConveyorBlockType(ConveyorBlockType.ASCENDING);
						s.gui.updateItems();
					}
					break;
				// switch to *D*escending conveyor block in building mode
				case Keyboard.KEY_D:
					if (changingBlock) {
						// we are now not going to remove blocks anymore
						s.removingBlocks = false;
						
						s.shadowBlock.setConveyorBlockType(ConveyorBlockType.DESCENDING);
						s.gui.updateItems();
					}
					break;
				// switch to bend *L*eft conveyor block in building mode
				case Keyboard.KEY_B:
					if (changingBlock) {
						// we are now not going to remove blocks anymore
						s.removingBlocks = false;
						
						if (s.shadowBlock.getConveyorBlockType() == ConveyorBlockType.BEND_LEFT) {
							s.shadowBlock.setConveyorBlockType(ConveyorBlockType.BEND_RIGHT);
						} else {
							s.shadowBlock.setConveyorBlockType(ConveyorBlockType.BEND_LEFT);
						}
						s.gui.updateItems();
					}
					break;
				case Keyboard.KEY_DELETE:
					
					if (s.programMode != ProgramMode.BUILDING_MODE) {
						return;
					}
					
					if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) &&
							!Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
						// we are now going to remove blocks
						s.removingBlocks = true;
						
						// hide the shadow block
						s.shadowBlock.setConveyorBlockType(null);
	
						// update the GUI
						s.gui.updateItems();
					} else {
						BuildingModeMenuBar.handleRemoveAllEvent();
					}
					
					break;
				}
			} else {
				switch (Keyboard.getEventKey()) {
				// switch to not *C*hanging the block being placed anymore
				case Keyboard.KEY_C:
					changingBlock = false;
					break;
				}
			}
		}
	}
	
	/**
	 * Handles keys that are pressed.
	 */
	public void handlePressedKeys() {
		
		// if the user is typing text in the GUI, we don't want to handle
		// pressed keys ourselves
		if (Component.getKeyFocusElement() != null) {
			return;
		}
		
		if (changingBlock) {
			return;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) {
				camera.turnRight();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) {
				camera.turnLeft();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) {
				camera.turnDown();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
				camera.turnUp();
			}
		} else {
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)) {
				camera.moveLeft();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)) {
				camera.moveRight();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)) {
				camera.moveForward();
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)) {
				camera.moveBackward();
			}
		}
	}
	
	/**
	 * Handles scrollwheel events from the mouse.
	 * 
	 * @param s State of the program. Used to access the GUI and let that handle
	 *          scroll events, possibly, before letting the camera handle it.
	 */
	public void handleScrollEvents(State s) {
		int dWheel = Mouse.getDWheel();
		if (dWheel == 0) {
			return;
		}
		
		// first see if the menu wants to handle this
		boolean handledByMenu = s.gui.handleMouseScrollEvent(Mouse.getX(), Mouse.getY(), dWheel);
		
		// otherwise, let the camera handle it
		if (!handledByMenu) {
			if (dWheel < 0) {
				camera.moveUp();
			} else if (dWheel > 0) {
				camera.moveDown();
			}
		}
	}
	
	/**
	 * Handles mouse move events and such.
	 * 
	 * @param s State of the program, used to determine in which mode we are
	 *          to see if a {@link ShadowBlock} should be drawn where the
	 *          mouse hovers or not (and also what kind of object).
	 */
	public void handleMouseEvents(State s) {
		
		// Variable handledButton[i] holds if an event for mouse button i
		// was handled or not. This is needed because LWJGL's API for the
		// mouse is slightly weird. Or I just don't get it.
		boolean[] handledButton = new boolean[] {false, false, false};
		boolean handledMouseMove = false;
		boolean handledMouseMoveByMenu = false;
		
		while (Mouse.next()) {
			// handle click
			int eventButton;
			if ((eventButton = Mouse.getEventButton()) != -1) {
				if (eventButton == 0) {
					if (Mouse.getEventButtonState()) {
						clickedPoint = new Point(Mouse.getX(), Mouse.getY());
						// make ShadowBlock not transparent anymore
						if (s.shadowBlock.hasBlock() && s.shadowBlock.isVisible()) {
							s.shadowBlock.setTransparent(false);
							updateShadowBlockAlerted(s);
						}
						
						// check if the user is removing blocks
						// then, remove the block that was clicked
						if (s.removingBlocks) {
							handleRemoval(s);
						}
					} else {
						if (clickedPoint != null) {
							if (Math.abs(clickedPoint.getX() - Mouse.getX()) < 3 &&
									Math.abs(clickedPoint.getY() - Mouse.getY()) < 3) {
								s.gui.handleMouseClickEvent(Mouse.getX(), Mouse.getY());
							}
							
							clickedPoint = null;
						}
						
						// check if the mouse was released after a drag: in that
						// case, add a new block at that position
						if (s.shadowBlock.hasBlock()) {
							if (s.shadowBlock.isVisible() &&
									!s.shadowBlock.isAlerted() &&
									!s.shadowBlock.isTransparent()) {
								s.world.addBlock(s,
										s.shadowBlock.clone());
								MainGUI.updateStatusBarInfo();
							}
							
							s.shadowBlock.setAlerted(false);
							s.shadowBlock.setTransparent(true);
						}
					}
				}
			}
			
			if (!Mouse.getEventButtonState()) {
				int dx = Mouse.getEventDX();
				int dy = Mouse.getEventDY();
				
				// handle general mouse move
				if (!handledMouseMove) {
					// see if a menubar is hovered
					
					if (Mouse.isButtonDown(0)) {
						handledMouseMoveByMenu = s.gui.handleMouseDragEvent(Mouse.getX(), Mouse.getY());
					} else {
						handledMouseMoveByMenu = s.gui.handleMouseMoveEvent(Mouse.getX(), Mouse.getY());
					}
					
					// in building mode, we might have to draw an object where the mouse
					// hovers (that is, calculate intersection of a projected ray from the
					// mouse with the scene, et cetera)
					if (s.programMode == ProgramMode.BUILDING_MODE &&
							s.shadowBlock.hasBlock() && !Mouse.isButtonDown(0)) {
						if (handledMouseMoveByMenu) {
							s.shadowBlock.setVisible(false);
						} else {
							updateShadowBlockPosition(Mouse.getX(), Mouse.getY(), s);
						}
					}
					
					handledMouseMove = true;
				}
				
				// handle left mouse button: mouse button 0
				if (!handledButton[0] && !handledMouseMoveByMenu && Mouse.isButtonDown(0)) {
					if (s.programMode == ProgramMode.BUILDING_MODE && s.shadowBlock.hasBlock()) {
						updateShadowBlockHeight(Mouse.getX(), Mouse.getY(), s);
					} else {
						camera.moveByMouse(dx, dy);
					}
					
					handledButton[0] = true;
				}
				
				// handle middle mouse button: mouse button 2
				if (!handledButton[2] && !handledMouseMoveByMenu && Mouse.isButtonDown(2)) {					
					camera.turnByMouse(dx, dy);
					
					handledButton[2] = true;
				}
			}
		}
	}
	
	/**
	 * Handles a block removal. This should be called when the user clicked the
	 * scene and {@link State#removingBlocks} is <code>true</code>.
	 * 
	 * @param s The state.
	 */
	protected void handleRemoval(State s) {
		
		if (!s.world.bc.inBounds(
				s.world.bc.getHighlightX(),
				s.world.bc.getHighlightY(),
				s.world.bc.getHighlightZ())) {
			return;
		}
		
		Block blockToRemove = s.world.bc.getBlock(
				s.world.bc.getHighlightX(),
				s.world.bc.getHighlightY(),
				s.world.bc.getHighlightZ());
		
		if (blockToRemove == null || !blockToRemove.isDeletable()) {
			return;
		}
		
		s.world.removeBlock(
				s.world.bc.getHighlightX(),
				s.world.bc.getHighlightY(),
				s.world.bc.getHighlightZ());
		
		MainGUI.updateStatusBarInfo();
	}
	
	/**
	 * Updates the highlighted block, based on the mouse position.
	 * 
	 * This should be called every frame.
	 * 
	 * @param s The state.
	 */
	protected void updateHighlightedBlock(State s) {
		
		if (s.world == null) {
			return;
		}
		
		if (!s.removingBlocks) {
			s.world.bc.setHighlight(-1, -1, -1);
			return;
		}
		
		// find the intersection of the camera viewing ray with the scene AABB
		findMouseViewVector(Mouse.getX(), Mouse.getY());		
		double[] result = Utils.getIntersectWithBox(mousePos3DvectorNear,
				mouseViewVector, -0.5, s.fieldLength - 0.5, -0.5, s.fieldWidth -
				0.5, 0.0, s.fieldHeight);
		// are we even hovering the scene?
		if (result == null) {
			s.world.bc.setHighlight(-1, -1, -1);
			return;
		}
		
		// we do not want to start behind the camera
		result[0] = Math.max(0, result[0]);
		Vector3f start = new Vector3f();
		Vector3f end = new Vector3f();
		start.scaleAdd((float) result[0], mouseViewVector, mousePos3DvectorNear);
		start.add(offsetVector);
		end.scaleAdd((float) result[1], mouseViewVector, mousePos3DvectorNear);
		end.add(offsetVector);
		// go a little further, to make sure we do not miss the cell on the ground
		mouseViewVector.scale(0.5f);
		end.add(mouseViewVector);
		
		// find interesting grid cells
		ArrayList<Vector3f> interestingCells = Utils.bresenham3D(start, end);
		// find interesting block closest to mouse
		int firstTakenIndex = s.world.getFirstTakenIndex(interestingCells);
		
		if (firstTakenIndex >= interestingCells.size()) {
			s.world.bc.setHighlight(-1, -1, -1);
			return;
		}
		s.world.bc.setHighlight(
				(int) (interestingCells.get(firstTakenIndex).x),
				(int) (interestingCells.get(firstTakenIndex).y),
				(int) (interestingCells.get(firstTakenIndex).z));
	}
	
	/**
	 * Try to load preferences of the user from last time.
	 * 
	 * @param s The state object to store the preferences in.
	 */
	private static void loadPreferences(State s) {
		if (s.prefs == null) {
			s.prefs = Preferences.userNodeForPackage(ACCGProgram.class);
		}
		
		s.mouseSensitivityFactor = s.prefs.getFloat("mouse.sensitivity", 1.0f); //$NON-NLS-1$
	}
	
	/**
	 * Find the vector describing the viewing ray shot from the mouse and
	 * store it in a global variable.
	 * 
	 * @param mouseX X-coordinate of mouse position on screen.
	 * @param mouseY Y-coordinate of mouse position on screen.
	 */
	private void findMouseViewVector(int mouseX, int mouseY) {
		glGetFloat(GL_MODELVIEW_MATRIX, modelMatrix);
		glGetFloat(GL_PROJECTION_MATRIX, projectionMatrix);
		glGetInteger(GL_VIEWPORT, viewport);
		GLU.gluUnProject(mouseX, mouseY, 0, modelMatrix, projectionMatrix,
				viewport, mousePos3D);
		mousePos3DvectorNear.x = mousePos3D.get(0);
		mousePos3DvectorNear.y = mousePos3D.get(1);
		mousePos3DvectorNear.z = mousePos3D.get(2) * 4;
		GLU.gluUnProject(mouseX, mouseY, 1, modelMatrix, projectionMatrix,
				viewport, mousePos3D);
		mousePos3DvectorFar.x = mousePos3D.get(0);
		mousePos3DvectorFar.y = mousePos3D.get(1);
		mousePos3DvectorFar.z = mousePos3D.get(2) * 4;
		mouseViewVector.sub(mousePos3DvectorFar, mousePos3DvectorNear);
		mouseViewVector.normalize();
	}
	
	/**
	 * Update the alerted property of the shadow block, assuming that this block
	 * is not {@code null} and placed at the position where it will remain for
	 * as long as the alerted property should hold.
	 * 
	 * @param s State, used to look-up ShadowBlock in.
	 */
	private void updateShadowBlockAlerted(State s) {
		if (!s.shadowBlock.hasBlock()) {
			return;
		}
		
		if (s.world.getBlockCount() == s.world.getBlockLimit()) {
			s.shadowBlock.setAlerted(true);
			return;
		}
		
		if (s.world.bc.checkBlockFuzzy(s.shadowBlock.getX(),
				s.shadowBlock.getY(), s.shadowBlock.getZ(),
				s.shadowBlock.getHeight())) {
			s.shadowBlock.setAlerted(false);
		} else {
			s.shadowBlock.setAlerted(true);
		}
	}
	
	/**
	 * Update the height of the shadow object. This function assumes that the
	 * shadow object is not null to begin with.
	 * 
	 * @param mouseX X-coordinate of mouse position on screen.
	 * @param mouseY Y-coordinate of mouse position on screen.
	 * @param s State, used to access shadow object.
	 */
	private void updateShadowBlockHeight(int mouseX, int mouseY, State s) {
		// find the intersection of the camera viewing ray with the block AABB
		findMouseViewVector(mouseX, mouseY);
		double[] result = Utils.getIntersectWithBox(mousePos3DvectorNear,
				mouseViewVector, s.shadowBlock.getX() - 0.5,
				s.shadowBlock.getX() + 0.5, s.shadowBlock.getY() - 0.5,
				s.shadowBlock.getY() + 0.5, 0, s.fieldHeight);
		// are we even hovering the scene?
		if (result == null) {
			s.shadowBlock.setVisible(false);
			return;
		}
		// find the position halfway
		Vector3f halfway = new Vector3f();
		halfway.scaleAdd((float) ((result[0] + result[1]) / 2), mouseViewVector,
				mousePos3DvectorNear);
		s.shadowBlock.setZ((int) GLUtils.clamp(halfway.z, 0, s.fieldHeight - 1));
		s.shadowBlock.setVisible(true);
		updateShadowBlockAlerted(s);
	}
	
	/**
	 * Update the position of the shadow object. This function assumes that the
	 * shadow object is not null to begin with.
	 * 
	 * @param mouseX X-coordinate of mouse position on screen.
	 * @param mouseY Y-coordinate of mouse position on screen.
	 * @param s State, used to access shadow object.
	 */
	private void updateShadowBlockPosition(int mouseX, int mouseY, State s) {
		// find the intersection of the camera viewing ray with the scene AABB
		findMouseViewVector(mouseX, mouseY);		
		double[] result = Utils.getIntersectWithBox(mousePos3DvectorNear,
				mouseViewVector, -0.5, s.fieldLength - 0.5, -0.5, s.fieldWidth -
				0.5, 0.0, s.fieldHeight);
		// are we even hovering the scene?
		if (result == null) {
			s.shadowBlock.setVisible(false);
			return;
		}
		
		// we do not want to start behind the camera
		result[0] = Math.max(0, result[0]);
		Vector3f start = new Vector3f();
		Vector3f end = new Vector3f();
		start.scaleAdd((float) result[0], mouseViewVector, mousePos3DvectorNear);
		start.add(offsetVector);
		end.scaleAdd((float) result[1], mouseViewVector, mousePos3DvectorNear);
		end.add(offsetVector);
		// go a little further, to make sure we do not miss the cell on the ground
		mouseViewVector.scale(0.5f);
		end.add(mouseViewVector);
		
		// find interesting grid cells
		ArrayList<Vector3f> interestingCells = Utils.bresenham3D(start, end);
		// update end position to something that makes more sense
		end.sub(mouseViewVector);
		end.z = 0;
		// check if the position for the block is in bounds, this may not be the case
		// in some corner cases (particular view on edge of scene)
		if (!s.world.bc.inBounds((int) end.x, (int) end.y, (int) end.z)) {
			s.shadowBlock.setVisible(false);
			return;
		}
		// position the shadowobject just before the first cell that contains a
		// block, or hide it if the first block is taken already
		int firstTakenIndex = s.world.getFirstTakenIndex(interestingCells);
		if (firstTakenIndex >= interestingCells.size() - 2 &&
				firstTakenIndex < interestingCells.size() &&
				s.world.bc.getBlock((int) end.x, (int) end.y, 0) != null) {
			s.shadowBlock.setAlerted(true);
			s.shadowBlock.setVisible(true);
			s.shadowBlock.setPosition(end);
		} else if (firstTakenIndex < interestingCells.size() - 1 ||
				interestingCells.get(firstTakenIndex - 1).z > 0) {
			s.shadowBlock.setVisible(false);
		} else {
			s.shadowBlock.setVisible(true);
			s.shadowBlock.setPosition(end);
			updateShadowBlockAlerted(s);
		}
	}
}
