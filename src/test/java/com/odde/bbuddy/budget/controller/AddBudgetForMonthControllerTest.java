package com.odde.bbuddy.budget.controller;

import com.odde.bbuddy.budget.domain.MonthlyBudget;
import com.odde.bbuddy.budget.domain.MonthlyBudgetPlanner;
import org.junit.Test;
import org.springframework.ui.Model;

import java.text.ParseException;

import static com.odde.bbuddy.common.Formats.parseDay;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AddBudgetForMonthControllerTest {

    private static final int SUCCESS = 1;
    private static final int FAIL = 2;
    MonthlyBudgetPlanner mockPlanner = mock(MonthlyBudgetPlanner.class);
    MonthlyBudgetController controller = new MonthlyBudgetController(mockPlanner);
    Model mockModel = mock(Model.class);
    private final MonthlyBudget monthlyBudget = new MonthlyBudget(parseDay("2016-07-01"), 100);

    @Test
    public void go_to_add_budget_for_month_page() throws ParseException {
        assertEquals("add_budget_for_month", controller.confirm(monthlyBudget, mockModel));
    }

    @Test
    public void add_monthly_budget() {
        controller.confirm(monthlyBudget, mockModel);

        verify(mockPlanner).addMonthlyBudget(eq(monthlyBudget), any(Runnable.class), any(Runnable.class));
    }

    @Test
    public void return_add_success_message_to_page_when_add_budget_for_month_successfully() throws ParseException {
        given_add_monthly_budget_will(SUCCESS);

        controller.confirm(monthlyBudget, mockModel);

        verify(mockModel).addAttribute("message", "Successfully add budget for month");
    }

    @Test
    public void return_add_fail_message_to_page_when_add_budget_for_month_failed() {
        given_add_monthly_budget_will(FAIL);

        controller.confirm(monthlyBudget, mockModel);

        verify(mockModel).addAttribute("message", "Add budget for month failed");
    }

    private void given_add_monthly_budget_will(int i) {
        doAnswer(invocation -> {
            Runnable after = (Runnable) invocation.getArguments()[i];
            after.run();
            return null;
        }).when(mockPlanner).addMonthlyBudget(any(MonthlyBudget.class), any(Runnable.class), any(Runnable.class));
    }

}
