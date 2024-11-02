package api.godevox.controllers;

import api.godevox.models.db.ExpenseDBModel;
import api.godevox.models.error.ErrorModel;
import api.godevox.models.request.ExpenseRequestModel;
import api.godevox.models.response.ResponseModel;
import api.godevox.services.ExpenseService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/expenses", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<ResponseModel<List<ExpenseDBModel>>> getAllExpenses() {
        List<ExpenseDBModel> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(new ResponseModel.Builder<List<ExpenseDBModel>>()
                .status("success")
                .data(expenses)
                .build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel<ExpenseDBModel>> addExpense(@Valid @RequestBody(required = false) ExpenseRequestModel expenseRequest) {
        if (expenseRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel.Builder<ExpenseDBModel>()
                            .status("error")
                            .error(new ErrorModel("BAD_REQUEST", "Request body is empty"))
                            .build());
        }

        logger.info("Received request body: {}", expenseRequest);

        ExpenseDBModel expense = new ExpenseDBModel(
                expenseRequest.getName(),
                expenseRequest.getAmount(),
                expenseRequest.getDate()
        );

        ExpenseDBModel savedExpense = expenseService.addExpense(expense);

        return ResponseEntity.ok(new ResponseModel.Builder<ExpenseDBModel>()
                .status("success")
                .data(savedExpense)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<ExpenseDBModel>> getExpenseById(@PathVariable Long id) {
        ExpenseDBModel expense = expenseService.getExpenseById(id);
        if (expense != null) {
            return ResponseEntity.ok(new ResponseModel.Builder<ExpenseDBModel>()
                    .status("success")
                    .data(expense)
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseModel.Builder<ExpenseDBModel>()
                            .status("error")
                            .error(new ErrorModel("NOT_FOUND", "Expense not found"))
                            .build());
        }
    }
}