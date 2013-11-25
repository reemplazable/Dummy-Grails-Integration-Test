package com.dummy



import org.hibernate.SessionFactory;

import grails.test.spock.IntegrationSpec;
import spock.lang.*

/**
 *
 */
class FooServiceTransactionlessSpec extends IntegrationSpec {
	
	static transactional = false;
	static final String DUMMY = "dummy"
	SessionFactory sessionFactory
	
	def FooService fooService;

    def setup() {
    }

    def cleanup() {
    }

    void "FooService saves a foo instance"() {
		given: "a foo instance"
			def foo = new Foo();
			foo.bar = DUMMY;
		
		when: "FooService saves it"
			fooService.saveFoo(foo);
			
		then: "its saved"
			Foo.findByBar(DUMMY) != null
    }
	
	void "FooService saves another foo instance with same session"() {
		given: "a foo instance"
			def transaction = sessionFactory.currentSession.beginTransaction()
			def foo = new Foo();
			foo.bar = "ymmud";
			foo.save(flush: true)
			transaction.commit()
			
			foo = new Foo();
			foo.bar = DUMMY;
		
		when: "FooService saves it"
			fooService.saveFoo(foo);
			
		then: "its saved"
			Foo.findByBar(DUMMY) != null
	}
	
	void "FooService saves another foo instance with new session"() {
		given: "a foo instance"
			def newSession = sessionFactory.openSession()
			def transaction = newSession.beginTransaction()
			def foo = new Foo();
			foo.bar = "ymmud";
			foo.save(flush: true)
			transaction.commit()
			
			foo = new Foo();
			foo.bar = DUMMY;
		
		when: "FooService saves it"
			fooService.saveFoo(foo);
			
		then: "its saved"
			Foo.findByBar(DUMMY) != null
	}
	
	void "FooService saves another foo instance with own class session"() {
		given: "a foo instance"
			Foo.withSession {
				def foo = new Foo()
				foo.bar = "ymmud"
				foo.save(flush: true)
			}
			//Cannot find the instance in DB
			//def query = sessionFactory.currentSession.createSQLQuery("Select * from foo;")
			//def resultList = query.list();
			//println resultList.empty
			
			def foo = new Foo();
			foo.bar = DUMMY;
		
		when: "FooService saves it"
			fooService.saveFoo(foo);
			
		then: "its saved"
			Foo.findByBar(DUMMY) != null
	}
	
	void "FooService saves another foo instance with new class session"() {
		given: "a foo instance"
			Foo.withNewSession {
				def foo = new Foo()
				foo.bar = "ymmud"
				foo.save(flush: true)
			}
			//def query = sessionFactory.currentSession.createSQLQuery("Select * from foo;")
			//def resultList = query.list();
			//println resultList.empty
			
			def foo = new Foo();
			foo.bar = DUMMY;
		
		when: "FooService saves it"
			fooService.saveFoo(foo);
			
		then: "its saved"
			Foo.findByBar(DUMMY) != null
	}
	
	void "FooService saves another foo instance with transaction"() {
		given: "a foo instance"
			Foo.withTransaction { transactionStatus ->
				def foo = new Foo()
				foo.bar = "ymmud"
				foo.save(flush: true)
				transactionStatus.flush()
			}
			//def query = sessionFactory.currentSession.createSQLQuery("Select * from foo;")
			//def resultList = query.list();
			//println resultList.empty
			
			def foo = new Foo();
			foo.bar = DUMMY;
		
		when: "FooService saves it"
			fooService.saveFoo(foo);
			
		then: "its saved"
			Foo.findByBar(DUMMY) != null
	}
}
