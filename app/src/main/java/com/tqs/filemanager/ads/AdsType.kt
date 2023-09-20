package com.tqs.filemanager.ads

enum class AdsItemType(val adsType: String) {
    ADSFULLSCREEN("fc_launch"),
    ADSINSERTRESULTSCAN("fc_scan_int"),
    ADSINSERTRESULTCLEAN("fc_result_int"),
    ADSNATIVEMAIN("fc_main_nat"),
    ADSNATIVERESULTSCAN("fc_scan_nat"),
    ADSNATIVERESULTCLEAN("fc_result_nat"),
}
object ADSType{
    const val ADS_TYPE_OP = "op"
    const val ADS_TYPE_INT = "int"
    const val ADS_TYPE_NAT = "nat"
}