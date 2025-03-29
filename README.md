# TP5DPBO2025C1

Saya Nina Wulandari dengan NIM 2312091 mengerjakan Tugas Praktikum 5 dalam mata kuliah DPBO untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin

# Desain Class
![Diagram TP5 drawio (1)](https://github.com/user-attachments/assets/72f6c2c0-ee83-477a-b312-36b5a7991e69)

# Desain Form
![FORM](https://github.com/user-attachments/assets/622954bb-284f-40af-b70a-7cacad44da29)


# Penjelasan Atribut dan Methods
1. Class Mahasiswa (Model) → Kelas ini merepresentasikan objek mahasiswa.
   * string nim
   * string nama
   * string jenisKelamin
   * string statusMahasiswa

     Methods:
      * getter dan setter untuk mengakses dan memodifikasi atribut mahasiswa.
    
2. Class Menu (View + Controller) → Kelas ini adalah GUI yang mewarisi JFrame
   * int selectedIndex
   * ArrayList<Mahasiswa> listMahasiswa
   * JPanel mainPanel
   * JTextField nimField
   * JTextField namaField
   * JTable mahasiswaTable
   * JButton addUpdateButton
   * JButton cancelButton
   * JComboBox<String> jenisKelaminComboBox
   * JComboBox<String> statusMahasiswaComboBox
   * JButton deleteButton
   * JLabel titleLabel
   * JLabel nimLabel
   * JLabel namaLabel
   * JLabel jenisKelaminLabel
   * JLabel StatusMahasiswaLabel
     
     Methods:
      * setTable() → membuat tabel untuk menampilkan data mahasiswa dari listMahasiswa.
     insertData() → Menambahkan data mahasiswa baru ke dalam listMahasiswa.
      * updateData() → Memperbarui data mahasiswa yang dipilih pada listMahasiswa.
      * deleteData() → Menghapus data mahasiswa dari listMahasiswa dan melakukan konfirmasi sebelum menghapus.
      * clearForm() → Mengosongkan form dan mengatur ulang tombol yaitu, "Update" menjadi "Add", invisible tombol "Delete", dan reset index.
      * populateList() → Mengisi listMahasiswa dengan contoh data mahasiswa.


 3. Class Database (Model + Data Access Layer) → Kelas ini menghubungkan Menu dengan database MySQL.
    * Connection connection → Objek yang digunakan untuk menghubungkan aplikasi ke database MySQL.
    * Statement statement → Objek yang digunakan untuk menjalankan query SQL.

        Methods:
       * selectQuery(String sql) → Menjalankan perintah SELECT pada database dan mengembalikan hasil dalam bentuk ResultSet.
       * insertUpdateDeleteQuery(String sql) → Menjalankan perintah INSERT, UPDATE, atau DELETE pada database dan mengembalikan jumlah baris yang terpengaruh.
       * getStatement(): Statement → Mengembalikan objek Statement untuk menjalankan query secara manual.


# Penjelasan Alur Program
 * Program dimulai dengan Jendela GUI yang muncul setelah di run.
 * Jendela GUI akan menampilkan form dan daftar data mahasiswa.
 * GUI ini menampilkan form input dan tabel data mahasiswa yang diambil dari database MySQL melalui kelas Database.
 * Disini saya menambahkan 1 atribut dan formnya sebagai modifikasi tugas bonus, atribut tersebut adalah statusMahasiswa yang mana menunjukkan apakah mahasiswa tersebut aktif, sedang cuti, sudah         lulus, atau dropout. Untuk formnya sendiri saya gunakan JComboBox supaya bisa dropdown pilihannya.
 * Data mahasiswa dikelola menggunakan ArrayList<Mahasiswa> listMahasiswa, yang diisi berdasarkan hasil query dari database.

Terdapat 3 fitur yang dapat dilakukan oleh user dengan alur berikut.
   1. Menambahkan Data
       * Untuk menambahkan data mahasiswa, user mengisi form dan memasukkan data mahasiswa (NIM, Nama, Jenis Kelamin, Status) kemudian klik button Add.
       * Sebelum data disimpan, program akan melakukan validasi input:
            - Jika ada field yang masih kosong, muncul dialog error yang meminta user melengkapi data terlebih dahulu.
            - Jika NIM sudah ada di database, muncul dialog error yang memberi tahu bahwa NIM tersebut telah digunakan.
       * Jika valid, Objek Mahasiswa dibuat berdasarkan input kemudian objek ini dimasukkan ke dalam database menggundakan method insertUpdateDeleteQuery()
       * Jika data berhasi ditambahkan akan muncul pop-up notifikasi sukses.
       * Data mahasiswa ditampilkan di JTable.
       * Setelah menambahkan mahasiswa, input field dikosongkan agar bisa diisi lagi.
         
   2. Mengupdate Data
       * Untuk mengupdate data, user klik salah satu baris data mahasiswa di JTable, lalu form yang semula kosong akan terisi dengan data tersebut.
       * Button Add akan berubah nama menjadi Update
       * Lakukan perubahan data yang diperlukan kemudian klik Update.
       * Sebelum data diperbarui, program akan melakukan validasi input:
            - Jika ada field yang masih kosong, muncul dialog error yang meminta user melengkapi data terlebih dahulu.
            - Jika validasi terpenuhi, perubahan data dikirim ke database menggunakan metode insertUpdateDeleteQuery().
       * Jika update berhasil akan muncul pop-up notifikasi sukses.
       * Data yang baru diupdate ditampilkan di JTable.
       * Setelah menambahkan mahasiswa, input field dikosongkan agar bisa diisi lagi.
  
  3. Menghapus Data
       * Untuk menghapus data, user klik salah satu baris data mahasiswa di JTable, lalu form yang semula kosong akan terisi dengan data tersebut.
       * Button Add akan berubah nama menjadi Update
       * Lalu klik button Delete.
       * Muncul pop-up confirmation prompt, untuk konfirmasi penghapusan data
       * Pilih Yes untuk menghapus data, pilih No untuk cancel
       * Jika penghapusan berhasil data mahasiswa yang baru dihapus diremove dari tampilan di JTable.
       * Setelah menambahkan mahasiswa, input field dikosongkan agar bisa diisi lagi.


# Dokumentasi Program
https://github.com/user-attachments/assets/9f3383af-0f17-467b-8791-f27674f9dfb6



