package com.goormthom.danpoong.reboot.repository;

import com.goormthom.danpoong.reboot.domain.User;
import com.goormthom.danpoong.reboot.domain.type.EProvider;
import com.goormthom.danpoong.reboot.domain.type.ERole;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Boolean existsBySerialIdAndAttendanceTimeIsNotNull(String serialId);

    Boolean existsBySerialIdAndGenderIsNotNull(String serialId);

    @Query("select u.id as id, u.role as role, u.password as password from User u where u.id = :id and u.refreshToken is not null")
    Optional<UserSecurityForm> findFormById(@Param("id") UUID id);

    @Query("select u.id as id, u.role as role, u.password as password from User u where u.id = :id and u.refreshToken = :refreshToken")
    Optional<UserSecurityForm> findFormByIdAndRefreshToken(@Param("id") UUID id, @Param("refreshToken") String refreshToken);

    @Query("select u.id as id, u.role as role, u.password as password from User u where u.serialId = :serialId and u.provider = :provider")
    Optional<UserSecurityForm> findFormBySerialIdAndProvider(@Param("serialId") String serialId, @Param("provider") EProvider provider);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.refreshToken = :refreshToken where u.id = :id")
    void updateRefreshToken(UUID id, String refreshToken);

    @Query("SELECT u.id FROM User u WHERE u.attendanceTime IS NOT NULL AND u.workEndTime > :currentDate")
    List<UUID> findActiveUserIds(LocalDate currentDate);


    interface UserSecurityForm {
        UUID getId();
        ERole getRole();
        String getPassword();

        // User To UserSecurityForm
        static UserSecurityForm of(User user) {
            return new UserSecurityForm() {
                @Override
                public UUID getId() {
                    return user.getId();
                }

                @Override
                public ERole getRole() {
                    return user.getRole();
                }

                @Override
                public String getPassword() {
                    return user.getPassword();
                }
            };
        }
    }
}
