package io.pivotal.john.sitestpoc;

import java.util.ArrayList;
import java.util.List;

public class Music {
	private List<String> notes = new ArrayList<>();

	public Music() {
	}

	public Music(List<String> notes) {
		this.notes = notes;
	}

	public List<String> getNotes() {
		return notes;
	}

	public void setNotes(List<String> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "Music{" +
			"notes=" + notes +
			'}';
	}
}
