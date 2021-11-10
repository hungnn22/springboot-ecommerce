package com.springboot.ecommerce.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class ReportService {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Object[]> reports;

	public ReportService(List<Object[]> reports) {
		super();
		this.reports = reports;
		workbook = new XSSFWorkbook();
	}

	public void export(String time, HttpServletResponse response) {
		String fileName = "report-" + time + ".xlsx";

		sheet = workbook.createSheet("Product Sold" + new Date().getTime());
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachement; filename=" + fileName;
		response.setHeader(headerKey, headerValue);

		this.writeHeaderRow();
		this.writeDataRow(reports);

		try {
			ServletOutputStream outputStream = response.getOutputStream();
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeDataRow(List<Object[]> reports) {
		int rowCount = 1;
		for (Object[] object : reports) {
			Row row = sheet.createRow(rowCount++);
			Cell cell = row.createCell(0);
			cell.setCellValue(object[0] + "");

			cell = row.createCell(1);
			cell.setCellValue(object[1] + "");

			cell = row.createCell(2);
			cell.setCellValue(object[3] + "");

			cell = row.createCell(3);
			cell.setCellValue(object[2] + "");

			cell = row.createCell(4);
			cell.setCellValue(object[4] + "");
		}
	}

	private void writeHeaderRow() {
		String[] headers = { "Product ID", "Product Name", "Solded Quantity", "Total Quantity", "Revenue" };
		Row row = sheet.createRow(0);

		CellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(14);

		Cell cell = null;
		for (int i = 0; i < headers.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(cellStyle);
			sheet.autoSizeColumn(i);
		}

	}
}
