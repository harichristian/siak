package id.ac.idu.util;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 12 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class Codec {

    public enum JenisMahasiswa implements CodecInterface {
        JenMhs1("U","Umum"),
        JenMhs2("K","Khusus");

        private final String value;
        private final String label;

        private JenisMahasiswa(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

    public enum JenisKelamin implements CodecInterface {
        Jenkel1("L","Laki-Laki"),
        Jenkel12("P","Perempuan");

        private final String value;
        private final String label;

        private JenisKelamin(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

    public enum GolDarah implements CodecInterface {
        GolO("O","O"),
        GolA("A","A"),
        GolB("B","B"),
        GolAB("AB","AB");
        
        private final String value;
        private final String label;

        private GolDarah(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

    public enum Agama implements CodecInterface {
        Agama1("1","Islam"),
        Agama2("2","Khatolik"),
        Agama3("3","Kristen"),
        Agama4("4","Buddha"),
        Agama5("5","Hindu"),
        Agama6("6","Kepercayaan");

        private final String value;
        private final String label;

        private Agama(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

    public enum StatusNikah implements CodecInterface {
        Nikah1("1","Belum Kawin"),
        Nikah2("2","Kawin"),
        Nikah3("3","Janda"),
        Nikah4("4","Duda");

        private final String value;
        private final String label;

        private StatusNikah(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

    public enum WargaNegara implements CodecInterface {
        Warga1("1","WNI"),
        Warga2("2","WNA");

        private final String value;
        private final String label;

        private WargaNegara(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

    public enum YaTidak implements CodecInterface {
        YaTidak1("0","Tidak"),
        YaTidak2("1","Ya");

        private final String value;
        private final String label;

        private YaTidak(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

    public enum StatusMahasiswa implements CodecInterface {
        Status1("1", "Aktif"),
        Status2("2", "Cuti"),
        Status3("3", "Cekal");

        private final String value;
        private final String label;

        private StatusMahasiswa(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

    public enum JenisSurat implements CodecInterface {
        Status1("1", "Aktif"),
        Status2("2", "Cuti");

        private final String value;
        private final String label;

        private JenisSurat(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

    public enum StatusRuangan implements CodecInterface {
        SR1("A","Available"),
        SR12("O","Out of Order");


        private final String value;
        private final String label;

        private StatusRuangan(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

     public enum JenisInstansi implements CodecInterface {
        JI1("01","Negeri"),
        JI2("02","Swasta");


        private final String value;
        private final String label;

        private JenisInstansi(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

    public enum StatusUniversitas implements CodecInterface {
        SU1("N","Negeri"),
        SU2("S","Swasta"),
        SU3("L","Luar Negeri");


        private final String value;
        private final String label;

        private StatusUniversitas(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

     public enum KodeFeedback implements CodecInterface {
        F1("1","Feedback Instansi"),
        F2("2","Feedback Alumni"),
        F3("3","Feedback Dosen"),
        F4("3","Feedback Kuliah");


        private final String value;
        private final String label;

        private KodeFeedback(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

     public enum FlagPeminatan implements CodecInterface {
        F1("1","Aktif"),
        F2("2","Tidak Aktif");



        private final String value;
        private final String label;

        private FlagPeminatan(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

    public enum StatusAktif implements CodecInterface {
        Stat1("1", "Aktif"),
        Stat0("0", "Tidak Aktif");

        private final String value;
        private final String label;

        private StatusAktif(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }

     public enum PendidikanTerakhir implements CodecInterface {
        Stat1("1", "Sarjana"),
        Stat0("0", "Magister");

        private final String value;
        private final String label;

        private PendidikanTerakhir(String _value, String _label) {
            this.value = _value;
            this.label = _label;
        }

        public String getValue() {
            return this.value;
        }

        public String getLabel() {
            return this.label;
        }

        public String getName() {
            return this.name();
        }
    }
}
