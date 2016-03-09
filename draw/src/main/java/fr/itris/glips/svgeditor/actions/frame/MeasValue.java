package fr.itris.glips.svgeditor.actions.frame;

public class MeasValue {

	private int id;

	private String name;

	private int psrId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPsrId() {
		return psrId;
	}

	public void setPsrId(int psrId) {
		this.psrId = psrId;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
