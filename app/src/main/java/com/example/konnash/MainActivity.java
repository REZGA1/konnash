package com.example.konnash;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.GridLayout;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Calendar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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

        View.OnClickListener langClickListener = v -> {
            if (btnValider != null) btnValider.setVisibility(View.VISIBLE);
            for (View btn : langButtons) {
                if (btn != null) {
                    if (btn == v) btn.setBackgroundResource(R.drawable.button_shape_selected);
                    else btn.setBackgroundResource(R.drawable.button_shape_global);
                }
            }
        };

        if (btnTurkish != null) btnTurkish.setOnClickListener(langClickListener);
        if (btnEnglish != null) btnEnglish.setOnClickListener(langClickListener);
        if (btnFrench != null) btnFrench.setOnClickListener(langClickListener);
        if (btnArabic != null) btnArabic.setOnClickListener(langClickListener);

        if (btnValider != null) {
            btnValider.setOnClickListener(v -> showCompleteInfo());
        }
    }

    private void showCompleteInfo() {
        setContentView(R.layout.activity_complete_info);
        applyWindowInsets(findViewById(R.id.main));
        View btnValider = findViewById(R.id.btnValidate);
        if (btnValider != null) btnValider.setOnClickListener(v -> showCarnetCaisse());
        
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
    }

    private void showCarnetFournisseurs() {
        setContentView(R.layout.activity_carnet_fournisseurs);
        applyWindowInsets(findViewById(R.id.main_root));
        setupBottomNav();
        setupDashboardTabs();
        
        View fab = findViewById(R.id.btnFABAddFournisseur);
        if (fab != null) fab.setOnClickListener(v -> showAjouterFournisseur());

        View btnRapports = findViewById(R.id.btnGoToRapports);
        if (btnRapports != null) btnRapports.setOnClickListener(v -> showRapports("fournisseur"));

        View btnFilter = findViewById(R.id.btnFilter);
        if (btnFilter != null) btnFilter.setOnClickListener(v -> showFiltre("fournisseur"));
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

        View btnSortie = findViewById(R.id.btnSortie);
        if (btnSortie != null) btnSortie.setOnClickListener(v -> showSortie());

        View btnHistorique = findViewById(R.id.btnHistorique);
        if (btnHistorique != null) btnHistorique.setOnClickListener(v -> showHistorique());

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

    private void showHistorique() {
        setContentView(R.layout.activity_historique);
        applyWindowInsets(findViewById(R.id.main_root));
        findViewById(R.id.btnBack).setOnClickListener(v -> showCarnetCaisse());
    }

    private void showFermerCaisse() {
        setContentView(R.layout.activity_fermer_caisse);
        applyWindowInsets(findViewById(R.id.main_root));
        findViewById(R.id.btnBack).setOnClickListener(v -> showCarnetCaisse());
        
        TextView tvSolde = findViewById(R.id.tvFermerSolde);
        TextView tvEntree = findViewById(R.id.tvFermerEntree);
        if (tvSolde != null) tvSolde.setText(getString(R.string.amount_format_da, currentSolde));
        if (tvEntree != null) tvEntree.setText(getString(R.string.amount_format_da, currentSolde));

        findViewById(R.id.btnCloseCaisse).setOnClickListener(v -> {
            currentSolde = 0;
            showCarnetCaisse();
        });
    }

    private void showEntreeDetail() {
        setContentView(R.layout.activity_entree_detail);
        applyWindowInsets(findViewById(R.id.main_root));
        findViewById(R.id.btnBack).setOnClickListener(v -> showCarnetCaisse());
    }

    private void showEntree() {
        setContentView(R.layout.activity_entree);
        applyWindowInsets(findViewById(R.id.main_root));
        findViewById(R.id.btnBack).setOnClickListener(v -> showCarnetCaisse());
        
        TextView tvAmount = findViewById(R.id.tvAmount);
        setupKeypad(tvAmount);

        findViewById(R.id.btnValider).setOnClickListener(v -> {
            String amountStr = tvAmount.getText().toString().replace(" DA", "").replace(",", ".");
            try {
                double addedAmount = Double.parseDouble(amountStr);
                currentSolde += addedAmount;
            } catch (NumberFormatException ignored) {}
            showCarnetCaisse();
        });
    }


    private void showRapports(String source) {
        if ("caisse".equals(source)) {
            setContentView(R.layout.activity_rapports_caisse);
        } else {
            setContentView(R.layout.activity_rapports);
        }
        applyWindowInsets(findViewById(R.id.main_root));

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            if ("credit".equals(source)) showCarnetCredit();
            else if ("fournisseur".equals(source)) showCarnetFournisseurs();
            else showCarnetCaisse();
        });

        View btnModifyPeriod = findViewById(R.id.btnModifyPeriod);
        if (btnModifyPeriod == null) btnModifyPeriod = findViewById(R.id.btnSelectPeriod); // activity_rapports_caisse use btnSelectPeriod
        
        if (btnModifyPeriod != null) {
            btnModifyPeriod.setOnClickListener(v -> showPeriodeRapport(source));
        }

        View tabApercu = findViewById(R.id.tabApercu);
        View tabDetails = findViewById(R.id.tabDetails);
        View layoutOverview = findViewById(R.id.layoutOverview);
        View layoutDetails = findViewById(R.id.layoutDetails);

        if (tabApercu != null && tabDetails != null) {
            tabApercu.setOnClickListener(v -> {
                if (layoutOverview != null) layoutOverview.setVisibility(View.VISIBLE);
                if (layoutDetails != null) layoutDetails.setVisibility(View.GONE);
                tabApercu.setBackgroundResource(R.drawable.button_shape_global);
                tabApercu.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFFFFF));
                ((TextView)tabApercu).setTextColor(0xFF3498DB);
                tabDetails.setBackground(null);
                ((TextView)tabDetails).setTextColor(0xFF7F8C8D);
            });
            tabDetails.setOnClickListener(v -> {
                if (layoutOverview != null) layoutOverview.setVisibility(View.GONE);
                if (layoutDetails != null) layoutDetails.setVisibility(View.VISIBLE);
                tabDetails.setBackgroundResource(R.drawable.button_shape_global);
                tabDetails.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFFFFFF));
                ((TextView)tabDetails).setTextColor(0xFF3498DB);
                tabApercu.setBackground(null);
                ((TextView)tabApercu).setTextColor(0xFF7F8C8D);
            });
        }

        TextView tvSolde = findViewById(R.id.tvSummarySolde);
        
        if (tvSolde != null) tvSolde.setText(getString(R.string.amount_format_da, currentSolde));
        
        TextView tvSummaryEntree = findViewById(R.id.tvSummaryEntree);
        if (tvSummaryEntree != null) tvSummaryEntree.setText(getString(R.string.amount_format_da, currentSolde));

        TextView tvDetailAmount = findViewById(R.id.tvDetailAmount);
        if (tvDetailAmount != null) tvDetailAmount.setText(getString(R.string.amount_format_da, currentSolde));

        TextView tvDetailSolde = findViewById(R.id.tvDetailSolde);
        if (tvDetailSolde != null) tvDetailSolde.setText(getString(R.string.solde_format_da, currentSolde));

        View btnDownload = findViewById(R.id.btnDownload);
        if (btnDownload != null) {
            btnDownload.setOnClickListener(v -> Toast.makeText(this, "Export PDF en cours...", Toast.LENGTH_SHORT).show());
        }
    }

    private void showPeriodeRapport(String source) {
        setContentView(R.layout.activity_periode_rapport);
        applyWindowInsets(findViewById(R.id.main_root));

        findViewById(R.id.btnClose).setOnClickListener(v -> showRapports(source));
        findViewById(R.id.btnApply).setOnClickListener(v -> showRapports(source));

        TextView tvStartDate = findViewById(R.id.tvStartDate);
        TextView tvEndDate = findViewById(R.id.tvEndDate);
        if (tvStartDate != null) tvStartDate.setOnClickListener(v -> showCalendar(tvStartDate));
        if (tvEndDate != null) tvEndDate.setOnClickListener(v -> showCalendar(tvEndDate));
    }

    private void showProfil() {
        setContentView(R.layout.activity_profil);
        applyWindowInsets(findViewById(R.id.main_root));
        setupBottomNav();
    }

    private void showSortie() {
        setContentView(R.layout.activity_sortie);
        applyWindowInsets(findViewById(R.id.main_root));
        findViewById(R.id.btnBack).setOnClickListener(v -> showCarnetCaisse());
        
        TextView tvAmount = findViewById(R.id.tvAmount);
        setupKeypad(tvAmount);

        findViewById(R.id.btnValider).setOnClickListener(v -> {
            String amountStr = tvAmount.getText().toString().replace(" DA", "").replace(",", ".");
            try {
                double subAmount = Double.parseDouble(amountStr);
                currentSolde -= subAmount;
            } catch (NumberFormatException ignored) {}
            showCarnetCaisse();
        });
    }

    private void showFiltre(String source) {
        setContentView(R.layout.activity_filtre);
        applyWindowInsets(findViewById(R.id.main_root));
        findViewById(R.id.btnClose).setOnClickListener(v -> {
            if ("credit".equals(source)) showCarnetCredit();
            else showCarnetFournisseurs();
        });
    }

    private void showAjouterClient() {
        setContentView(R.layout.activity_ajouter_client);
        findViewById(R.id.btnBack).setOnClickListener(v -> showCarnetCredit());
    }

    private void showAjouterFournisseur() {
        setContentView(R.layout.activity_ajouter_fournisseur);
        findViewById(R.id.btnBack).setOnClickListener(v -> showCarnetFournisseurs());
    }

    private void showCalendar(TextView targetView) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> targetView.setText(getString(R.string.date_format, dayOfMonth, (monthOfYear + 1), year1)),
                year, month, day);
        datePickerDialog.show();
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
        ViewGroup keypad = null;
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
        View btnFournisseurs = findViewById(R.id.btnTabFournisseurs);
        if (btnClients != null) btnClients.setOnClickListener(v -> showCarnetCredit());
        if (btnFournisseurs != null) btnFournisseurs.setOnClickListener(v -> showCarnetFournisseurs());
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
}