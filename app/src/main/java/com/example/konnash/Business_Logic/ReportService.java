package com.example.konnash.Business_Logic;

import android.content.Context;

import com.example.konnash.Database.TransactionArchiveDAO;
import com.example.konnash.Database.TransactionDAO;
import com.example.konnash.Database.UserProfileDAO;
import com.example.konnash.Model.DateFilter;
import com.example.konnash.Model.Report;
import com.example.konnash.Model.Transaction;
import com.example.konnash.Model.TransactionArchive;
import com.example.konnash.Model.UserProfile;

import java.util.Calendar;
import java.util.List;

public class ReportService {

    private final TransactionDAO        transactionDAO;
    private final TransactionArchiveDAO archiveDAO;
    private final UserProfileDAO        userProfileDAO;
    private final UserService           userService;

    public ReportService(Context context) {
        transactionDAO = new TransactionDAO(context);
        archiveDAO     = new TransactionArchiveDAO(context);
        userProfileDAO = new UserProfileDAO(context);
        userService    = new UserService();
    }

    // ─── توليد التقرير حسب الفلتر ───

    public Report getReport(DateFilter filter, long customStart, long customEnd) {
        long[] range = getDateRange(filter, customStart, customEnd);
        return buildReport(range[0], range[1]);
    }

    // ─── حذف معاملة من كل مكان ───

    public void deleteTransaction(Transaction transaction) {

        if (transaction.getArchiveId() == 0) {
            // معاملة نشطة — حدّث رصيد المستخدم
            UserProfile profile = userProfileDAO.get();
            if (profile != null) {
                userService.reverseTransaction(profile, transaction);
                userProfileDAO.update(profile);
            }
        } else {
            // معاملة مؤرشفة — حدّث الأرشيف
            TransactionArchive archive = archiveDAO.getById(transaction.getArchiveId());
            if (archive != null) {
                if (transaction.getType() == Transaction.Type.INCOME) {
                    archive.setIncome(archive.getIncome() - transaction.getAmount());
                } else {
                    archive.setExpense(archive.getExpense() - transaction.getAmount());
                }
                archive.setCount(archive.getCount() - 1);
                archiveDAO.update(archive);
            }
        }

        // احذف المعاملة نهائياً
        transactionDAO.delete(transaction.getId());
    }

    // ─── تعديل معاملة (معدى تغيير حالة الأرشفة) ───

    public void updateTransaction(Transaction oldTransaction, Transaction newTransaction) {

        if (oldTransaction.getArchiveId() == 0) {
            // نشطة — حدّث رصيد المستخدم
            UserProfile profile = userProfileDAO.get();
            if (profile != null) {
                userService.reverseTransaction(profile, oldTransaction);
                userService.applyTransaction(profile, newTransaction);
                userProfileDAO.update(profile);
            }
        } else {
            // مؤرشفة — حدّث الأرشيف
            TransactionArchive archive = archiveDAO.getById(oldTransaction.getArchiveId());
            if (archive != null) {
                if (oldTransaction.getType() == Transaction.Type.INCOME) {
                    archive.setIncome(archive.getIncome() - oldTransaction.getAmount());
                } else {
                    archive.setExpense(archive.getExpense() - oldTransaction.getAmount());
                }
                if (newTransaction.getType() == Transaction.Type.INCOME) {
                    archive.setIncome(archive.getIncome() + newTransaction.getAmount());
                } else {
                    archive.setExpense(archive.getExpense() + newTransaction.getAmount());
                }
                archiveDAO.update(archive);
            }
        }

        transactionDAO.update(newTransaction);
    }

    // ───── Helpers ─────

    private Report buildReport(long startDate, long endDate) {
        List<Transaction> list  = transactionDAO.getByDateRange(startDate, endDate);
        double            income  = 0;
        double            expense = 0;

        for (Transaction t : list) {
            if (t.getType() == Transaction.Type.INCOME) {
                income  += t.getAmount();
            } else {
                expense += t.getAmount();
            }
        }

        return new Report(income, expense, list.size(), list);
    }

    private long[] getDateRange(DateFilter filter, long customStart, long customEnd) {
        Calendar cal = Calendar.getInstance();
        long now     = cal.getTimeInMillis();

        switch (filter) {

            case TODAY:
                setStartOfDay(cal);
                return new long[]{cal.getTimeInMillis(), now};

            case YESTERDAY:
                cal.add(Calendar.DAY_OF_YEAR, -1);
                setStartOfDay(cal);
                long yStart = cal.getTimeInMillis();
                setEndOfDay(cal);
                return new long[]{yStart, cal.getTimeInMillis()};

            case THIS_WEEK:
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                setStartOfDay(cal);
                return new long[]{cal.getTimeInMillis(), now};

            case LAST_WEEK:
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                setStartOfDay(cal);
                long lwStart = cal.getTimeInMillis();
                cal.add(Calendar.DAY_OF_YEAR, 6);
                setEndOfDay(cal);
                return new long[]{lwStart, cal.getTimeInMillis()};

            case THIS_MONTH:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                setStartOfDay(cal);
                return new long[]{cal.getTimeInMillis(), now};

            case LAST_MONTH:
                cal.add(Calendar.MONTH, -1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                setStartOfDay(cal);
                long lmStart = cal.getTimeInMillis();
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                setEndOfDay(cal);
                return new long[]{lmStart, cal.getTimeInMillis()};

            case THIS_YEAR:
                cal.set(Calendar.DAY_OF_YEAR, 1);
                setStartOfDay(cal);
                return new long[]{cal.getTimeInMillis(), now};

            case LAST_YEAR:
                cal.add(Calendar.YEAR, -1);
                cal.set(Calendar.DAY_OF_YEAR, 1);
                setStartOfDay(cal);
                long lyStart = cal.getTimeInMillis();
                cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
                setEndOfDay(cal);
                return new long[]{lyStart, cal.getTimeInMillis()};

            case CUSTOM:
                return new long[]{customStart, customEnd};

            case ALL:
            default:
                return new long[]{0, now};
        }
    }

    private void setStartOfDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private void setEndOfDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
    }
}