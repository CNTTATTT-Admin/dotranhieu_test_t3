package LearningManageSystem.LMS.repository;

import LearningManageSystem.LMS.Entity.SchoolSystem.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}
