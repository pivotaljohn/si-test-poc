package io.pivotal.john.sitestpoc;

import java.io.Serializable;

public class Groove implements Serializable {

	private static final long serialVersionUID = 42L;

	private String name;
	private int funkiness;

	public Groove() {
	}

	public Groove(String name, int funkiness) {
		this.name = name;
		this.funkiness = funkiness;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFunkiness() {
		return funkiness;
	}

	public void setFunkiness(int funkiness) {
		this.funkiness = funkiness;
	}

	@Override
	public String toString() {
		return "Groove{" +
			"name='" + name + '\'' +
			", funkiness=" + funkiness +
			'}';
	}
}
