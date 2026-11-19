import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PemesananCafe extends JFrame implements ActionListener {

    private String[] namaMenu = {"Kopi Hitam", "Kopi Susu", "Teh Manis", "Roti Bakar", "Kentang Goreng"};
    private int[] hargaMenu = {12000, 18000, 8000, 15000, 14000};

    private int total = 0;

    private JLabel lblJudul;
    private JLabel lblNama;
    private JLabel lblMenu;
    private JLabel lblJumlah;
    private JLabel lblTotal;
    private JLabel lblStatus;

    private JTextField txtNama;
    private JTextField txtJumlah;
    private JComboBox<String> cmbMenu;

    private JButton btnTambah;
    private JButton btnBayar;
    private JButton btnReset;

    private JTextArea areaPesanan;

    public PemesananCafe() {
        super("Pemesanan Cafe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        buatKomponen();
        susunTampilan();
        pasangListener();

        pack();
        setLocationRelativeTo(null); 
    }

    private void buatKomponen() {
        lblJudul = new JLabel("Cafe Senja", JLabel.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 20));

        lblNama = new JLabel("Nama pelanggan :");
        lblMenu = new JLabel("Pilih menu :");
        lblJumlah = new JLabel("Jumlah :");

        txtNama = new JTextField(15);
        txtJumlah = new JTextField("1", 15);

        cmbMenu = new JComboBox<String>();
        for (int i = 0; i < namaMenu.length; i++) {
            cmbMenu.addItem(namaMenu[i] + " - Rp " + hargaMenu[i]);
        }

        btnTambah = new JButton("Tambah Pesanan");
        btnBayar = new JButton("Bayar");
        btnReset = new JButton("Reset");

        areaPesanan = new JTextArea(8, 30);
        areaPesanan.setEditable(false); 

        lblTotal = new JLabel("Total: Rp 0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));

        lblStatus = new JLabel("Silakan isi pesanan");
    }

    private void susunTampilan() {
        JPanel panelAtas = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        panelAtas.add(lblJudul);

        JPanel panelInput = new JPanel(new GridLayout(3, 2, 5, 5)); 
        panelInput.add(lblNama);
        panelInput.add(txtNama);
        panelInput.add(lblMenu);
        panelInput.add(cmbMenu);
        panelInput.add(lblJumlah);
        panelInput.add(txtJumlah);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelTombol.add(btnTambah);
        panelTombol.add(btnBayar);
        panelTombol.add(btnReset);

        JScrollPane scrollPesanan = new JScrollPane(areaPesanan);
        scrollPesanan.setBorder(BorderFactory.createTitledBorder("Daftar pesanan"));

        JPanel panelAtasTengah = new JPanel(new BorderLayout(5, 5));
        panelAtasTengah.add(panelInput, BorderLayout.NORTH);
        panelAtasTengah.add(panelTombol, BorderLayout.CENTER);

        JPanel panelTengah = new JPanel(new BorderLayout(5, 5));
        panelTengah.add(panelAtasTengah, BorderLayout.NORTH);
        panelTengah.add(scrollPesanan, BorderLayout.CENTER);
        panelTengah.add(lblTotal, BorderLayout.SOUTH);
        panelTengah.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel panelBawah = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBawah.add(lblStatus);

        add(panelAtas, BorderLayout.NORTH);
        add(panelTengah, BorderLayout.CENTER);
        add(panelBawah, BorderLayout.SOUTH);
    }

    private void pasangListener() {
        btnTambah.addActionListener(this);
        btnBayar.addActionListener(this);
        btnReset.addActionListener(this);

        HoverHandler hover = new HoverHandler();
        btnTambah.addMouseListener(hover);
        btnBayar.addMouseListener(hover);
        btnReset.addMouseListener(hover);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object sumber = e.getSource();

        if (sumber == btnTambah) {
            tambahPesanan();
        } else if (sumber == btnBayar) {
            bayar();
        } else if (sumber == btnReset) {
            kosongkanPesanan();
            txtNama.setText("");
            lblStatus.setText("Semua isian sudah dihapus");
        }
    }

    private void tambahPesanan() {
        int jumlah;
        try {
            jumlah = Integer.parseInt(txtJumlah.getText().trim());
        } catch (NumberFormatException ex) {
            jumlah = 0;
        }

        if (jumlah <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Jumlah harus berupa angka bulat lebih dari 0.",
                    "Jumlah tidak valid",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int pilihan = cmbMenu.getSelectedIndex();
        int subtotal = hargaMenu[pilihan] * jumlah;
        total = total + subtotal;

        areaPesanan.append(jumlah + " x " + namaMenu[pilihan] + " = Rp " + subtotal + "\n");
        lblTotal.setText("Total: Rp " + total);
        lblStatus.setText(namaMenu[pilihan] + " ditambahkan");
        txtJumlah.setText("1");
    }

    private void bayar() {
        String nama = txtNama.getText().trim();

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nama pelanggan belum diisi.",
                    "Data belum lengkap",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (total == 0) {
            JOptionPane.showMessageDialog(this,
                    "Belum ada pesanan.",
                    "Pesanan kosong",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Terima kasih, " + nama + "!\nTotal pembayaran: Rp " + total,
                "Pembayaran berhasil",
                JOptionPane.INFORMATION_MESSAGE);

        kosongkanPesanan();
        txtNama.setText("");
        lblStatus.setText("Pesanan selesai, siap untuk pelanggan berikutnya");
    }

    private void kosongkanPesanan() {
        total = 0;
        areaPesanan.setText("");
        lblTotal.setText("Total: Rp 0");
        txtJumlah.setText("1");
    }

    private class HoverHandler extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            Object sumber = e.getSource();
            if (sumber == btnTambah) {
                lblStatus.setText("Menambahkan menu ke daftar pesanan");
            } else if (sumber == btnBayar) {
                lblStatus.setText("Menyelesaikan pembayaran");
            } else if (sumber == btnReset) {
                lblStatus.setText("Menghapus semua isian dan pesanan");
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            lblStatus.setText("Silakan isi pesanan");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PemesananCafe().setVisible(true);
            }
        });
    }
}
