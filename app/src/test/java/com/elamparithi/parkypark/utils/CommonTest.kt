package com.elamparithi.parkypark.utils

import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase

class CommonTest : TestCase() {

    fun testGetTotalHours() {
       val totalHours = Common.getTotalHours(1642404450, 1642414450)
        assertThat(totalHours).isEqualTo(3)
    }
}