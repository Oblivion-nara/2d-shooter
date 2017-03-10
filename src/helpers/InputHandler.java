package helpers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

/**
 * Handles input for a given Component
 * @author mxw596
 */
public class InputHandler implements KeyListener, MouseListener, MouseWheelListener{
	
	private boolean[] keyArray = new boolean[256];
	private boolean[] mouseArray = new boolean[MouseInfo.getNumberOfButtons()];
	private boolean overComp, mouseWheelMoved = false;
	private String typedAcum = "";
	private Point lastMousePosition = new Point(0,0);
	private Component c;
	private double mouseWheelRotation = 0;
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static Point2D.Double midPoint = new Point2D.Double((double)screenSize.width/2, (double)screenSize.height/2);
	
	/**
	 * adds this class as a listener for a given Component
	 * @param c the Component being listened to
	 */
	public InputHandler(Component c){
		c.addKeyListener(this);
		c.addMouseListener(this);
		c.addMouseWheelListener(this);
		this.c = c;
		
	}

	/**
	 * returns the position of the mouse on the screen
	 * @return the position of the mouse on the screen as a Point
	 */
	public Point getMousePositionOnScreen(){
		try {
			Point p = MouseInfo.getPointerInfo().getLocation();
			if(p == null){
				return lastMousePosition;
			}
			lastMousePosition = p;
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			return c.getMousePosition();
		}
	}
	
	/**
	 * gets the position of the mouse on the component this class is listening to
	 * @return the position of the mouse on the component
	 */
	public Point getMousePositionRelativeToComponent(){
		try {
			Point p = c.getMousePosition();
			if(p == null){
				return lastMousePosition;
			}
			lastMousePosition = p;
			return p;
		} catch (Exception e){
			e.printStackTrace();
			return MouseInfo.getPointerInfo().getLocation();
		}
	}
	
	/**
	 * checks whether the key at a position in he array of keys is being pressed
 	 * @param keyCode the id of the key being checked
	 * @return a true or false value indicating whether the key has been pressed
	 */
	public boolean isKeyDown(int keyCode){
		return keyArray[keyCode];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyArray[e.getKeyCode()] = true;
	}
	
	public void artificialKeyPressed(int keyCode) {
		keyArray[keyCode] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyArray[e.getKeyCode()] = false;
	}
	
	public void artificialKeyReleased(int keyCode) {
		keyArray[keyCode] = false;
	}
	
	public String getTypedAcum(){
		return typedAcum;
	}
	
	public void clearTypedAcum(){
		typedAcum = "";
	}

	@Override
	public void keyTyped(KeyEvent e) {
		typedAcum += e.getKeyChar();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	
	public boolean isMouseOverComp(){
		return overComp;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		overComp = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		overComp = false;
	}
	
	public boolean isMouseDown(int mouseButton){
		return mouseArray[mouseButton];
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseArray[e.getButton()] = true;
	}
	
	public void artificialMousePressed(int mouseButton) {
		mouseArray[mouseButton] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseArray[e.getButton()] = false;
	}
	
	public void artificialMouseReleased(int mouseButton) {
		mouseArray[mouseButton] = false;
	}
	
	public boolean getMouseWheelUp(){
		return mouseWheelRotation < 0;
	}
	
	public boolean getMouseWheelDown(){
		return mouseWheelRotation > 0;
	}

	public boolean hasMouseWheelMoved(){
		return mouseWheelMoved;
	}
	
	@Override
	/**
	 * positive value is down/towards user negative value is up/away from user
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseWheelRotation = e.getWheelRotation();
		mouseWheelMoved = true;
	}
	
	/**
	 * get the rotation amount of the mouse wheel
	 * @return rotation amount as a double
	 */
	public double getMouseWheelRotation() {
		mouseWheelMoved = false;
		return mouseWheelRotation;
	}
	
	/**
	 * stops the mouse wheel
	 */
	public void stopMouseWheel(){
		mouseWheelMoved = false;
	}

}
