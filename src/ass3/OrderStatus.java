package ass3;

public enum OrderStatus {
	INCOMPLETE(1), IN_PROGRESS(2), COMPLETE(3), DELIVERED(4);
	
	@SuppressWarnings("unused")
	private int value;
	
	private OrderStatus(int value) {
		this.value = value;
	}
	
	
}
