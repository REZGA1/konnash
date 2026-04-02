package com.example.konnash;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        // Language buttons listener - show the Valider button on click
        View.OnClickListener langClickListener = v -> {
            if (btnValider != null) btnValider.setVisibility(View.VISIBLE);
        };

        findViewById(R.id.btnTurkish).setOnClickListener(langClickListener);
        findViewById(R.id.btnEnglish).setOnClickListener(langClickListener);
        findViewById(R.id.btnFrench).setOnClickListener(langClickListener);
        findViewById(R.id.btnArabic).setOnClickListener(langClickListener);

        if (btnValider != null) {
            btnValider.setOnClickListener(v -> showCompleteInfo());
        }
    }

    private void showCompleteInfo() {
        setContentView(R.layout.activity_complete_info);
        applyWindowInsets(findViewById(R.id.main));

        View btnValider = findViewById(R.id.btnValidate);
        if (btnValider != null) {
            // When clicked, go to the Carnet Caisse dashboard
            btnValider.setOnClickListener(v -> showCarnetCaisse());
        }
    }

    // --- Main Dashboard Screens ---

    private void showCarnetCredit() {
        setContentView(R.layout.activity_carnet_credit);
        applyWindowInsets(findViewById(R.id.main_root));
        setupBottomNav();
        setupDashboardTabs();
        
        // Link FAB to "Ajouter Client"
        View fab = findViewById(R.id.btnFABAddClient);
        if (fab != null) fab.setOnClickListener(v -> showAjouterClient());
    }

    private void showCarnetFournisseurs() {
        setContentView(R.layout.activity_carnet_fournisseurs);
        applyWindowInsets(findViewById(R.id.main_root));
        setupBottomNav();
        setupDashboardTabs();
        
        // Link FAB to "Ajouter Fournisseur"
        View fab = findViewById(R.id.btnFABAddFournisseur);
        if (fab != null) fab.setOnClickListener(v -> showAjouterFournisseur());
    }

    private void showCarnetCaisse() {
        setContentView(R.layout.activity_carnet_caisse);
        applyWindowInsets(findViewById(R.id.main_root));
        setupBottomNav();
        
        // Navigation: ENTRÉE and SORTIE buttons
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

    // --- Sub-Screens / Form Screens ---

    private void showAjouterClient() {
        setContentView(R.layout.activity_ajouter_client);
        applyWindowInsets(findViewById(android.R.id.content)); 

        // Back Arrow and Confirmer return to Credit Dashboard
        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> showCarnetCredit());

        View btnConfirmer = findViewById(R.id.btnConfirmer);
        if (btnConfirmer != null) btnConfirmer.setOnClickListener(v -> showCarnetCredit());

        // Sub-section links
        View btnAddress = findViewById(R.id.btnGoToAddress);
        if (btnAddress != null) btnAddress.setOnClickListener(v -> showAdresse("client"));

        View btnTags = findViewById(R.id.btnGoToTags);
        if (btnTags != null) btnTags.setOnClickListener(v -> showGererTags("client"));
    }

    private void showAjouterFournisseur() {
        setContentView(R.layout.activity_ajouter_fournisseur);
        applyWindowInsets(findViewById(android.R.id.content));

        // Back Arrow and Confirmer return to Fournisseurs Dashboard
        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> showCarnetFournisseurs());

        View btnConfirmer = findViewById(R.id.btnConfirmer);
        if (btnConfirmer != null) btnConfirmer.setOnClickListener(v -> showCarnetFournisseurs());

        // Sub-section links
        View btnAddress = findViewById(R.id.btnGoToAddress);
        if (btnAddress != null) btnAddress.setOnClickListener(v -> showAdresse("fournisseur"));

        View btnTags = findViewById(R.id.btnGoToTags);
        if (btnTags != null) btnTags.setOnClickListener(v -> showGererTags("fournisseur"));
    }

    private void showAdresse(String source) {
        setContentView(R.layout.activity_adresse);
        applyWindowInsets(findViewById(R.id.main_root));

        View.OnClickListener goBackToSource = v -> {
            if ("client".equals(source)) showAjouterClient();
            else showAjouterFournisseur();
        };

        // Close 'X' and Valider return to the form
        View btnClose = findViewById(R.id.btnClose);
        if (btnClose != null) btnClose.setOnClickListener(goBackToSource);

        View btnValider = findViewById(R.id.btnValider);
        if (btnValider != null) btnValider.setOnClickListener(goBackToSource);
    }

    private void showGererTags(String source) {
        setContentView(R.layout.activity_gerer_tags);
        applyWindowInsets(findViewById(android.R.id.content));

        // Close 'X' returns to the form
        View btnClose = findViewById(R.id.btnClose);
        if (btnClose != null) btnClose.setOnClickListener(v -> {
            if ("client".equals(source)) showAjouterClient();
            else showAjouterFournisseur();
        });
    }

    private void showEntree() {
        setContentView(R.layout.activity_entree);
        applyWindowInsets(findViewById(R.id.main_root));

        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> showCarnetCaisse());

        View btnValider = findViewById(R.id.btnValider);
        if (btnValider != null) btnValider.setOnClickListener(v -> showCarnetCaisse());
    }

    private void showSortie() {
        setContentView(R.layout.activity_sortie);
        applyWindowInsets(findViewById(R.id.main_root));

        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) btnBack.setOnClickListener(v -> showCarnetCaisse());

        View btnValider = findViewById(R.id.btnValider);
        if (btnValider != null) btnValider.setOnClickListener(v -> showCarnetCaisse());
    }

    // --- Shared UI Logic Helpers ---

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
            // Nav Item 1: Carnet Credit
            navLayout.getChildAt(0).setOnClickListener(v -> showCarnetCredit());
            // Nav Item 2: Carnet Caisse
            navLayout.getChildAt(1).setOnClickListener(v -> showCarnetCaisse());
            // Nav Item 3: Profil
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