package com.vaadin.starter.beveragebuddy.backend

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList

class PipedStreamUtilTest : DynaTest({
    test("PipedStreamUtil") {
        val i = PipedStreamUtil.getInputStream { o -> o.write(byteArrayOf(1, 2, 3)) }
        expectList(1, 2, 3) { i.readBytes().toList() }
    }
})
