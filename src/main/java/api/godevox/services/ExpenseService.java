package api.godevox.services;

import api.godevox.models.db.ExpenseDBModel;
import api.godevox.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseDBModel> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public ExpenseDBModel addExpense(ExpenseDBModel expense) {
        return expenseRepository.save(expense);
    }

    public ExpenseDBModel getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }
}