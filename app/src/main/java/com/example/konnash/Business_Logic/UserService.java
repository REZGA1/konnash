package com.example.konnash.Business_Logic;

import com.example.konnash.Model.Transaction;
import com.example.konnash.Model.UserProfile;

import java.util.List;

public class UserService {

    // Validation
    public boolean isValid(UserProfile profile) {

        if (profile.getStoreName() == null || profile.getStoreName().isEmpty())
            return false;

        if (profile.getPhone() == null || profile.getPhone().isEmpty())
            return false;

        if (profile.getCountryCode() == null || profile.getCountryCode().isEmpty())
            return false;

        return true;
    }

    // تطبيق المعاملة على رصيد المستخدم
    public void applyTransaction(UserProfile profile, Transaction transaction) {
        if (transaction.isArchived()) return;

        if (transaction.getType() == Transaction.Type.INCOME) {
            profile.setIncome(profile.getIncome() + transaction.getAmount());
        } else {
            profile.setExpense(profile.getExpense() + transaction.getAmount());
        }
    }

    // إلغاء تطبيق المعاملة عند الحذف أو الأرشفة
    public void reverseTransaction(UserProfile profile, Transaction transaction) {
        if (transaction.isArchived()) return;

        if (transaction.getType() == Transaction.Type.INCOME) {
            profile.setIncome(profile.getIncome() - transaction.getAmount());
        } else {
            profile.setExpense(profile.getExpense() - transaction.getAmount());
        }
    }

    // حساب مجموع الدخل من المعاملات غير المؤرشفة
    public double calculateTotalIncome(List<Transaction> transactions) {
        double total = 0;
        for (Transaction t : transactions) {
            if (!t.isArchived() && t.getType() == Transaction.Type.INCOME) {
                total += t.getAmount();
            }
        }
        return total;
    }

    // حساب مجموع المصروف من المعاملات غير المؤرشفة
    public double calculateTotalExpense(List<Transaction> transactions) {
        double total = 0;
        for (Transaction t : transactions) {
            if (!t.isArchived() && t.getType() == Transaction.Type.EXPENSE) {
                total += t.getAmount();
            }
        }
        return total;
    }

    // عدد المعاملات غير المؤرشفة
    public int countActiveTransactions(List<Transaction> transactions) {
        int count = 0;
        for (Transaction t : transactions) {
            if (!t.isArchived()) count++;
        }
        return count;
    }

    // تجمع كل المعاملات غير المؤرشفة وتحدّث المستخدم
    public void syncBalance(UserProfile profile, List<Transaction> transactions) {
        profile.setIncome(calculateTotalIncome(transactions));
        profile.setExpense(calculateTotalExpense(transactions));
    }
}