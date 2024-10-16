package team.ivy.oceanguardian.domain.admin.service;


import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringResponse;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;
import team.ivy.oceanguardian.domain.monitoring.repository.MonitoringRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminMonitoringService {

    private final MonitoringRepository monitoringRepository;
    private final ExcelService excelService;

    @Transactional
    public List<MonitoringResponse> getMonitoringsBetween(LocalDateTime startTime, LocalDateTime endTime) {

        List<Monitoring> monitorings = monitoringRepository.findAllByCreatedAtBetween(startTime, endTime);
        log.info("getMonitoringsBetween 데이터 개수"+monitorings.size());
        return monitorings.stream().map(MonitoringResponse::toDto).toList();
    }

    @Transactional
    public void downloadMonitoringData(LocalDateTime startTime, LocalDateTime endTime, HttpServletResponse response)
        throws IOException {
        List<Monitoring> monitorings = monitoringRepository.findAllByCreatedAtBetween(startTime, endTime);

        excelService.downloadMonitoringExcelFile(monitorings, response);
    }

    @Transactional
    public Long updateSolveStatusToTrue(Long monitoringId) {
        Monitoring monitoring = monitoringRepository.findById(monitoringId).orElseThrow();

        // 조사 데이터 isResolved 업데이트 후 저장
        Monitoring savedMonitoring = monitoringRepository.save(Monitoring.builder()
            .id(monitoring.getId())
            .serialNumber(monitoring.getSerialNumber())
            .location(monitoring.getLocation())
            .coastName(monitoring.getCoastName())
            .coastLength(monitoring.getCoastLength())
            .predictedTrashVolume(monitoring.getPredictedTrashVolume())
            .mainTrashType(monitoring.getMainTrashType())
            .member(monitoring.getMember())
            .isResolved(Boolean.TRUE)
            .build());

        return savedMonitoring.getId();
    }

    @Transactional
    public Long updateSolveStatusToFalse(Long monitoringId) {
        Monitoring monitoring = monitoringRepository.findById(monitoringId).orElseThrow();

        // 조사 데이터 isResolved 업데이트 후 저장
        Monitoring savedMonitoring = monitoringRepository.save(Monitoring.builder()
            .id(monitoring.getId())
            .serialNumber(monitoring.getSerialNumber())
            .location(monitoring.getLocation())
            .coastName(monitoring.getCoastName())
            .coastLength(monitoring.getCoastLength())
            .predictedTrashVolume(monitoring.getPredictedTrashVolume())
            .mainTrashType(monitoring.getMainTrashType())
            .member(monitoring.getMember())
            .isResolved(Boolean.FALSE)
            .build());

        return savedMonitoring.getId();
    }

}
