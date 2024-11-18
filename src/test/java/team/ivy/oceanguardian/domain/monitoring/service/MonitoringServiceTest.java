package team.ivy.oceanguardian.domain.monitoring.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.ivy.oceanguardian.domain.image.entity.Image;
import team.ivy.oceanguardian.domain.image.repository.ImageRepository;
import team.ivy.oceanguardian.domain.member.entity.Member;
import team.ivy.oceanguardian.domain.monitoring.dto.MonitoringResponse;
import team.ivy.oceanguardian.domain.monitoring.entity.Monitoring;
import team.ivy.oceanguardian.domain.monitoring.repository.MonitoringRepository;

@ExtendWith(MockitoExtension.class)
public class MonitoringServiceTest {

    @Mock
    private MonitoringRepository monitoringRepository;
    @Mock
    private ImageRepository imageRepository;
    @InjectMocks
    private MonitoringService monitoringService;

    private Monitoring mockMonitoring;
    private Image mockImage;

    @BeforeEach
    void setUp() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point location = geometryFactory.createPoint(new Coordinate(127.4194, 37.7749));

        //Monitoring 및 Image 객체 초기화
        Member mockMember = Member.builder()
            .id(1L)
            .name("이테스")
            .roles(List.of("USER"))
            .phoneNumber("01012345678")
            .password("1234")
            .build();

        mockMonitoring = Monitoring.builder()
            .id(1L)
            .serialNumber("123456")
            .coastName("해운대")
            .coastLength(500.0)
            .predictedTrashVolume(15.0)
            .mainTrashType((byte)2)
            .location(location)
            .member(mockMember)
            .build();

        mockImage = Image.builder()
            .id(1L)
            .url("http://image-url.com/image1.jpg")
            .description("테스트 데이터 이미지")
            .monitoring(mockMonitoring)
            .build();
    }

    @DisplayName("적절한 데이터를 불러오는지 테스트")
    @Test
    void test_getMonitoring() {
        //Given
        Long monitoringId = 1L;
        when(monitoringRepository.findMonitoringWithMember(monitoringId)).thenReturn(mockMonitoring);
        when(imageRepository.findByMonitoring(mockMonitoring)).thenReturn(List.of(mockImage));

        //When
        MonitoringResponse response = monitoringService.getMonitoring(monitoringId);

        //Then
        assertNotNull(response);
        assertEquals(mockMonitoring.getId(),response.getId());
        assertEquals(mockMonitoring.getSerialNumber(),response.getSerialNumber());
        assertEquals(mockImage.getUrl(),response.getMonitoringImageUrl());
    }

    @Test
    void test_getMonitoring_WithEmptyImageList() {
        //Given
        Long monitoringId = 1L;
        when(monitoringRepository.findMonitoringWithMember(monitoringId)).thenReturn(mockMonitoring);
        when(imageRepository.findByMonitoring(mockMonitoring)).thenReturn(Collections.emptyList());

        //When
        MonitoringResponse response = monitoringService.getMonitoring(monitoringId);

        //Then
        assertNotNull(response);
        assertNull(response.getMonitoringImageUrl());
    }

    @Test
    void test_getMonitoring_WithInvalidId() {
        //Given
        Long invalidMonitoringId = 99L;
        when(monitoringRepository.findMonitoringWithMember(invalidMonitoringId)).thenReturn(null);

        //When & Then
        assertThrows(IllegalAccessError.class, () -> monitoringService.getMonitoring(invalidMonitoringId));
    }

}
