import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Menu extends JFrame{
    public static void main(String[] args) {
        // buat object window
        Menu window = new Menu();

        // atur ukuran window
        window.setSize(480, 560);
        // letakkan window di tengah layar
        window.setLocationRelativeTo(null);
        // isi window
        window.setContentPane(window.mainPanel);
        // ubah warna background
        window.getContentPane().setBackground(Color.white);
        // tampilkan window
        window.setVisible(true);
        // agar program ikut berhenti saat window diclose
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua mahasiswa
    private ArrayList<Mahasiswa> listMahasiswa;
    private Database database;

    private JPanel mainPanel;
    private JTextField nimField;
    private JTextField namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> jenisKelaminComboBox;
    private JComboBox<String> statusMahasiswaComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JLabel StatusMahasiswaLabel;

    // constructor
    public Menu() {
        // inisialisasi listMahasiswa
        listMahasiswa = new ArrayList<>();

        // buat objek database
        database = new Database();

        // isi tabel mahasiswa
        mahasiswaTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] jenisKelaminData = {"", "Laki-laki", "Perempuan"};
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel(jenisKelaminData));

        String[] statusMahasiswaData = {"", "Aktif", "Cuti", "Lulus", "Dropout"};
        statusMahasiswaComboBox.setModel(new DefaultComboBoxModel(statusMahasiswaData));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex == -1){
                    insertData();
                }else {
                    updateData();
                }
            }
        });
        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex >= 0){
                    int response = JOptionPane.showConfirmDialog(
                            null, "Hapus Data?", "Konfirmasi",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (response == JOptionPane.YES_OPTION) {
                        deleteData();
                    }
                }
            }
        });
        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        // saat salah satu baris tabel ditekan
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = mahasiswaTable.getSelectedRow();

                // simpan value textfield dan combo box
                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex, 2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex, 3).toString();
                String selectedStatusMahasiswa = mahasiswaTable.getModel().getValueAt(selectedIndex, 4).toString();

                // ubah isi textfield dan combo box
                nimField.setText(selectedNim);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);
                statusMahasiswaComboBox.setSelectedItem(selectedStatusMahasiswa);

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] column = {"No", "NIM", "Nama", "Jenis Kelamin", "Status Mahasiswa"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null, column);

        // isi tabel dengan listMahasiswa
        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM mahasiswa");
            int i = 0;
            while (resultSet.next()) {
                Object[] row = new Object[5];

                row[0] = i + 1;
                row[1] = resultSet.getString("nim");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getString("jenisKelamin");
                row[4] = resultSet.getString("statusMahasiswa");

                temp.addRow(row);
                // Tambahkan ke listMahasiswa
                listMahasiswa.add(new Mahasiswa(
                        resultSet.getString("nim"),
                        resultSet.getString("nama"),
                        resultSet.getString("jenisKelamin"),
                        resultSet.getString("statusMahasiswa")
                ));
                i++;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return temp;
    }

    public void insertData() {
        // ambil value dari textfield dan combobox
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String statusMahasiswa = statusMahasiswaComboBox.getSelectedItem().toString();

        // cek apakah ada input yang kosong dalam form
        if (nim.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty() || statusMahasiswa.isEmpty()){
            JOptionPane.showMessageDialog(null, "Semua field harap diisi!", "Error!", JOptionPane.ERROR_MESSAGE);
            return; // keluar jika ada field kosong
        }

        // Cek apakah NIM sudah ada di database
        try {
            ResultSet resultSet = database.selectQuery("SELECT COUNT(*) AS jumlah FROM mahasiswa WHERE nim = '" + nim + "';");
            if (resultSet.next() && resultSet.getInt("jumlah") > 0) {
                JOptionPane.showMessageDialog(null, "NIM sudah terdaftar!", "Error!", JOptionPane.ERROR_MESSAGE);
                return; // Hentikan proses insert jika NIM sudah ada
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengecek NIM!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // tambahkan data ke dalam list
        String sql = "INSERT INTO mahasiswa VALUES (null, '" + nim + "', '" + nama + "', '" + jenisKelamin + "', '" + statusMahasiswa + "');";
        database.insertUpdateDeleteQuery(sql);

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Insert Berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
    }

    public void updateData() {
        // ambil data dari form
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String statusMahasiswa = statusMahasiswaComboBox.getSelectedItem().toString();

        // cek apakah ada input yang kosong dalam form
        if (nim.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty() || statusMahasiswa.isEmpty()){
            JOptionPane.showMessageDialog(null, "Semua field harap diisi!", "Error!", JOptionPane.ERROR_MESSAGE);
            return; // keluar jika ada field kosong
        }

        // ubah data mahasiswa di list
        String sql = "UPDATE mahasiswa SET nama = '" + nama + "', jenisKelamin = '" + jenisKelamin + "', statusMahasiswa = '" + statusMahasiswa +
                     "' WHERE nim = '" + nim + "';";
        database.insertUpdateDeleteQuery(sql);

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Update Berhasil!");
        JOptionPane.showMessageDialog(null, "Data berhasil diupdate");
    }

    public void deleteData() {
        int selectedRow = mahasiswaTable.getSelectedRow(); // Ambil baris yang dipilih di JTable

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return; // Keluar jika tidak ada baris yang dipilih
        }

        String nim = mahasiswaTable.getValueAt(selectedRow, 1).toString(); // Ambil NIM dari tabel
        String namaMahasiswa = mahasiswaTable.getValueAt(selectedRow, 2).toString(); // Ambil Nama Mahasiswa

        // Konfirmasi sebelum menghapus
        int response = JOptionPane.showConfirmDialog(
                null,
                "Apakah Anda yakin ingin menghapus data mahasiswa atas nama " + namaMahasiswa + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            // Hapus data dari database
            String sql = "DELETE FROM mahasiswa WHERE nim = '" + nim + "'";
            int affectedRows = database.insertUpdateDeleteQuery(sql);

            if (affectedRows > 0) {
                mahasiswaTable.setModel(setTable()); // Refresh JTable setelah DELETE
                clearForm(); // Bersihkan form input
                JOptionPane.showMessageDialog(null, "Data mahasiswa atas nama " + namaMahasiswa + " berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menghapus data!", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedItem("");
        statusMahasiswaComboBox.setSelectedItem("");

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");
        // sembunyikan button delete
        deleteButton.setVisible(false);
        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }
    
}
