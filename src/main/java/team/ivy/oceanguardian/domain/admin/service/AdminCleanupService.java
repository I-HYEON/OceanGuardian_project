package team.ivy.oceanguardian.domain.admin.service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.ivy.oceanguardian.domain.cleanup.dto.CleanupResponse;
import team.ivy.oceanguardian.domain.cleanup.dto.CoastAvg;
import team.ivy.oceanguardian.domain.cleanup.entity.Cleanup;
import team.ivy.oceanguardian.domain.cleanup.repository.CleanupRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCleanupService {
    private final CleanupRepository cleanupRepository;
    private final ExcelService excelService;

    @Transactional
    public List<CleanupResponse> getCleanupsBetween(LocalDateTime startTime, LocalDateTime endTime) {
        List<Cleanup> cleanups = cleanupRepository.findAllByCreatedAtBetween(startTime, endTime);
        log.info("getCleanupsBetween 데이터 개수"+cleanups.size());
        return cleanups.stream().map(CleanupResponse::toDto).toList();
    }

    @Transactional
    public List<CoastAvg> getGroupedByCoastName() {
        List<Object[]> results = cleanupRepository.findGroupedByCoastName();

        return results.stream()
            .sorted(
                Comparator.comparing((Object[] row) -> ((Double) row[2] * 50) / (Double) row[1]).reversed())
            .map(row -> CoastAvg.toDto(
                (String) row[0],
                Double.parseDouble(String.format("%.4f", ((Double) row[2] * 50) / ((Double) row[1]))),
                (Double) row[3],
                (Double) row[4]
            )).toList();
    }

    @Transactional
    public void downloadCleanupData(LocalDateTime startTime, LocalDateTime endTime, HttpServletResponse response)
        throws IOException {
        List<Cleanup> cleanups = cleanupRepository.findAllByCreatedAtBetween(startTime, endTime);

        excelService.downloadCleanupExcelFile(cleanups, response);
    }

    @Transactional
    public void downloadAvgData(HttpServletResponse response)
        throws IOException {
        List<Object[]> results = cleanupRepository.findGroupedByCoastName();

        excelService.downloadAvgExcelFile(results, response);
    }

}
