package com.fadeljoanpratama.a031_posttest4

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fadeljoanpratama.a031_posttest4.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseWarga
    private lateinit var wargaDao: WargaDao

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseWarga.getDatabase(this)
        wargaDao = db.wargaDao()

        setupSpinner()
        setupListView()

        binding.buttonSimpan.setOnClickListener {
            simpanData()
        }

        binding.buttonReset.setOnClickListener {
            resetForm()
            lifecycleScope.launch {
                wargaDao.deleteAll()
                Toast.makeText(this@MainActivity, "Semua data dihapus!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.Status,
            android.R.layout.simple_spinner_item
        ).also { spinnerAdapter ->
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.dropdown.adapter = spinnerAdapter
        }

        binding.dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@MainActivity, "Anda memilih: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun setupListView() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        binding.listWarga.adapter = adapter

        binding.listWarga.emptyView = binding.emptyView

        lifecycleScope.launch {
            wargaDao.getAllWarga().collectLatest { wargaList ->
                val displayList = wargaList.mapIndexed { index, warga ->
                    val nomor = index + 1
                    "$nomor. Nama: ${warga.nama} (${warga.jenisKelamin}) - ${warga.statusPernikahan} | NIK: ${warga.nik} | Alamat: ${warga.kabupaten}, ${warga.kecamatan}, ${warga.desa} RT ${warga.rt} / RW ${warga.rw}"
                }
                adapter.clear()
                adapter.addAll(displayList)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun simpanData() {
        val nama = binding.kolomNama.text.toString().trim()
        val nik = binding.kolomNIK.text.toString().trim()
        val kab = binding.kolomKabupaten.text.toString().trim()
        val kec = binding.kolomKecamatan.text.toString().trim()
        val desa = binding.kolomDesa.text.toString().trim()
        val rt = binding.kolomRT.text.toString().trim()
        val rw = binding.kolomRW.text.toString().trim()

        val jenisKelamin = when (binding.radiogrup.checkedRadioButtonId) {
            R.id.rdb1 -> "Laki-Laki"
            R.id.rdb2 -> "Perempuan"
            else -> {
                Toast.makeText(this, "Pilih jenis kelamin!", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val status = binding.dropdown.selectedItem?.toString()
        if (status.isNullOrEmpty()) {
            Toast.makeText(this, "Pilih status pernikahan!", Toast.LENGTH_SHORT).show()
            return
        }

        if (nama.isEmpty() || nik.isEmpty() || kab.isEmpty() || kec.isEmpty() || desa.isEmpty() || rt.isEmpty() || rw.isEmpty() || jenisKelamin.isEmpty() ||status.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        val warga = WargaEntity(
            nama = nama,
            nik = nik,
            kabupaten = kab,
            kecamatan = kec,
            desa = desa,
            rt = rt,
            rw = rw,
            jenisKelamin = jenisKelamin,
            statusPernikahan = status
        )

        lifecycleScope.launch {
            wargaDao.insert(warga)
            runOnUiThread {
                resetForm()
                Toast.makeText(this@MainActivity, "Data disimpan ke database!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetForm() {
        binding.kolomNama.text.clear()
        binding.kolomNIK.text.clear()
        binding.kolomKabupaten.text.clear()
        binding.kolomKecamatan.text.clear()
        binding.kolomDesa.text.clear()
        binding.kolomRT.text.clear()
        binding.kolomRW.text.clear()
        binding.radiogrup.clearCheck()
        binding.dropdown.setSelection(0)
    }
}