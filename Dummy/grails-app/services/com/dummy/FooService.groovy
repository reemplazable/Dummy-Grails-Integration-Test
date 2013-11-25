package com.dummy

import grails.transaction.Transactional

@Transactional
class FooService {

    def saveFoo(Foo foo) {
		foo.save();
    }
}
