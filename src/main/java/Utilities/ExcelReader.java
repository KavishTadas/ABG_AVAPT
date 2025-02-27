package Utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExcelReader {

	public static List<String> getUtterances(String filePath, String sheetName) {
		List<String> utterances = new ArrayList<>();
		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheet(sheetName);
			for (Row row : sheet) {

				Cell cell = row.getCell(0); // Column 1 (Index 0)
				if (cell != null) {
					utterances.add(cell.getStringCellValue());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return utterances;
	}

	public static String getRandomUtterance(List<String> utterances) {
		if (utterances.isEmpty()) {
			return "DefaultMessage"; // Fallback in case of an empty sheet
		}
		Random random = new Random();
		return utterances.get(random.nextInt(utterances.size()));
	}
}
