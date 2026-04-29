package com.example.konnash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.konnash.Business_Logic.LanguageManager;
import com.example.konnash.Database.AppSettingsDAO;
import com.example.konnash.Database.DatabaseHelper;
import com.example.konnash.Database.UserProfileDAO;
import com.example.konnash.Database.TransactionDAO;
import com.example.konnash.Model.AppSettings;
import com.example.konnash.Model.UserProfile;
import com.example.konnash.Model.Transaction;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String selectedLanguageCode = LanguageManager.FRENCH;

    // Temporary storage for client form data during navigation
    private String tempClientName = "";
    private String tempClientPhone = "";
    private String tempClientAddress = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        AppSettingsDAO settingsDAO = new AppSettingsDAO(newBase);
        AppSettings settings = settingsDAO.get();
        if (settings != null && settings.getLanguage() != null) {
            Context context = LanguageManager.applyLanguage(newBase, settings.getLanguage());
            super.attachBaseContext(context);
        } else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        AppSettingsDAO settingsDAO = new AppSettingsDAO(this);
        UserProfileDAO userDAO = new UserProfileDAO(this);
        
        // Navigation Logic: Decide which screen to show on startup
        if (settingsDAO.get() != null && userDAO.get() != null) {
            showCarnetCaisse();
        } else if (settingsDAO.get() != null) {
            showCompleteInfo();
        } else {
            showLanguageSelection();
        }
    }

    // --- Navigation Methods ---

    public void showCarnetCaisse() {
        setContentView(R.layout.activity_carnet_caisse);
        applyWindowInsets(findViewById(R.id.main_root));
        setupBottomNav();
        updateHeaderShopName();
        
        View btnEntree = findViewById(R.id.btnEntree);
        if (btnEntree != null) btnEntree.setOnClickListener(v -> showEntree());
    }

    public void showCarnetCredit() {
        setContentView(R.layout.activity_carnet_credit);
        applyWindowInsets(findViewById(R.id.main_root));
        setupBottomNav();
        updateHeaderShopName();
        
        View fab = findViewById(R.id.btnFABAddClient);
        if (fab != null) fab.setOnClickListener(v -> showAjouterClient());

        loadClientsList();
    }

    public void showProfil() {
        setContentView(R.layout.activity_profil);
        applyWindowInsets(findViewById(R.id.main_root));
        setupBottomNav();
        updateHeaderShopName();

        View btnConfiguration = findViewById(R.id.btnConfiguration);
        LinearLayout layoutConfigDropdown = findViewById(R.id.layoutConfigDropdown);
        ImageView ivConfigArrow = findViewById(R.id.ivConfigArrow);

        if (btnConfiguration != null && layoutConfigDropdown != null) {
            btnConfiguration.setOnClickListener(v -> {
                if (layoutConfigDropdown.getVisibility() == View.GONE) {
                    layoutConfigDropdown.setVisibility(View.VISIBLE);
                    if (ivConfigArrow != null) ivConfigArrow.setRotation(270); // Up arrow
                } else {
                    layoutConfigDropdown.setVisibility(View.GONE);
                    if (ivConfigArrow != null) ivConfigArrow.setRotation(90); // Down arrow
                }
            });
        }

        View btnLangue = findViewById(R.id.btnLangue);
        if (btnLangue != null) {
            btnLangue.setOnClickListener(v -> showLanguageSelection());
        }
    }

    private void updateHeaderShopName() {
        TextView tvShopName = findViewById(R.id.tvShopName);
        if (tvShopName != null) {
            UserProfile profile = new UserProfileDAO(this).get();
            if (profile != null && profile.getStoreName() != null && !profile.getStoreName().isEmpty()) {
                tvShopName.setText(profile.getStoreName());
            }
        }
    }

    private void setupBottomNav() {
        View nav = findViewById(R.id.bottom_navigation);
        if (nav instanceof LinearLayout) {
            LinearLayout navLayout = (LinearLayout) nav;
            // index 0: Credit, 1: Caisse, 2: Profil
            if (navLayout.getChildCount() >= 3) {
                navLayout.getChildAt(0).setOnClickListener(v -> showCarnetCredit());
                navLayout.getChildAt(1).setOnClickListener(v -> showCarnetCaisse());
                navLayout.getChildAt(2).setOnClickListener(v -> showProfil());
            }
        }
    }

    // --- Onboarding & Setup ---

    private void showLanguageSelection() {
        setContentView(R.layout.activity_main);
        applyWindowInsets(findViewById(R.id.main));

        View btnValider = findViewById(R.id.btnValiderLanguage);
        View btnTurkish = findViewById(R.id.btnTurkish);
        View btnEnglish = findViewById(R.id.btnEnglish);
        View btnFrench = findViewById(R.id.btnFrench);
        View btnArabic = findViewById(R.id.btnArabic);

        View[] langButtons = {btnTurkish, btnEnglish, btnFrench, btnArabic};

        if (btnTurkish != null) btnTurkish.setOnClickListener(v -> selectLanguage(LanguageManager.TURKISH, btnTurkish, langButtons, btnValider));
        if (btnEnglish != null) btnEnglish.setOnClickListener(v -> selectLanguage(LanguageManager.ENGLISH, btnEnglish, langButtons, btnValider));
        if (btnFrench != null)  btnFrench.setOnClickListener(v -> selectLanguage(LanguageManager.FRENCH, btnFrench, langButtons, btnValider));
        if (btnArabic != null)  btnArabic.setOnClickListener(v -> selectLanguage(LanguageManager.ARABIC, btnArabic, langButtons, btnValider));

        if (btnValider != null) {
            btnValider.setOnClickListener(v -> {
                new AppSettingsDAO(this).save(new AppSettings(selectedLanguageCode, null));
                recreate(); 
            });
        }
    }

    private void selectLanguage(String code, View selectedBtn, View[] allButtons, View validerBtn) {
        selectedLanguageCode = code;
        if (validerBtn != null) validerBtn.setVisibility(View.VISIBLE);
        for (View btn : allButtons) {
            if (btn != null) {
                if (btn == selectedBtn) btn.setBackgroundResource(R.drawable.button_shape_selected);
                else btn.setBackgroundResource(R.drawable.button_shape_global);
            }
        }
    }

    private void showCompleteInfo() {
        setContentView(R.layout.activity_complete_info);
        applyWindowInsets(findViewById(R.id.main));
        EditText etStore = findViewById(R.id.etPointOfSale);
        findViewById(R.id.btnValidate).setOnClickListener(v -> {
            String name = etStore.getText().toString().trim();
            if (!name.isEmpty()) {
                new UserProfileDAO(this).save(new UserProfile(name, "", "", "", "", 0.0, 0.0, System.currentTimeMillis()));
                showCarnetCaisse();
            } else {
                Toast.makeText(this, "Veuillez entrer le nom du magasin", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Client Management ---

    private void showAjouterClient() {
        setContentView(R.layout.activity_ajouter_client);
        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> {
            tempClientName = "";
            tempClientPhone = "";
            tempClientAddress = "";
            showCarnetCredit();
        });
        
        EditText etNom = findViewById(R.id.etNom);
        EditText etPhone = findViewById(R.id.etPhone);
        TextView tvAddress = findViewById(R.id.tvAddress);
        View rlAddressDropdown = findViewById(R.id.rlAddressDropdown);
        View btnConfirmer = findViewById(R.id.btnConfirmer);
        View btnGoToTags = findViewById(R.id.btnGoToTags);

        // Restore temporary data
        if (etNom != null) etNom.setText(tempClientName);
        if (etPhone != null) etPhone.setText(tempClientPhone);
        if (tvAddress != null && !tempClientAddress.isEmpty()) tvAddress.setText(tempClientAddress);

        if (rlAddressDropdown != null) {
            rlAddressDropdown.setOnClickListener(v -> {
                // Save current inputs before navigating
                tempClientName = etNom != null ? etNom.getText().toString().trim() : "";
                tempClientPhone = etPhone != null ? etPhone.getText().toString().trim() : "";
                showAdresse();
            });
        }

        if (btnGoToTags != null) {
            btnGoToTags.setOnClickListener(v -> {
                // Save current inputs before navigating
                tempClientName = etNom != null ? etNom.getText().toString().trim() : "";
                tempClientPhone = etPhone != null ? etPhone.getText().toString().trim() : "";
                showGererTags();
            });
        }

        if (btnConfirmer != null) {
            btnConfirmer.setOnClickListener(v -> {
                String name = etNom != null ? etNom.getText().toString().trim() : "";
                String phone = etPhone != null ? etPhone.getText().toString().trim() : "";
                String address = tempClientAddress;

                if (name.isEmpty()) {
                    Toast.makeText(this, "Nom est obligatoire", Toast.LENGTH_SHORT).show();
                    return;
                }

                SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("phone", phone);
                values.put("address", address);
                db.insert("clients", null, values);

                // Reset temp data
                tempClientName = "";
                tempClientPhone = "";
                tempClientAddress = "";

                showCarnetCredit();
            });
        }
    }

    private void showAdresse() {
        setContentView(R.layout.activity_adresse);
        applyWindowInsets(findViewById(R.id.main_root));
        
        EditText etAddressInput = findViewById(R.id.etAddressInput);
        View btnValider = findViewById(R.id.btnValider);
        View btnClose = findViewById(R.id.btnClose);

        if (etAddressInput != null) etAddressInput.setText(tempClientAddress);

        if (btnClose != null) btnClose.setOnClickListener(v -> showAjouterClient());

        if (btnValider != null) {
            btnValider.setOnClickListener(v -> {
                tempClientAddress = etAddressInput != null ? etAddressInput.getText().toString().trim() : "";
                showAjouterClient();
            });
        }
    }

    private void showGererTags() {
        setContentView(R.layout.activity_gerer_tags);
        View btnClose = findViewById(R.id.btnClose);
        View btnConfirmer = findViewById(R.id.btnConfirmer);

        if (btnClose != null) btnClose.setOnClickListener(v -> showAjouterClient());
        if (btnConfirmer != null) btnConfirmer.setOnClickListener(v -> showAjouterClient());
    }

    private void loadClientsList() {
        LinearLayout container = findViewById(R.id.clientsContainer);
        TextView tvCount = findViewById(R.id.tvClientsCount);
        View emptyState = findViewById(R.id.emptyStateContainer);

        if (container == null) return;
        container.removeAllViews();

        SQLiteDatabase db = DatabaseHelper.getInstance(this).getReadableDatabase();
        Cursor cursor = db.query("clients", null, null, null, null, null, "id DESC");

        int count = cursor.getCount();
        if (tvCount != null) tvCount.setText("Clients (" + count + ")");

        if (count == 0) {
            if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
        } else {
            if (emptyState != null) emptyState.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                addClientItem(container, name, phone);
            }
        }
        cursor.close();
    }

    private void addClientItem(LinearLayout container, String name, String phone) {
        View itemView = getLayoutInflater().inflate(R.layout.item_client, container, false);
        TextView tvName = itemView.findViewById(R.id.tvClientName);
        TextView tvPhone = itemView.findViewById(R.id.tvClientPhone);
        TextView tvInitial = itemView.findViewById(R.id.tvClientInitial);

        if (tvName != null) tvName.setText(name);
        if (tvPhone != null) tvPhone.setText(phone.isEmpty() ? "" : phone);
        if (tvInitial != null && !name.isEmpty()) tvInitial.setText(name.substring(0, 1).toUpperCase());

        container.addView(itemView);
        View separator = new View(this);
        separator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        container.addView(separator);
    }

    // --- Caisse Operations ---

    private void showEntree() {
        setContentView(R.layout.activity_entree);
        applyWindowInsets(findViewById(R.id.main_root));
        
        TextView tvAmount = findViewById(R.id.tvAmount);
        View btnBack = findViewById(R.id.btnBack);
        View btnValider = findViewById(R.id.btnValider);
        
        if (tvAmount != null) tvAmount.setText(""); // Start with empty display
        
        if (btnBack != null) btnBack.setOnClickListener(v -> showCarnetCaisse());
        
        if (btnValider != null) {
            btnValider.setOnClickListener(v -> {
                String amountStr = tvAmount.getText().toString().trim();
                if (amountStr.isEmpty()) {
                    Toast.makeText(this, "Veuillez entrer un montant", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    double finalAmount = evaluate(amountStr);
                    
                    Transaction transaction = new Transaction(
                            0, 0, Transaction.Type.INCOME, finalAmount,
                            "Entrée Caisse", null, "Général",
                            System.currentTimeMillis(), false
                    );
                    
                    TransactionDAO dao = new TransactionDAO(this);
                    if (dao.insert(transaction)) {
                        Toast.makeText(this, "Enregistré : " + String.format(Locale.US, "%.2f DA", finalAmount), Toast.LENGTH_SHORT).show();
                        showCarnetCaisse();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Calcul invalide", Toast.LENGTH_SHORT).show();
                }
            });
        }

        setupKeypad(tvAmount);
    }

    private void setupKeypad(TextView tvAmount) {
        View.OnClickListener listener = v -> {
            String current = tvAmount.getText().toString().trim();
            
            if (v instanceof Button) {
                String text = ((Button) v).getText().toString();
                
                if (text.equals("AC")) {
                    tvAmount.setText("");
                } else if (text.equals("=")) {
                    try {
                        double result = evaluate(current);
                        tvAmount.setText(String.format(Locale.US, "%.2f", result));
                    } catch (Exception ignored) {}
                } else if (!text.equals("M+") && !text.equals("M-")) {
                    tvAmount.append(text);
                }
            } else if (v instanceof ImageView) {
                // Backspace
                if (current.length() > 0) {
                    tvAmount.setText(current.substring(0, current.length() - 1));
                }
            }
        };

        GridLayout keypad = findViewById(R.id.calculatorKeypad);
        if (keypad != null) {
            for (int i = 0; i < keypad.getChildCount(); i++) {
                keypad.getChildAt(i).setOnClickListener(listener);
            }
        }
    }

    private double evaluate(String expression) {
        try {
            String expr = expression.replace("×", "*").replace(",", ".");
            return new Object() {
                int pos = -1, ch;
                void nextChar() { ch = (++pos < expr.length()) ? expr.charAt(pos) : -1; }
                boolean eat(int charToEat) {
                    while (ch == ' ') nextChar();
                    if (ch == charToEat) { nextChar(); return true; }
                    return false;
                }
                double parse() { nextChar(); double x = parseExpression(); return x; }
                double parseExpression() { double x = parseTerm(); for (;;) { if (eat('+')) x += parseTerm(); else if (eat('-')) x -= parseTerm(); else return x; } }
                double parseTerm() { double x = parseFactor(); for (;;) { if (eat('*')) x *= parseFactor(); else if (eat('/')) x /= parseFactor(); else return x; } }
                double parseFactor() {
                    if (eat('+')) return parseFactor();
                    if (eat('-')) return -parseFactor();
                    double x; int startPos = this.pos;
                    if ((ch >= '0' && ch <= '9') || ch == '.') { while ((ch >= '0' && ch <= '9') || ch == '.') nextChar(); x = Double.parseDouble(expr.substring(startPos, this.pos)); }
                    else return 0;
                    return x;
                }
            }.parse();
        } catch (Exception e) { return 0; }
    }

    private void applyWindowInsets(View view) {
        if (view == null) return;
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
