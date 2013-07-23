package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.store.ImportData;
import edu.iastate.music.marching.attendance.tasks.Export;
import edu.iastate.music.marching.attendance.tasks.Import;

public class TaskServlet extends AbstractBaseServlet {

	private enum Page {
		export, import_, export_daily;
	}

	private static final long serialVersionUID = 2390747813204817960L;

	private static final String SERVLET_PATH = "task";

	public static final String IMPORT_PARAM_STOREID = "datastore_id";

	private static final Logger LOG = Logger.getLogger(TaskServlet.class
			.getName());

	public static final String EXPORT_DATA_URL = pageToUrl(Page.export,
			SERVLET_PATH);

	public static final String IMPORT_DATA_URL = pageToUrl(Page.import_,
			SERVLET_PATH);

	private void doDailyExport(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {

			LOG.info("Performing the daily full data export.");

			DataTrain train = DataTrain.getAndStartTrain();

			// Check if we should perform an export based on preferences
			if (train.getAppDataManager().get().isCronExportEnabled()) {

				Export.performExport();

			} else {
				LOG.info("Daily full data export currently disabled, doing nothing.");
			}

		} catch (Exception e) {
			LOG.log(Level.SEVERE,
					"Encountered exception doing daily data export", e);
		} finally {
			// Never retry, avoids infinite loops
			resp.sendError(200);
		}
	}

	private void doExport(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		LOG.info("Doing full data export.");

		try {

			Export.performExport();

		} catch (Exception e) {
			LOG.log(Level.SEVERE,
					"Encountered exception doing on-demand data export", e);
		} finally {
			// Never retry, avoids infinite loops
			resp.sendError(200);
		}
	}

	private void doImport(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		LOG.info("Doing full data import.");

		DataTrain dt = DataTrain.getAndStartTrain();

		long id = Long.parseLong(req.getParameter(IMPORT_PARAM_STOREID));
		ImportData importData = dt.getDataManager().getImportData(id);

		try {
			Import.performImport(importData);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Encountered exception doing data import", e);
		}

		try {
			// Always delete the import data afterwards
			dt.getDataManager().removeImportData(importData);
		} finally {
			// Never retry, avoids infinite loops
			resp.sendError(200);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case export:
				doExport(req, resp);
				break;
			case export_daily:
				doDailyExport(req, resp);
				break;
			case import_:
				doImport(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}
	}
}
