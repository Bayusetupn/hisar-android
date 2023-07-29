package com.example.hisar.admin
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.hisar.api.ApiClient
import com.example.hisar.data.Kota
import com.example.hisar.data.Provinsi
import com.example.hisar.databinding.ActivityTambahAgenBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahAgenAcitvity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahAgenBinding
    private var data: List<Provinsi.ProvinsiItem> = emptyList()
    private var kota: List<Kota.Kabupaten> = emptyList()
    private var alamat: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahAgenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProvinsi()

        for (i in 0..7){
            alamat.add(i,"")
        }

        binding.kecamatan.addTextChangedListener {
            alamat[2] = it.toString()
            binding.full.text = "${alamat[6]}, Rt ${alamat[5]}/Rw ${alamat[4]} ,${alamat[3]} ,Kecamatan ${alamat[2]}, ${alamat[1]}, ${alamat[0]}."
        }
        binding.kelurahan.addTextChangedListener {
            alamat[3] = it.toString()
            binding.full.text = "${alamat[6]}, Rt ${alamat[5]}/Rw ${alamat[4]} ,${alamat[3]} ,Kecamatan ${alamat[2]}, ${alamat[1]}, ${alamat[0]}."
        }
        binding.rw.addTextChangedListener {
            alamat[4] = it.toString()
            binding.full.text = "${alamat[6]}, Rt ${alamat[5]}/Rw ${alamat[4]} ,${alamat[3]} ,Kecamatan ${alamat[2]}, ${alamat[1]}, ${alamat[0]}."
        }
        binding.rt.addTextChangedListener {
            alamat[5] = it.toString()
            binding.full.text = "${alamat[6]}, Rt ${alamat[5]}/Rw ${alamat[4]} ,${alamat[3]} ,Kecamatan ${alamat[2]}, ${alamat[1]}, ${alamat[0]}."
        }


    }

    private fun setSpinnerData(){
        val item = data.map { it.nama }
        val adapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_dropdown_item,item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.provinsi.adapter = adapter

    }

    private fun setSpinnerKota(){
        val item = kota.map { it.nama }
        val adapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_dropdown_item,item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.kota.adapter = adapter
    }

    private fun getKota(id: Int,){
        ApiClient.alamat.getKota(id)
            .enqueue(object : Callback<Kota>{
                override fun onResponse(call: Call<Kota>, response: Response<Kota>) {
                    if (response.isSuccessful){
                        kota = response.body()?.kotaKabupaten?: Kota.Kabupaten("Pilih Kabupaten/Kota") as List<Kota.Kabupaten>
                        setSpinnerKota()
                        binding.kota.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                               alamat[1] =  p0?.getItemAtPosition(p2).toString()
                                binding.full.text = "${alamat[6]}, Rt ${alamat[5]}/Rw ${alamat[4]} ,${alamat[3]} ,Kecamatan ${alamat[2]}, ${alamat[1]}, ${alamat[0]}."
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }

                        }
                    }
                }

                override fun onFailure(call: Call<Kota>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }
    private fun getProvinsi(){
        ApiClient.alamat.getProvinsi()
            .enqueue(object: Callback<Provinsi>{
                override fun onResponse(call: Call<Provinsi>, response: Response<Provinsi>) {
                    if (response.isSuccessful){
                        data = (response.body()?.provinsi ?: Provinsi.ProvinsiItem("Pilih Provinsi")) as List<Provinsi.ProvinsiItem>
                        setSpinnerData()
                        binding.provinsi.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                binding.full.text = "${alamat[6]}, Rt ${alamat[5]}/Rw ${alamat[4]} ,${alamat[3]} ,Kecamatan ${alamat[2]}, ${alamat[1]}, ${alamat[0]}."
                                data[p2].nama?.let { alamat.set(0, it) }
                                data[p2].id?.let { getKota(it) }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {

                            }
                        }
                    }

                }

                override fun onFailure(call: Call<Provinsi>, t: Throwable) {
                    Toast.makeText(applicationContext,"Server Error!",Toast.LENGTH_SHORT).show()
                }

            })
    }


}
