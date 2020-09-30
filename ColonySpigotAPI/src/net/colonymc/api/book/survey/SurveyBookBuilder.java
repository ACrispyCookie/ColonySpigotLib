package net.colonymc.api.book.survey;

import java.util.ArrayList;

public class SurveyBookBuilder {
	
	ArrayList<SurveyLine> lines = new ArrayList<SurveyLine>();
	
	public SurveyBook build() {
		SurveyBook book = new SurveyBook(lines);
		return book;
	}
	
	public SurveyBookBuilder addLine(SurveyLine line) {
		lines.add(line);
		return this;
	}

}
