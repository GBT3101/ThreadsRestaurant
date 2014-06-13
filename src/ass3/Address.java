package ass3;

public class Address {

	/********* Fields *********/

	private int _x;
	private int _y;

	/********* Constructors *********/

	public Address() {
		this._x = 0;
		this._y = 0;
	}

	public Address(int x, int y) {
		this._x = x;
		this._y = y;
	}

	/********* Getters/Setters *********/

	public int getX() {
		return _x;
	}

	public int getY() {
		return _y;
	}

	public void setX(int x) {
		this._x = x;
	}

	public void setY(int y) {
		this._y = y;
	}

	/********* Methods *********/

	public double distanceBetween(Address a) {
		int posX = Math.abs(getX() - a.getX());
		int posY = Math.abs(getY() - a.getY());
		return Math.sqrt(posX * posX + posY * posY);
	}

	public String toString() {
		return "[Address: X=" + getX() + ", Y=" + getY() + "]";
	}
}
