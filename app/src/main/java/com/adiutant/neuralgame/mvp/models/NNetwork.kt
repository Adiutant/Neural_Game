package com.adiutant.neuralgame.mvp.models

import com.orm.SugarRecord

class NNetwork: SugarRecord {
    var syn0:String? = null
    var syn1:String? = null
    var syn2:String? = null
    constructor(syn_zero:String?, syn_one:String?,syn_two:String?) {
        this.syn0 = syn_zero
        this.syn1 = syn_one
        this.syn2 = syn_two
    }
    constructor()

}