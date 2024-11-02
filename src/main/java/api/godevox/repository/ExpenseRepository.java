package api.godevox.repository;

import api.godevox.models.db.ExpenseDBModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseDBModel, Long> {
}