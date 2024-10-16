package team.ivy.oceanguardian.domain.admin.service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.admin.utils.DateConverter;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;

@Service
@Transactional(readOnly = true)
public class ExcelService {

    public void downloadMonitoringExcelFile(List<Monitoring> data, HttpServletResponse response) throws IOException {
        // 워크북 생성 (.xlsx 파일 형식)
        Workbook workbook = new XSSFWorkbook();

        // 시트 생성
        Sheet sheet = workbook.createSheet("해안쓰레기 조사 데이터");

        // 날짜 형식 지정 (yyyy-MM-dd로 표시)
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        // 헤더 작성
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("조사 일련번호");
        headerRow.createCell(1).setCellValue("해안명");
        headerRow.createCell(2).setCellValue("위도");
        headerRow.createCell(3).setCellValue("경도");
        headerRow.createCell(4).setCellValue("해안길이(m)");
        headerRow.createCell(5).setCellValue("예측량(L)");
        headerRow.createCell(6).setCellValue("주요쓰레기종류");
        headerRow.createCell(7).setCellValue("조사시기");

        // 데이터 작성
        int rowNum = 1;
        for (Monitoring monitoring : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(monitoring.getSerialNumber());
            row.createCell(1).setCellValue(monitoring.getCoastName());
            row.createCell(2).setCellValue(monitoring.getLocation().getY());
            row.createCell(3).setCellValue(monitoring.getLocation().getX());
            row.createCell(4).setCellValue(monitoring.getCoastLength());
            row.createCell(5).setCellValue(monitoring.getPredictedTrashVolume());
            row.createCell(6).setCellValue(monitoring.getMainTrashType());

            Cell dateCell = row.createCell(7);
            LocalDateTime updatedAt = monitoring.getUpdatedAt();
            if (updatedAt != null) {
                Date monitoringDate = DateConverter.convertToDate(updatedAt);
                dateCell.setCellValue(monitoringDate);  // Date 값 설정
                dateCell.setCellStyle(dateCellStyle);  // 날짜 형식 적용
            } else {
                dateCell.setCellValue("");
            }
        }

        // HTTP 응답 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=ocean_inspector_data.xlsx");

        // 엑셀 파일을 OutputStream으로 전송
        workbook.write(response.getOutputStream());
        workbook.close();
        response.getOutputStream().flush(); // 데이터 전송 완료 후 flush
    }

    public void downloadCleanupExcelFile(List<Cleanup> data, HttpServletResponse response) throws IOException {
        // 워크북 생성 (.xlsx 파일 형식)
        Workbook workbook = new XSSFWorkbook();

        // 시트 생성
        Sheet sheet = workbook.createSheet("해안쓰레기 청소 데이터");

        // 날짜 형식 지정 (yyyy-MM-dd로 표시)
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        // 헤더 작성
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("청소 일련번호");
        headerRow.createCell(1).setCellValue("해안명");
        headerRow.createCell(2).setCellValue("위도");
        headerRow.createCell(3).setCellValue("경도");
        headerRow.createCell(4).setCellValue("해안길이(m)");
        headerRow.createCell(5).setCellValue("수거 마대 개수(개)");
        headerRow.createCell(6).setCellValue("수거량 환산(L, 마대 개수 * 50L)");
        headerRow.createCell(7).setCellValue("주요쓰레기종류");
        headerRow.createCell(8).setCellValue("청소시기");

        // 데이터 작성
        int rowNum = 1;
        for (Cleanup cleanup : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cleanup.getSerialNumber());
            row.createCell(1).setCellValue(cleanup.getCoastName());
            row.createCell(2).setCellValue(cleanup.getLocation().getY());
            row.createCell(3).setCellValue(cleanup.getLocation().getX());
            row.createCell(4).setCellValue(cleanup.getCoastLength());
            row.createCell(5).setCellValue(cleanup.getActualTrashVolume());
            row.createCell(6).setCellValue(cleanup.getActualTrashVolume()*50);
            row.createCell(7).setCellValue(cleanup.getMainTrashType());

            Cell dateCell = row.createCell(8);
            LocalDateTime updatedAt = cleanup.getUpdatedAt();
            if (updatedAt != null) {
                Date cleanupDate = DateConverter.convertToDate(updatedAt);
                dateCell.setCellValue(cleanupDate);  // Date 값 설정
                dateCell.setCellStyle(dateCellStyle);  // 날짜 형식 적용
            } else {
                dateCell.setCellValue("");
            }

        }

        // HTTP 응답 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=ocean_cleanup_data.xlsx");

        // 엑셀 파일을 OutputStream으로 전송
        workbook.write(response.getOutputStream());
        workbook.close();
        response.getOutputStream().flush(); // 데이터 전송 완료 후 flush
    }

    public void downloadAvgExcelFile(List<Object[]> data, HttpServletResponse response) throws IOException {
        // 워크북 생성 (.xlsx 파일 형식)
        Workbook workbook = new XSSFWorkbook();

        // 시트 생성
        Sheet sheet = workbook.createSheet("해안선 길이 대비 수거량(평균기준)");

        // 헤더 작성
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("해안명");
        headerRow.createCell(1).setCellValue("평균 해안선 길이");
        headerRow.createCell(2).setCellValue("평균 수거량");
        headerRow.createCell(3).setCellValue("평균 해안선 길이 대비 평균 수거량");
        headerRow.createCell(4).setCellValue("위도");
        headerRow.createCell(5).setCellValue("경도");

        // 데이터 작성
        int rowNum = 1;
        for (Object[] objects : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue((String) objects[0]);
            row.createCell(1).setCellValue((Double) objects[1]);
            row.createCell(2).setCellValue((Double) objects[2] * 50);
            row.createCell(3).setCellValue(((Double) objects[2] * 50) / ((Double) objects[1]));
            row.createCell(4).setCellValue((Double) objects[3]);
            row.createCell(5).setCellValue((Double) objects[4]);
        }

        // HTTP 응답 설정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=average_collection_per_coastline_length_data.xlsx");

        // 엑셀 파일을 OutputStream으로 전송
        workbook.write(response.getOutputStream());
        workbook.close();
        response.getOutputStream().flush(); // 데이터 전송 완료 후 flush
    }
}
