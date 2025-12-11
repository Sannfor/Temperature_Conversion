package com.project1.temperatureconversion;

import com.google.android.material.snackbar.Snackbar;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etInputSuhu;
    Spinner spinSatuan;
    Button btnHitung, btnReset;
    TextView tvCelsius, tvFahrenheit, tvKelvin, tvReamur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inisialisasi Komponen
        etInputSuhu = findViewById(R.id.etInputSuhu);
        spinSatuan = findViewById(R.id.spinSatuan);
        btnHitung = findViewById(R.id.btnHitung);
        btnReset = findViewById(R.id.btnReset);

        tvCelsius = findViewById(R.id.tvCelsius);
        tvFahrenheit = findViewById(R.id.tvFahrenheit);
        tvKelvin = findViewById(R.id.tvKelvin);
        tvReamur = findViewById(R.id.tvReamur);

        // 2. Isi Pilihan Spinner (Dropdown)
        String[] pilihanSuhu = {"Celsius", "Fahrenheit", "Kelvin", "Reamur"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pilihanSuhu);
        spinSatuan.setAdapter(adapter);

        // 3. Logika Tombol Hitung
        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesKonversi();
                tutupKeyboard(); // Fitur Keyboard Otomatis
            }
        });

        // 4. Logika Tombol Reset
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etInputSuhu.setText("");
                tvCelsius.setText("Celsius: -");
                tvFahrenheit.setText("Fahrenheit: -");
                tvKelvin.setText("Kelvin: -");
                tvReamur.setText("Reamur: -");
                tutupKeyboard();
            }
        });
    }

    private void prosesKonversi() {
        String input = etInputSuhu.getText().toString();
        if (input.isEmpty()) {
            // 1. Tampilkan tanda seru merah di kolom input
            etInputSuhu.setError("Kolom ini wajib diisi!");
            etInputSuhu.requestFocus(); // Pindahkan kursor ke sini otomatis

            // 2. Tampilkan Snackbar merah di bawah layar
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Mohon masukkan angka suhu terlebih dahulu!", Snackbar.LENGTH_LONG);

            // Ubah warna background Snackbar jadi Merah (Error)
            snackbar.getView().setBackgroundColor(Color.RED);
            // Ubah warna teks jadi Putih
            snackbar.setTextColor(Color.WHITE);

            snackbar.show();

            return; // Berhenti di sini, jangan lanjut menghitung
        }

        try {
            // Ubah koma jadi titik biar aman
            double suhuAwal = Double.parseDouble(input.replace(",", "."));
            String satuanDipilih = spinSatuan.getSelectedItem().toString();

            // Konversi input APA SAJA ke Celsius dulu (Base Unit)
            double suhudlmCelsius = 0;

            if (satuanDipilih.equals("Celsius")) {
                suhudlmCelsius = suhuAwal;
            } else if (satuanDipilih.equals("Fahrenheit")) {
                suhudlmCelsius = (suhuAwal - 32) * 5/9;
            } else if (satuanDipilih.equals("Kelvin")) {
                suhudlmCelsius = suhuAwal - 273.15;
            } else if (satuanDipilih.equals("Reamur")) {
                suhudlmCelsius = suhuAwal * 5/4;
            }

            // Setelah jadi Celsius, baru hitung ke semua arah
            double hasilC = suhudlmCelsius;
            double hasilF = (suhudlmCelsius * 9/5) + 32;
            double hasilK = suhudlmCelsius + 273.15;
            double hasilR = suhudlmCelsius * 4/5;

            // Tampilkan Hasil
            tvCelsius.setText("Celsius: " + String.format("%.2f", hasilC));
            tvFahrenheit.setText("Fahrenheit: " + String.format("%.2f", hasilF));
            tvKelvin.setText("Kelvin: " + String.format("%.2f", hasilK));
            tvReamur.setText("Reamur: " + String.format("%.2f", hasilR));

        } catch (Exception e) {
            Toast.makeText(this, "Format Salah!", Toast.LENGTH_SHORT).show();
        }
    }

    // Tutup Keyboard
    private void tutupKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}