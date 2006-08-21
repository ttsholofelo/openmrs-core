package org.openmrs.web.servlet;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.openmrs.reporting.DataTable;
import org.openmrs.reporting.TableRow;

/**
 * Servlet for rendering a graph of values over time. Accepts the following request parameters:
 * width:  Width of the generated image
 * height: Height of the generated image
 * mimeType: Accepts either image/png or image/jpeg
 * chartTitle: The title of the graph
 * rangeAxisTitle: The y-axis title
 * domainAxisTitle: The x-axis title
 * minRange: The minimum value for y-axis values
 * maxRange: The maximum value for y-axis values
 */
public class PieChartServlet extends AbstractGraphServlet {

	public static final long serialVersionUID = 1231231L;
	private Log log = LogFactory.getLog(PieChartServlet.class);

	protected JFreeChart createChart(HttpServletRequest request, HttpServletResponse response) {
		
		String chartTitle = request.getParameter("chartTitle") == null ? "" : request.getParameter("chartTitle");

		// Create data set
		DataTable ageGenderData = (DataTable)request.getSession().getAttribute("ageGenderDataTable");
		ArrayList<TableRow> ageGenderRows = ageGenderData.getRows();
		DefaultKeyedValues keyValues = new DefaultKeyedValues();
		
		for (TableRow row : ageGenderRows) {
			String category = (String) row.get("gender + age");
			Integer count = (Integer) row.get("Cohort.count");
			log.info("Adding value: " + count + " for " + category );
			try {
				keyValues.addValue(category, count);
			}
			catch (Exception e) {
				log.error(e);
			}
		}
		
		// Create graph
		PieDataset pieSet = new DefaultPieDataset(keyValues);
		JFreeChart chart = ChartFactory.createPieChart(chartTitle, pieSet, true, true, false);

		return chart;
	}
}

	



