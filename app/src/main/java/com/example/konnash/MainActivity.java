package com.example.konnash;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.GridLayout;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.CheckBox;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Calendar;
import java.util.Locale;
import android.widget.Toast;

import com.example.konnash.Business_Logic.LanguageManager;
import com.example.konnash.Database.AppSettingsDAO;
import com.example.konnash.Database.UserProfileDAO;
import com.example.konnash.Database.TagDAO;
import com.example.konnash.Model.AppSettings;
import com.example.konnash.Model.UserProfile;
import com.example.konnash.Model.Tag;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String selectedLanguageCode = LanguageManager.FRENCH; // Default to French

    // Temporary storage for client form data
    private String tempClientName = "";
    private String tempClientPhone = "";
    private String tempClientAddress = "";
    private List<Tag> selectedTags = new ArrayList<>(); // Temporary selected tags
    private List<Tag> allTags = new ArrayList<>(); // All available tags

    @Override
    protected void attachBaseContext(Context newBase) {
        // Load saved language from database
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
        
        // Start with the Language Selection screen (activity_main)
        showLanguageSelection();
    }

    // --- Onboarding Screens ---

    private void showLanguageSelection() {
        setContentView(R.layout.activity_main);
        applyWindowInsets(findViewById(R.id.main));

        View btnValider = findViewById(R.id.btnValiderLanguage);
        View btnTurkish = findViewById(R.id.btnTurkish);
        View btnEnglish = findViewById(R.id.btnEnglish);
        View btnFrench = findViewById(R.id.btnFrench);
        View btnArabic = findViewById(R.id.btnArabic);

        View[] langButtons = {btnTurkish, btnEnglish, btnFrench, btnArabic};

        // Language selection click listener
        if (btnTurkish != null) {
            btnTurkish.setOnClickListener(v -> {
                selectedLanguageCode = LanguageManager.TURKISH;
                if (btnValider != null) btnValider.setVisibility(View.VISIBLE);
                updateButtonStyles(langButtons, btnTurkish);
            });
        }
        if (btnEnglish != null) {
            btnEnglish.setOnClickListener(v -> {
                selectedLanguageCode = LanguageManager.ENGLISH;
                if (btnValider != null) btnValider.setVisibility(View.VISIBLE);
                updateButtonStyles(langButtons, btnEnglish);
            });
        }
        if (btnFrench != null) {
            btnFrench.setOnClickListener(v -> {
                selectedLanguageCode = LanguageManager.FRENCH;
                if (btnValider != null) btnValider.setVisibility(View.VISIBLE);
                updateButtonStyles(langButtons, btnFrench);
            });
        }
        if (btnArabic != null) {
            btnArabic.setOnClickListener(v -> {
                selectedLanguageCode = LanguageManager.ARABIC;
                if (btnValider != null) btnValider.setVisibility(View.VISIBLE);
                updateButtonStyles(langButtons, btnArabic);
            });
        }

        if (btnValider != null) {
            btnValider.setOnClickListener(v -> {
                // Save the selected language to database
                AppSettingsDAO settingsDAO = new AppSettingsDAO(this);
                AppSettings settings = new AppSettings(selectedLanguageCode, null);
                settingsDAO.save(settings);
                
                // Apply the language immediately and show complete info
                showCompleteInfo();
            });
        }
    }

    private void updateButtonStyles(View[] buttons, View selected) {
        for (View btn : buttons) {
            if (btn != null) {
                if (btn == selected) btn.setBackgroundResource(R.drawable.button_shape_selected);
                else btn.setBackgroundResource(R.drawable.button_shape_global);
            }
        }
    }

    private void showCompleteInfo() {
        setContentView(R.layout.activity_complete_info);
        applyWindowInsets(findViewById(R.id.main));
        
        EditText etPointOfSale = findViewById(R.id.etPointOfSale);
        
        View btnValider = findViewById(R.id.btnValidate);
        if (btnValider != null) {
            btnValider.setOnClickListener(v -> {
                // Save the point of sale name to database
                String storeName = etPointOfSale != null ? etPointOfSale.getText().toString().trim() : "";
                if (!storeName.isEmpty()) {
                    UserProfileDAO userDAO = new UserProfileDAO(this);
                    UserProfile existingProfile = userDAO.get();
                    if (existingProfile != null) {
                        existingProfile.setStoreName(storeName);
                        userDAO.save(existingProfile);
                    } else {
                        // Create new profile with store name
                        UserProfile newProfile = new UserProfile(
                            storeName, "", "", "", "", 0.0, 0.0, System.currentTimeMillis()
                        );
                        userDAO.save(newProfile);
                    }
                }
                showCarnetCaisse();
            });
        }
        
        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> showLanguageSelection());
    }

    // --- Main Dashboard Screens ---

    private void showCarnetCredit() {
        setContentView(R.layout.activity_carnet_credit);
        applyWindowInsets(findViewById(R.id.main_root));
        setupBottomNav();
        setupDashboardTabs();
        
        View fab = findViewById(R.id.btnFABAddClient);
        if (fab != null) fab.setOnClickListener(v -> showAjouterClient());

        View btnRapports = findViewById(R.id.btnGoToRapports);
        if (btnRapports != null) btnRapports.setOnClickListener(v -> showRapports("credit"));

        View btnFilter = findViewById(R.id.btnFilter);
        if (btnFilter != null) btnFilter.setOnClickListener(v -> showFiltre("credit"));

        loadPersons("clients", R.id.clientsContainer, R.id.tvClientsCount, "Clients");
    }

    private double currentSolde = 0.0;

    private void showCarnetCaisse() {
        if (currentSolde != 0) {
            setContentView(R.layout.activity_carnet_caisse_data);
        } else {
            setContentView(R.layout.activity_carnet_caisse);
        }
        applyWindowInsets(findViewById(R.id.main_root));
        setupBottomNav();

        // Load and display the saved store name
        TextView tvShopName = findViewById(R.id.tvShopName);
        if (tvShopName != null) {
            UserProfileDAO userDAO = new UserProfileDAO(this);
            UserProfile profile = userDAO.get();
            if (profile != null && profile.getStoreName() != null && !profile.getStoreName().isEmpty()) {
                tvShopName.setText(profile.getStoreName());
            } else {
                tvShopName.setText(R.string.shop_name);
            }
        }

        TextView tvSolde = findViewById(R.id.tvTotalSolde);
        if (tvSolde != null) tvSolde.setText(getString(R.string.amount_format_da, currentSolde));

        TextView tvTotalEntrees = findViewById(R.id.tvTotalEntrees);
        if (tvTotalEntrees != null) tvTotalEntrees.setText(getString(R.string.amount_format_da, currentSolde));

        TextView tvItemAmount = findViewById(R.id.tvItemAmount);
        if (tvItemAmount != null) tvItemAmount.setText(getString(R.string.amount_format_da, currentSolde));

        TextView tvItemSolde = findViewById(R.id.tvItemSolde);
        if (tvItemSolde != null) tvItemSolde.setText(getString(R.string.solde_format_da, currentSolde));

        View btnEntree = findViewById(R.id.btnEntree);
        if (btnEntree != null) btnEntree.setOnClickListener(v -> showEntree());

        // btnHistorique is now static - no click handler

        View btnRapports = findViewById(R.id.btnRapports);
        if (btnRapports != null) btnRapports.setOnClickListener(v -> showRapports("caisse"));

        View btnFermer = findViewById(R.id.btnFermer);
        if (btnFermer != null) btnFermer.setOnClickListener(v -> showFermerCaisse());

        View itemOperation = findViewById(R.id.layoutOperationItem);
        if (itemOperation != null) itemOperation.setOnClickListener(v -> showEntreeDetail());

        View btnDownload = findViewById(R.id.btnDownload);
        if (btnDownload != null) {
            btnDownload.setOnClickListener(v -> Toast.makeText(this, "Export PDF en cours...", Toast.LENGTH_SHORT).show());
        }
    }

    private void showAjouterClient() {
        setContentView(R.layout.activity_ajouter_client);
        findViewById(R.id.btnBack).setOnClickListener(v -> showCarnetCredit());

        android.widget.EditText etNom = findViewById(R.id.etNom);
        android.widget.EditText etPhone = findViewById(R.id.etPhone);
        android.widget.TextView tvAddress = findViewById(R.id.tvAddress);
        View rlAddressDropdown = findViewById(R.id.rlAddressDropdown);
        View btnConfirmer = findViewById(R.id.btnConfirmer);
        // View btnGererTags = findViewById(R.id.btnGererTags); // TODO: Add btnGererTags to layout
        
        // Restore temporary data
        if (etNom != null) etNom.setText(tempClientName);
        if (etPhone != null) etPhone.setText(tempClientPhone);
        if (tvAddress != null) tvAddress.setText(tempClientAddress.isEmpty() ? "" : tempClientAddress);
        
        /* TODO: Add btnGererTags to layout first
        // Handle Gérer les tags button
        if (btnGererTags != null) {
            btnGererTags.setOnClickListener(v -> {
                // Save current form data before navigating
                tempClientName = etNom != null ? etNom.getText().toString().trim() : "";
                tempClientPhone = etPhone != null ? etPhone.getText().toString().trim() : "";
                showGererTags();
            });
        }
        */
        
        // Display selected tags in the form
        // displaySelectedTags(); // TODO: Re-enable when btnGererTags is added
        
        // Make address dropdown clickable - navigate to address screen
        if (rlAddressDropdown != null) {
            rlAddressDropdown.setOnClickListener(v -> {
                // Save current values before navigating
                tempClientName = etNom != null ? etNom.getText().toString().trim() : "";
                tempClientPhone = etPhone != null ? etPhone.getText().toString().trim() : "";
                showAdresse();
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
                if (!phone.isEmpty() && phone.length() != 9) {
                    Toast.makeText(this, "Le numéro de téléphone doit contenir 9 chiffres", Toast.LENGTH_SHORT).show();
                    return;
                }

                android.database.sqlite.SQLiteDatabase db = com.example.konnash.Database.DatabaseHelper.getInstance(this).getWritableDatabase();
                android.content.ContentValues values = new android.content.ContentValues();
                values.put("name", name);
                values.put("phone", phone);
                values.put("address", address);
                db.insert("clients", null, values);

                // Clear temporary data
                tempClientName = "";
                tempClientPhone = "";
                tempClientAddress = "";
                
                Toast.makeText(this, "Client ajouté", Toast.LENGTH_SHORT).show();
                showCarnetCredit();
            });
        }
    }
    
    private void showAdresse() {
        setContentView(R.layout.activity_adresse);
        applyWindowInsets(findViewById(R.id.main_root));
        
        EditText etAddress = findViewById(R.id.etAddressInput);
        View btnValider = findViewById(R.id.btnValider);
        
        // Pre-fill with current address if any
        if (etAddress != null) etAddress.setText(tempClientAddress);
        
        // Close button returns without saving
        View btnClose = findViewById(R.id.btnClose);
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> showAjouterClient());
        }
        
        // Validate button saves address and returns
        if (btnValider != null) {
            btnValider.setOnClickListener(v -> {
                if (etAddress != null) {
                    tempClientAddress = etAddress.getText().toString().trim();
                }
                showAjouterClient();
            });
        }
    }

    private void showGererTags() {
        setContentView(R.layout.activity_gerer_tags);
        applyWindowInsets(findViewById(R.id.main_root));

        View btnClose = findViewById(R.id.btnClose);
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> showAjouterClient());
        }

        View btnNewTag = findViewById(R.id.btnNewTag);
        if (btnNewTag != null) {
            btnNewTag.setOnClickListener(v -> showNouveauTag());
        }

        View btnConfirmer = findViewById(R.id.btnConfirmer);
        if (btnConfirmer != null) {
            btnConfirmer.setOnClickListener(v -> {
                // Save selected tags and return
                showAjouterClient();
            });
        }

        // Load and display tags
        loadTagsList();
    }

    private void loadTagsList() {
        LinearLayout tagsContainer = findViewById(R.id.tagsContainer);
        View emptyState = findViewById(R.id.emptyState);
        
        if (tagsContainer == null) return;

        // Clear existing views except empty state
        tagsContainer.removeAllViews();

        // Load tags from database
        TagDAO tagDAO = new TagDAO(this);
        allTags = tagDAO.getAllTags();

        if (allTags.isEmpty()) {
            // Show empty state
            if (emptyState != null) {
                tagsContainer.addView(emptyState);
            }
            return;
        }

        // Add tag items
        for (Tag tag : allTags) {
            addTagItemToContainer(tagsContainer, tag);
        }
    }

    private void addTagItemToContainer(LinearLayout container, Tag tag) {
        // Create tag item layout programmatically
        LinearLayout tagItem = new LinearLayout(this);
        tagItem.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        tagItem.setOrientation(LinearLayout.HORIZONTAL);
        tagItem.setPadding(0, 16, 0, 16);

        // Checkbox
        CheckBox checkBox = new CheckBox(this);
        checkBox.setId((int) tag.getId());
        checkBox.setChecked(isTagSelected(tag));
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!isTagSelected(tag)) selectedTags.add(tag);
            } else {
                selectedTags.removeIf(t -> t.getId() == tag.getId());
            }
        });
        tagItem.addView(checkBox);

        // Tag name with background
        TextView tvTagName = new TextView(this);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        nameParams.setMargins(16, 0, 16, 0);
        tvTagName.setLayoutParams(nameParams);
        tvTagName.setText(tag.getName());
        tvTagName.setPadding(16, 8, 16, 8);
        tvTagName.setBackgroundColor(android.graphics.Color.parseColor(tag.getColor()));
        tvTagName.setTextColor(android.graphics.Color.WHITE);
        tagItem.addView(tvTagName);

        // Stats text
        TextView tvStats = new TextView(this);
        tvStats.setText(tag.getClientCount() + " Client • " + tag.getFournisseurCount() + " Fournisseur");
        tvStats.setTextColor(0xFF7F8C8D);
        tvStats.setTextSize(12);
        tagItem.addView(tvStats);

        container.addView(tagItem);
    }

    private boolean isTagSelected(Tag tag) {
        for (Tag t : selectedTags) {
            if (t.getId() == tag.getId()) return true;
        }
        return false;
    }

    private void showNouveauTag() {
        setContentView(R.layout.activity_nouveau_tag);
        applyWindowInsets(findViewById(R.id.main_root));

        EditText etTagName = findViewById(R.id.etTagName);
        View btnColorPicker = findViewById(R.id.btnColorPicker);
        View btnConfirmer = findViewById(R.id.btnConfirmer);
        View btnClose = findViewById(R.id.btnClose);

        // Default color
        final String[] selectedColor = {"#3498DB"};

        if (btnColorPicker != null) {
            btnColorPicker.setOnClickListener(v -> {
                // Simple color picker - cycle through preset colors
                String[] colors = {"#3498DB", "#E74C3C", "#2ECC71", "#F39C12", "#9B59B6", "#1ABC9C"};
                int currentIndex = 0;
                for (int i = 0; i < colors.length; i++) {
                    if (colors[i].equals(selectedColor[0])) {
                        currentIndex = (i + 1) % colors.length;
                        break;
                    }
                }
                selectedColor[0] = colors[currentIndex];
                btnColorPicker.setBackgroundColor(android.graphics.Color.parseColor(selectedColor[0]));
            });
        }

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> showGererTags());
        }

        if (btnConfirmer != null) {
            btnConfirmer.setOnClickListener(v -> {
                String tagName = etTagName != null ? etTagName.getText().toString().trim() : "";
                if (tagName.isEmpty()) {
                    Toast.makeText(this, "Nom du tag est obligatoire", Toast.LENGTH_SHORT).show();
                    return;
                }

                TagDAO tagDAO = new TagDAO(this);
                if (tagDAO.tagExists(tagName)) {
                    Toast.makeText(this, "Ce tag existe déjà", Toast.LENGTH_SHORT).show();
                    return;
                }

                Tag newTag = new Tag(tagName, selectedColor[0]);
                tagDAO.addTag(newTag);
                Toast.makeText(this, "Tag ajouté", Toast.LENGTH_SHORT).show();
                showGererTags();
            });
        }
    }

    private void displaySelectedTags() {
        LinearLayout tagsContainer = findViewById(R.id.tagsContainer);
        if (tagsContainer == null) return;
        
        tagsContainer.removeAllViews();
        
        for (Tag tag : selectedTags) {
            TextView tvTag = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 16, 0);
            tvTag.setLayoutParams(params);
            tvTag.setText(tag.getName());
            tvTag.setPadding(16, 8, 16, 8);
            tvTag.setBackgroundColor(android.graphics.Color.parseColor(tag.getColor()));
            tvTag.setTextColor(android.graphics.Color.WHITE);
            tvTag.setTextSize(14);
            tagsContainer.addView(tvTag);
        }
    }

    private void showRapports(String source) {
        if ("caisse".equals(source)) {
            setContentView(R.layout.activity_rapports_caisse);
        } else {
            setContentView(R.layout.activity_rapports);
        }
        applyWindowInsets(findViewById(R.id.main_root));

        View btnFilter = findViewById(R.id.btnFilter);
        if (btnFilter != null) btnFilter.setOnClickListener(v -> showFiltre("credit"));

        loadPersons("clients", R.id.clientsContainer, R.id.tvClientsCount, "Clients");
    }

    private void showFiltre(String source) {
        // TODO: Implement date filter functionality
        Toast.makeText(this, "Filtre: " + source, Toast.LENGTH_SHORT).show();
    }

    // --- Helpers ---

    private void setupKeypad(TextView tvAmount) {
        View.OnClickListener listener = v -> {
            if (v instanceof Button) {
                String text = ((Button) v).getText().toString();
                String current = tvAmount.getText().toString().replace(" DA", "").replace(",", ".");
                
                if (text.equals("AC")) {
                    tvAmount.setText(R.string.default_amount);
                } else if (text.equals("=")) {
                    try {
                        double result = evaluate(current);
                        tvAmount.setText(getString(R.string.amount_format_da, result));
                    } catch (Exception ignored) {}
                } else if (text.equals("+") || text.equals("-") || text.equals("×") || text.equals("/")) {
                    tvAmount.setText(getString(R.string.amount_operator_format, current, text));
                } else {
                    if (current.equals("0.00") || current.equals("0")) {
                        tvAmount.setText(getString(R.string.amount_simple_format, text));
                    } else {
                        tvAmount.setText(getString(R.string.amount_simple_format, current + text));
                    }
                }
            }
        };

        // Find all buttons in the GridLayout and attach listener
        ViewGroup keypad = findViewById(R.id.calculatorKeypad);
        if (keypad == null) {
            // Fallback: search for GridLayout in the view hierarchy
            View root = findViewById(R.id.main_root);
            if (root instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) root;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    if (child instanceof LinearLayout) {
                        ViewGroup ll = (ViewGroup) child;
                        for (int j = 0; j < ll.getChildCount(); j++) {
                            if (ll.getChildAt(j) instanceof GridLayout) {
                                keypad = (GridLayout) ll.getChildAt(j);
                                break;
                            }
                        }
                    }
                }
            }
        }

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
                double parse() { nextChar(); double x = parseExpression(); if (pos < expr.length()) throw new RuntimeException("Unexpected: " + (char)ch); return x; }
                double parseExpression() { double x = parseTerm(); for (;;) { if (eat('+')) x += parseTerm(); else if (eat('-')) x -= parseTerm(); else return x; } }
                double parseTerm() { double x = parseFactor(); for (;;) { if (eat('*')) x *= parseFactor(); else if (eat('/')) x /= parseFactor(); else return x; } }
                double parseFactor() {
                    if (eat('+')) return parseFactor();
                    if (eat('-')) return -parseFactor();
                    double x; int startPos = this.pos;
                    if (eat('(')) { x = parseExpression(); eat(')'); }
                    else if ((ch >= '0' && ch <= '9') || ch == '.') { while ((ch >= '0' && ch <= '9') || ch == '.') nextChar(); x = Double.parseDouble(expr.substring(startPos, this.pos)); }
                    else throw new RuntimeException("Unexpected: " + (char)ch);
                    return x;
                }
            }.parse();
        } catch (Exception e) { return 0; }
    }

    private void setupDashboardTabs() {
        View btnClients = findViewById(R.id.btnTabClients);
        if (btnClients != null) btnClients.setOnClickListener(v -> showCarnetCredit());
        // Fournisseurs tab is now static - no click handler
    }

    private void setupBottomNav() {
        View nav = findViewById(R.id.bottom_navigation);
        if (nav instanceof LinearLayout) {
            LinearLayout navLayout = (LinearLayout) nav;
            navLayout.getChildAt(0).setOnClickListener(v -> showCarnetCredit());
            navLayout.getChildAt(1).setOnClickListener(v -> showCarnetCaisse());
            navLayout.getChildAt(2).setOnClickListener(v -> showProfil());
        }
    }

    private void applyWindowInsets(View view) {
        if (view == null) return;
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void loadPersons(String table, int containerId, int countTextViewId, String labelPrefix) {
        LinearLayout container = findViewById(containerId);
        if (container == null) return;
        container.removeAllViews();
        
        // Get parent ScrollView and empty state container
        View scrollView = (View) container.getParent();
        View emptyState = null;
        if (containerId == R.id.clientsContainer) {
            emptyState = findViewById(R.id.emptyStateContainer);
        }
        
        android.database.sqlite.SQLiteDatabase db = com.example.konnash.Database.DatabaseHelper.getInstance(this).getReadableDatabase();
        android.database.Cursor cursor = db.query(table, new String[]{"name", "phone"}, null, null, null, null, "id DESC");
        
        int count = 0;
        while (cursor.moveToNext()) {
            count++;
            String name = cursor.getString(0);
            String phone = cursor.getString(1);
            
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            itemLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
            itemLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);
            
            TextView tvIcon = new TextView(this);
            tvIcon.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(45), dpToPx(45)));
            tvIcon.setBackgroundResource(R.drawable.bg_light_blue_btn);
            tvIcon.setGravity(android.view.Gravity.CENTER);
            tvIcon.setText(name.length() > 0 ? name.substring(0, 1).toUpperCase() : "U");
            tvIcon.setTextColor(0xFF333333);
            tvIcon.setTypeface(null, android.graphics.Typeface.BOLD);
            
            LinearLayout centerLayout = new LinearLayout(this);
            centerLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            params.setMargins(dpToPx(16), 0, 0, 0);
            centerLayout.setLayoutParams(params);
            
            TextView tvName = new TextView(this);
            tvName.setText(name);
            tvName.setTextColor(0xFF333333);
            tvName.setTypeface(null, android.graphics.Typeface.BOLD);
            
            TextView tvSubtitle = new TextView(this);
            tvSubtitle.setText("Aujourd'hui" + (phone.isEmpty() ? "" : " - " + phone));
            tvSubtitle.setTextSize(12);
            tvSubtitle.setTextColor(0xFFBDC3C7);
            
            centerLayout.addView(tvName);
            centerLayout.addView(tvSubtitle);
            
            LinearLayout rightLayout = new LinearLayout(this);
            rightLayout.setOrientation(LinearLayout.VERTICAL);
            rightLayout.setGravity(android.view.Gravity.END);
            
            TextView tvAmount = new TextView(this);
            tvAmount.setText("0.0 DA");
            tvAmount.setTextColor(0xFFE74C3C);
            tvAmount.setTypeface(null, android.graphics.Typeface.BOLD);
            
            TextView tvGive = new TextView(this);
            tvGive.setText("J'ai donné");
            tvGive.setTextSize(10);
            tvGive.setTextColor(0xFFBDC3C7);
            
            rightLayout.addView(tvAmount);
            rightLayout.addView(tvGive);
            
            itemLayout.addView(tvIcon);
            itemLayout.addView(centerLayout);
            itemLayout.addView(rightLayout);
            
            final String finalName = name;
            itemLayout.setOnClickListener(v -> showClientDetails(finalName, table.equals("fournisseurs")));
            
            container.addView(itemLayout);
            
            View separator = new View(this);
            separator.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(1)));
            separator.setBackgroundColor(0xFFEEEEEE);
            container.addView(separator);
        }
        cursor.close();
        
        // Show/hide empty state
        if (emptyState != null) {
            if (count == 0) {
                emptyState.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            } else {
                emptyState.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        }
        
        TextView tvCount = findViewById(countTextViewId);
        if (tvCount != null) {
            tvCount.setText(labelPrefix + " (" + count + ")");
        }
    }

    private void showEntree() {
        // TODO: Implement entry screen
        Toast.makeText(this, "Entrée", Toast.LENGTH_SHORT).show();
    }

    private void showFermerCaisse() {
        // TODO: Implement close cash register screen
        Toast.makeText(this, "Fermer Caisse", Toast.LENGTH_SHORT).show();
    }

    private void showEntreeDetail() {
        // TODO: Implement entry detail screen
        Toast.makeText(this, "Détail Entrée", Toast.LENGTH_SHORT).show();
    }

    private void showProfil() {
        // TODO: Implement profile screen
        Toast.makeText(this, "Profil", Toast.LENGTH_SHORT).show();
    }

    private void showClientDetails(String name, boolean isFournisseur) {
        // TODO: Implement client details screen
        Toast.makeText(this, (isFournisseur ? "Fournisseur: " : "Client: ") + name, Toast.LENGTH_SHORT).show();
    }

}


