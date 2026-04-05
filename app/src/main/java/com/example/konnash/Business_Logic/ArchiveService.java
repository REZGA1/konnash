package com.example.konnash.Business_Logic;

import android.content.Context;

import com.example.konnash.Database.TransactionArchiveDAO;
import com.example.konnash.Database.TransactionDAO;
import com.example.konnash.Database.UserProfileDAO;
import com.example.konnash.Model.Transaction;
import com.example.konnash.Model.TransactionArchive;
import com.example.konnash.Model.UserProfile;

import java.util.List;

public class ArchiveService {

    private final TransactionDAO        transactionDAO;
    private final TransactionArchiveDAO archiveDAO;
    private final UserProfileDAO        userProfileDAO;
    private final UserService           userService;

    public ArchiveService(Context context) {
        transactionDAO = new TransactionDAO(context);
        archiveDAO     = new TransactionArchiveDAO(context);
        userProfileDAO = new UserProfileDAO(context);
        userService    = new UserService();
    }

    // أرشفة كل المعاملات النشطة
    public boolean archiveActive(UserProfile profile) {

        List<Transaction> active = transactionDAO.getActive();

        // لا يوجد معاملات نشطة
        if (active.isEmpty()) return false;

        // 1. احسب بيانات الأرشيف
        long   openDate  = getOldestDate(active);
        long   closeDate = getNewestDate(active);
        double income    = userService.calculateTotalIncome(active);
        double expense   = userService.calculateTotalExpense(active);
        int    count     = active.size();

        // 2. أنشئ عنصر الأرشيف واحفظه
        TransactionArchive archive = new TransactionArchive(
                0, openDate, closeDate, count, income, expense
        );
        long archiveId = archiveDAO.insert(archive);

        if (archiveId == -1) return false;

        // 3. حدّث كل معاملة — اربطها بالأرشيف وضعها كمؤرشفة
        for (Transaction t : active) {
            t.setArchiveId(archiveId);
            t.setArchived(true);
            transactionDAO.update(t);
        }

        // 4. أعد رصيد المستخدم للصفر
        profile.setIncome(0);
        profile.setExpense(0);
        userProfileDAO.update(profile);

        return true;
    }

    // إضافة معاملة مباشرة لأرشيف موجود
    public boolean addToArchive(Transaction transaction, long archiveId) {

        TransactionArchive archive = archiveDAO.getById(archiveId);
        if (archive == null) return false;

        // 1. ربط المعاملة بالأرشيف
        transaction.setArchiveId(archiveId);
        transaction.setArchived(true);
        transactionDAO.insert(transaction);

        // 2. تحديث بيانات الأرشيف
        if (transaction.getType() == Transaction.Type.INCOME) {
            archive.setIncome(archive.getIncome() + transaction.getAmount());
        } else {
            archive.setExpense(archive.getExpense() + transaction.getAmount());
        }
        archive.setCount(archive.getCount() + 1);
        archive.setCloseDate(Math.max(archive.getCloseDate(), transaction.getDateTime()));

        archiveDAO.update(archive);
        return true;
    }

    // تعديل معاملة داخل أرشيف — لا يمكن تغيير نوعها لغير مؤرشفة
    public boolean updateInArchive(Transaction oldTransaction, Transaction newTransaction) {

        TransactionArchive archive = archiveDAO.getById(oldTransaction.getArchiveId());
        if (archive == null) return false;

        // 1. اطرح القيمة القديمة
        if (oldTransaction.getType() == Transaction.Type.INCOME) {
            archive.setIncome(archive.getIncome() - oldTransaction.getAmount());
        } else {
            archive.setExpense(archive.getExpense() - oldTransaction.getAmount());
        }

        // 2. أضف القيمة الجديدة
        if (newTransaction.getType() == Transaction.Type.INCOME) {
            archive.setIncome(archive.getIncome() + newTransaction.getAmount());
        } else {
            archive.setExpense(archive.getExpense() + newTransaction.getAmount());
        }

        // 3. احفظ التعديلات
        transactionDAO.update(newTransaction);
        archiveDAO.update(archive);
        return true;
    }

    // حذف معاملة من أرشيف
    public boolean deleteFromArchive(Transaction transaction) {

        TransactionArchive archive = archiveDAO.getById(transaction.getArchiveId());
        if (archive == null) return false;

        // 1. اطرح القيمة من الأرشيف
        if (transaction.getType() == Transaction.Type.INCOME) {
            archive.setIncome(archive.getIncome() - transaction.getAmount());
        } else {
            archive.setExpense(archive.getExpense() - transaction.getAmount());
        }
        archive.setCount(archive.getCount() - 1);

        // 2. احذف المعاملة وحدّث الأرشيف
        transactionDAO.delete(transaction.getId());
        archiveDAO.update(archive);
        return true;
    }

    // ───── Helpers ─────

    private long getOldestDate(List<Transaction> list) {
        long min = list.get(0).getDateTime();
        for (Transaction t : list) {
            if (t.getDateTime() < min) min = t.getDateTime();
        }
        return min;
    }

    private long getNewestDate(List<Transaction> list) {
        long max = list.get(0).getDateTime();
        for (Transaction t : list) {
            if (t.getDateTime() > max) max = t.getDateTime();
        }
        return max;
    }
}