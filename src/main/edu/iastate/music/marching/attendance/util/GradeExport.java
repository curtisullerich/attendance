package edu.iastate.music.marching.attendance.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.model.User;

public class GradeExport {

	public static final String CONTENT_TYPE_CSV = "text/csv";

	public static void exportCSV(DataTrain train, OutputStream out)
			throws IOException {
		PrintWriter writer = new PrintWriter(out);

		// Header
		writer.println("Email Id" + "\t" + "University ID" + "\t" + "Grade");

		for (User student : train.getUsersController().get(User.Type.Student)) {
			writer.println(student.getPrimaryEmail() + "\t"
					+ student.getUniversityID() + "\t"
					+ student.getGrade().getDisplayName());
		}

		writer.println("");
		writer.flush();
		writer.close();
		out.flush();
	}
}
