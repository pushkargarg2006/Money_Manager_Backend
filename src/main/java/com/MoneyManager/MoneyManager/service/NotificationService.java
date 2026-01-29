package com.MoneyManager.MoneyManager.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.MoneyManager.MoneyManager.DataTransfer_object.expenseDto;
import com.MoneyManager.MoneyManager.entity.profileEntity;
import com.MoneyManager.MoneyManager.reposiratory.ProfileReposiratory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final EmailService emailService;
    private final ProfileReposiratory profileRepositary;
    private final expenseService expenseService;


    @Value("${Money.Manager.frontEnd.Url}")
    private String frontEndUrl;

    // @Scheduled(cron = "0 * * * * *", zone = "IST") 
    @Scheduled(cron = "0 0 22 * * *", zone = "IST") 
    public void sendDailyIncomeExpenseReminder() {
        log.info("Starting daily income and expense reminder task");

        
        List<profileEntity> profiles = profileRepositary.findAll();

        for (profileEntity profile : profiles) {
            String email = profile.getEmail();
            

            String subject = "Daily Income and Expense Reminder";
            String body = "Hii" + profile.getFullName() + ",<br>" +
                    "this is the daily reminder to add your income and expense:<br>" +
                    "to add the details: " + "<br><br>" +
                    "<a href='" + frontEndUrl + "'>Go to MoneyManager</a><br><br>" +
                    "Best regards,<br>" +
                    "MoneyManager Team";
                   

            emailService.sendEmail(email, subject, body);
        }

        log.info("Completed daily income and expense reminder task");

    }
    @Scheduled(cron = "0 0 23 * * *", zone = "IST")
    // @Scheduled(cron = "0 * * * * *", zone = "IST")
    public void sendDailyExpenseSummary(){
        log.info("Job started: send daily expense summary");
        List<profileEntity> profiles = profileRepositary.findAll();
        for (profileEntity profile : profiles) {
            String email = profile.getEmail();
            String subject = "Daily Expense Summary";
            List<expenseDto> dailyExpenses = expenseService.getExpensesByDate(profile.getId(), LocalDate.now());
            if(!dailyExpenses.isEmpty()) {
               StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse:collapse;width:100%;font-family:Arial,sans-serif;'>");
                        table.append("<tr style='background-color:#f2f2f2;'>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>S.No</th>")
                           .append("<th style='border:1px solid #ddd;padding:8px;'>Expense</th>")
                         .append("<th style='border:1px solid #ddd;padding:8px;'>Category</th>")
                         .append("<th style='border:1px solid #ddd;padding:8px;text-align:right;'>Amount (₹)</th>")
                         .append("</tr>");
                            int i = 1;
            for (expenseDto expense : dailyExpenses) {
                 table.append("<tr>")
                    .append("<td style='border:1px solid #ddd;padding:8px;'>").append(i++).append("</td>")
                    .append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getName()).append("</td>")
                    .append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getCategoryName()).append("</td>")
                    .append("<td style='border:1px solid #ddd;padding:8px;text-align:right;'>").append("₹ ").append(expense.getAmount())
                    .append("</td>")
                    .append("</tr>");
                            }
                    table.append("</table>");
            
            String body = "Hii " + profile.getFullName() + ",<br>" +
                    "Here is your expense summary for " + LocalDate.now() + ":<br><br>" +
                    (dailyExpenses.isEmpty() ? "No expenses recorded today." : table.toString()) +
                    "<br>Best regards,<br>" +
                    "MoneyManager Team";

            emailService.sendEmail(email, subject, body);

                        }


        }
        log.info("Job completed: send daily expense summary");
    }



}
