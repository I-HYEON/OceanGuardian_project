package team.ivy.oceanguardian.domain.cleanup.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import team.ivy.oceanguardian.domain.member.entity.Member;
import team.ivy.oceanguardian.global.entity.AuditBaseEntity;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cleanup extends AuditBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cleanup_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String serialNumber;
    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point location;
    @Column(nullable = false)
    private String coastName;
    @Column(nullable = false)
    private Double coastLength;
    @Column(nullable = false)
    private Double actualTrashVolume;
    @Column(nullable = false)
    private byte mainTrashType;
    @Column(nullable = false)
    private Boolean pickupDone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;
}