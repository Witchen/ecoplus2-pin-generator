package pingenerator;

public class PIN {

	private String id;
	private String pin;
	private int status;

	public PIN(String id, String pin, int status) {
		this.id = id;
		this.pin = pin;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "PIN [id=" + id + ", pin=" + pin + ", status=" + status + "]";
	}

}
