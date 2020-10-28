package com.adiutant.neuralgame.mvp.models


import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.api.ops.impl.scalar.ScalarMultiplication
import org.nd4j.linalg.api.ops.impl.scalar.ScalarSet
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.factory.Nd4j.rand

import kotlin.math.exp


class NeuralNetwork {
    var syn0: INDArray? = null
    var syn1: INDArray? = null

    var history: INDArray? = null
    var resultl1: INDArray? = null
    var resultl2: INDArray? = null
    var resultl0: INDArray? = null

//    constructor(
//        syn_zero: Array<Array<Float>>?,
//        syn_one: Array<Array<Float>>?,
//        syn_two: Array<Array<Float>>?,
//        h_history: ArrayList<Int>?
//    ) {
//        this.syn0 = syn_zero
//        this.syn1 = syn_one
//        this.syn2 = syn_two
//        this.history = h_history
//    }

    fun nonlin(x_n:INDArray, deriv:Boolean=false): INDArray {
        val rows = x_n.rows()
        val columns = x_n.columns()
        var value = 0f

         var x  = x_n
        for (i in 0 until rows)
        {
            for (j in 0 until columns)
            {
                if (deriv) {
                    value = 1 / (1 + exp(-x.getFloat(i,j))) * (1 - 1 / (1 + exp(-x.getFloat(i,j))))
                }
                else{value =  1 / (1 + exp(-x.getFloat(i,j)))}
                x.putScalar(i.toLong(), j.toLong(), value.toDouble())
            }
        }
        return x
    }

    fun predict(x: INDArray) {


        var l0: INDArray = x
        this.resultl0 = l0
        l0 = l0.mmul(this.syn0)
        var l1:INDArray = nonlin(l0,false)
        this.resultl1 = l1
        var l2: INDArray? = null
        l1 = l1.mmul(this.syn1)
        l2 = nonlin(l1,false)
        this.resultl2 = l2
        println(l2)


//        println(this.syn0?.get(0)?.get(0))
//        println(l0)
//        var n = this.syn0!!.size // количество строк
//        var m = this.syn0!![0].size
//        var result_l1: ArrayList<Float> = arrayListOf()
//        var result_l1_n: Float = 0f
//        for (i in 0 until n) {
//            for (j in 0 until m) {
//                result_l1_n += this.syn0!![i][j] * l0[j]
//            }
//            result_l1.add(result_l1_n)
//        }
//        n = result_l1.size // количество строк
//        for (i in 0 until n) {
//            result_l1[i] = nonlin(result_l1[i])
//        }
//
//        val l1: ArrayList<Float> = result_l1
//
//
//        n = this.syn1!!.size // количество строк
//        m = this.syn1!![0].size
//        var result_l2: ArrayList<Float> = arrayListOf()
//        var result_l2_n: Float = 0f
//        for (i in 0 until n) {
//            for (j in 0 until m) {
//                result_l2_n += this.syn1!![i][j] * l1[j]
//            }
//            result_l2.add(result_l2_n)
//        }
//        n = result_l2.size // количество строк
//        for (i in 0 until n) {
//            result_l2[i] = nonlin(result_l2[i])
//        }
//        val l2: ArrayList<Float> = result_l2
//
//        n = this.syn2!!.size // количество строк
//        m = this.syn2!![0].size
//        var result_l3: ArrayList<Float> = arrayListOf()
//        var result_l3_n: Float = 0f
//        for (i in 0 until n) {
//            for (j in 0 until m) {
//                result_l3_n += this.syn2!![i][j] * l2[j]
//            }
//            result_l3.add(result_l3_n)
//        }
//        n = result_l3.size // количество строк
//        for (i in 0 until n) {
//            result_l3[i] = nonlin(result_l3[i])
//        }
//
//        this.resultl3 = result_l3
//        println(result_l3)
//        this.resultl2 = result_l2
//        println(result_l2)
//        this.resultl1 = result_l1
//        println(result_l1)
    }

    fun transpose(arr: Array<Array<Float>>): Array<Array<Float>> {
        val m = arr.size
        val n: Int = arr[0].size
        val ret =
            Array(n) { Array<Float>(m) { 0f } }
        for (i in 0 until m) {
            for (j in 0 until n) {
                ret[j][i] = arr[i][j]
            }
        }
        return ret
    }

    fun transposeVec(arr: ArrayList<Float>): Array<Array<Float>> {
        val m = arr.size
        val ret = Array(m) { Array<Float>(1) { 0f } }
        for (i in 0 until m) {
            ret[i][0] = arr[i]
        }
        return ret
    }

    fun fit(x: INDArray, y: INDArray) {
        var l2_error: INDArray?= null
        l2_error = y.reshape(1,3).subi(this.resultl2)
        var l2_delta:INDArray? = l2_error
        l2_delta = nonlin(this.resultl2!!,true).mul(l2_error)
        val syn1T = this.syn1?.transpose()
        var l1_error:INDArray? = l2_delta.mmul(syn1T)
        var l1_delta:INDArray? =null
        l1_delta = nonlin(this.resultl1!!,true).mul(l1_error)
        this.syn1 = this.syn1?.add(this.resultl1?.transpose()?.mmul(l2_delta.mul(3)))
        this.syn0 = this.syn0?.add(this.resultl0?.transpose()?.mmul(l1_delta))
    }

//        if (this.resultl3 != null) {
//            val l3: ArrayList<Float> = this.resultl3!!
//            val l2: ArrayList<Float> = this.resultl2!!
//            val l1: ArrayList<Float> = this.resultl1!!
//            var l3_error: ArrayList<Float> = this.resultl3!!
//            var n = l3_error.size // количество строк
//            for (i in 0 until n) {
//                l3_error[i] = y[i] - l3[i]
//            }
//
//            var l3_delta: ArrayList<Float> = arrayListOf()
//            n = l3.size // количество строк
//            for (i in 0 until n) {
//                l3_delta.add(nonlin(l3[i], deriv = true) * l3_error[i])
//            }
//            var l2_error_n: Float = 0f
//            var l2_error: ArrayList<Float> = arrayListOf()
//            var syn2T: Array<Array<Float>> = transpose(syn2!!)
//
//            n = syn2T!!.size // количество строк
//            var m = syn2T!![0].size
//            for (i in 0 until n) {
//                for (j in 0 until m) {
//                    l2_error_n += syn2T!![i][j] * l3_delta[j]
//                }
//                l2_error.add(l2_error_n)
//            }
//            var l2_delta: ArrayList<Float> = arrayListOf()
//            n = l2_error.size
//            for (i in 0 until n) {
//                l2_delta.add(l2_error[i] * nonlin(l2[i], deriv = true))
//            }
//            ///
//            var l1_error_n: Float = 0f
//            var l1_error: ArrayList<Float> = arrayListOf()
//            var syn1T: Array<Array<Float>> = transpose(syn1!!)
//
//            n = syn1T!!.size // количество строк
//            m = syn1T!![0].size
//            for (i in 0 until n) {
//                for (j in 0 until m) {
//                    l1_error_n += syn1T!![i][j] * l2_delta[j]
//                }
//                l1_error.add(l1_error_n)
//            }
//            var l1_delta: ArrayList<Float> = arrayListOf()
//            n = l1_error.size
//            for (i in 0 until n) {
//                l1_delta.add(l1_error[i] * nonlin(l1[i], deriv = true))
//            }
//            ///
//            var l0_error_n: Float = 0f
//            var l0_error: ArrayList<Float> = arrayListOf()
//            var syn0T: Array<Array<Float>> = transpose(syn0!!)
//
//            n = syn0T!!.size // количество строк
//            m = syn0T!![0].size
//            for (i in 0 until n) {
//                for (j in 0 until m) {
//                    l0_error_n += syn0T!![i][j] * l1_delta[j]
//                }
//                l0_error.add(l0_error_n)
//            }
//            var l0_delta: ArrayList<Float> = arrayListOf()
//            n = l0_error.size
//            for (i in 0 until n) {
//                l0_delta.add(l0_error[i] * nonlin(l1[i], deriv = true))
//            }
//            ////
//            var l2T = transposeVec(l2)
//            var l2adj = Array(l2T.size) { Array<Float>(1) { 0f } }
//            n = this.syn2!!.size // количество строк
//            m = this.syn2!![0].size
//            for (i in 0 until n) {
//                for (j in 0 until m) {
//                    this.syn2!![i][j] += l2T[j][0] * l3_delta[j]
//                }
//            }
//            var l1T = transposeVec(l1)
//            var l1adj = Array(l1T.size) { Array<Float>(1) { 0f } }
//            n = this.syn1!!.size // количество строк
//            m = this.syn1!![0].size
//            for (i in 0 until n) {
//                for (j in 0 until m) {
//                    this.syn1!![i][j] += l1T[j][0] * l2_delta[j]
//                }
//            }
//            var l0T = transposeVec(l2)
//            var l0adj = Array(l0T.size) { Array<Float>(1) { 0f } }
//            n = this.syn0!!.size // количество строк
//            m = this.syn0!![0].size
//            for (i in 0 until n) {
//                for (j in 0 until m) {
//                    this.syn0!![i][j] += l0T[j][0] * l1_delta[j]
//                }
//            }


    constructor()

    fun initWeights()
    {
        this.syn0 = rand(Nd4j.create(9,12),0)
        this.syn1 = rand(Nd4j.create(12,3),0)
        var rows = this.syn0!!.rows()
        var columns = this.syn0!!.columns()
        for (i in 0 until rows)
        {
            for (j in 0 until columns) {
               this.syn0 =  this.syn0!!.putScalar(i.toLong(), j.toLong(),(-1..1).random().toDouble())
            }
        }
         rows = this.syn1!!.rows()
         columns = this.syn1!!.columns()
        for (i in 0 until rows)
        {
            for (j in 0 until columns) {
                this.syn1 =  this.syn1!!.putScalar(i.toLong(), j.toLong(),(-1..1).random().toDouble())
            }
        }


       // println(this.syn0)
        //println(this.syn1)
    }

}
