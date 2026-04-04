package com.example.konnash;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Calendar;

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

    private void showCarnetCaisse() {
        setContentView(R.layout.activity_carnet_caisse);
        applyWindowInsets(findViewById(R.id.main_root));
        setupBottomNav();
        
        View btnEntree = findViewById(R.id.btnEntree);
        if (btnEntree != null) btnEntree.setOnClickListener(v -> showEntree());

        View btnSortie = findViewById(R.id.btnSortie);
        if (btnSortie != null) btnSortie.setOnClickListener(v -> showSortie());
    }

    private void showProfil() {
        setContentView(R.layout.activity_profil);
        applyWindowInsets(findViewById(R.id.main_root));
        setupBottomNav();
    }

    // --- Filter Screen ---

    private void showFiltre(String source) {
        setContentView(R.layout.activity_filtre);
        applyWindowInsets(findViewById(R.id.main_root));

        View btnClose = findViewById(R.id.btnClose);
        View btnApply = findViewById(R.id.btnApply);
        View btnManageTags = findViewById(R.id.btnManageTags);

        View.OnClickListener goBack = v -> {
            if ("credit".equals(source)) showCarnetCredit();
            else showCarnetFournisseurs();
        };

        if (btnClose != null) btnClose.setOnClickListener(goBack);
        if (btnApply != null) btnApply.setOnClickListener(goBack);
        if (btnManageTags != null) btnManageTags.setOnClickListener(v -> showGererTags(source));

        setupFilterSelectionLogic();
    }

    private void setupFilterSelectionLogic() {
        // "Filtrer par" group
        int[] filterContainerIds = {R.id.itemFilterTout, R.id.itemFilterPris, R.id.itemFilterSolde, R.id.itemFilterDonne};
        int[] filterRadioIds = {R.id.rbFilterTout, R.id.rbFilterPris, R.id.rbFilterSolde, R.id.rbFilterDonne};

        // "Trier selon" group
        int[] sortContainerIds = {R.id.itemSortRecent, R.id.itemSortOld, R.id.itemSortAsc, R.id.itemSortDesc, R.id.itemSortAlpha};
        int[] sortRadioIds = {R.id.rbSortRecent, R.id.rbSortOld, R.id.rbSortAsc, R.id.rbSortDesc, R.id.rbSortAlpha};

        setupSelectionGroup(filterContainerIds, filterRadioIds);
        setupSelectionGroup(sortContainerIds, sortRadioIds);
    }

    private void setupSelectionGroup(int[] containerIds, int[] radioIds) {
        for (int i = 0; i < containerIds.length; i++) {
            final int index = i;
            View container = findViewById(containerIds[i]);
            if (container != null) {
                container.setOnClickListener(v -> {
                    // Update selection state for all items in the group
                    for (int j = 0; j < containerIds.length; j++) {
                        View c = findViewById(containerIds[j]);
                        RadioButton rb = findViewById(radioIds[j]);
                        if (c != null && rb != null) {
                            boolean isSelected = (j == index);
                            c.setSelected(isSelected); // Triggers blue border in selector
                            rb.setChecked(isSelected); // Fills blue dot
                        }
                    }
                });
            }
        }
    }

    // --- Rapport Screens ---

    private void showRapports(String source) {
        setContentView(R.layout.activity_rapports);
        applyWindowInsets(findViewById(R.id.main_root));

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            if ("credit".equals(source)) showCarnetCredit();
            else showCarnetFournisseurs();
        });

        findViewById(R.id.btnModifyPeriod).setOnClickListener(v -> showPeriodeRapport(source));
        findViewById(R.id.btnSort).setOnClickListener(v -> showTrierSelon(source));
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

    private void showCalendar(TextView targetView) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> targetView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1),
                year, month, day);
        datePickerDialog.show();
    }

    private void showTrierSelon(String source) {
        setContentView(R.layout.activity_trier_selon);
        applyWindowInsets(findViewById(R.id.main_root));

        findViewById(R.id.btnClose).setOnClickListener(v -> showRapports(source));
        findViewById(R.id.btnApply).setOnClickListener(v -> showRapports(source));
    }

    // --- Sub-Screens / Form Screens ---

    private void showAjouterClient() {
        setContentView(R.layout.activity_ajouter_client);
        findViewById(R.id.btnBack).setOnClickListener(v -> showCarnetCredit());
        findViewById(R.id.btnConfirmer).setOnClickListener(v -> showCarnetCredit());
        findViewById(R.id.btnGoToAddress).setOnClickListener(v -> showAdresse("client"));
        findViewById(R.id.btnGoToTags).setOnClickListener(v -> showGererTags("client"));
    }

    private void showAjouterFournisseur() {
        setContentView(R.layout.activity_ajouter_fournisseur);
        findViewById(R.id.btnBack).setOnClickListener(v -> showCarnetFournisseurs());
        findViewById(R.id.btnConfirmer).setOnClickListener(v -> showCarnetFournisseurs());
        findViewById(R.id.btnGoToAddress).setOnClickListener(v -> showAdresse("fournisseur"));
        findViewById(R.id.btnGoToTags).setOnClickListener(v -> showGererTags("fournisseur"));
    }

    private void showAdresse(String source) {
        setContentView(R.layout.activity_adresse);
        applyWindowInsets(findViewById(R.id.main_root));
        View.OnClickListener goBack = v -> {
            if ("client".equals(source)) showAjouterClient();
            else showAjouterFournisseur();
        };
        findViewById(R.id.btnClose).setOnClickListener(goBack);
        findViewById(R.id.btnValider).setOnClickListener(goBack);
    }

    private void showGererTags(String source) {
        setContentView(R.layout.activity_gerer_tags);
        findViewById(R.id.btnClose).setOnClickListener(v -> {
            if ("client".equals(source)) showAjouterClient();
            else showAjouterFournisseur();
        });
    }

    private void showEntree() {
        setContentView(R.layout.activity_entree);
        applyWindowInsets(findViewById(R.id.main_root));
        findViewById(R.id.btnBack).setOnClickListener(v -> showCarnetCaisse());
        findViewById(R.id.btnValider).setOnClickListener(v -> showCarnetCaisse());
    }

    private void showSortie() {
        setContentView(R.layout.activity_sortie);
        applyWindowInsets(findViewById(R.id.main_root));
        findViewById(R.id.btnBack).setOnClickListener(v -> showCarnetCaisse());
        findViewById(R.id.btnValider).setOnClickListener(v -> showCarnetCaisse());
    }

    // --- Helpers ---

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