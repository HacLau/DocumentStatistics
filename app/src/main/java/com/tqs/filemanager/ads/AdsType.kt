package com.tqs.filemanager.ads

enum class AdsType(val adsType: String) {
    ADSOPEN("fc_launch"),
    ADSINSERTRESULTSCAN("fc_scan_int"),
    ADSINSERTRESULTCLEAN("fc_result_int"),
    ADSNATIVEMAIN("fc_main_nat"),
    ADSNATIVERESULTSCAN("fc_scan_nat"),
    ADSNATIVERESULTCLEAN("fc_result_nat"),
}