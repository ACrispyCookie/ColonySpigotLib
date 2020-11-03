package net.colonymc.colonyspigotapi.book.survey;

import java.util.ArrayList;

public class SurveyBookBuilder {
	
	final ArrayList<SurveyLine> lines = new ArrayList<>();
	
	public SurveyBook build() {
        return new SurveyBook(lines);
	}
	
	public SurveyBookBuilder addLine(SurveyLine line) {
		lines.add(line);
		return this;
	}

}
