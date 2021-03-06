namespace Test/Exp/NullMatch {

    @test
    def testUnitNullMatch01(): Bool =
        match? () {
          case null => false
          case _    => true
        }

    @test
    def testStringNullMatch01(): Bool =
        match? "Hello World!" {
          case null => false
          case _    => true
        }

    @test
    def testObjectNullMatch01(): Bool =
        import new java.lang.Object() as newObj;
        match? (newObj() as & Pure) {
          case null => false
          case _    => true
        }

    @test
    def testRecordNullMatch01(): Bool =
        match? {x = "hello", y = "world"} {
          case null => false
          case _    => true
        }

    @test
    def testSchemaNullMatch01(): Bool =
        match? #{A("Hello World").} {
          case null => false
          case _    => true
        }

    @test
    def testOptionNullMatch01(): Bool =
        match? None {
          case null => false
          case _    => true
        }

    @test
    def testOptionNullMatch02(): Bool =
        match? Some("Hello World") {
          case null => false
          case _    => true
        }

    @test
    def testResultNullMatch01(): Bool =
        match? Ok("Hello World") {
          case null => false
          case _    => true
        }

    @test
    def testResultNullMatch02(): Bool =
        match? Err("Hello World") {
          case null => false
          case _    => true
        }

    @test
    def testListNullMatch01(): Bool =
        match? Nil {
          case null => false
          case _    => true
        }

    @test
    def testListNullMatch02(): Bool =
        match? "Hello" :: "World" :: Nil {
          case null => false
          case _    => true
        }

    pub enum Option[t] {
        case None,
        case Some(t)
    }

    pub enum Result[t, e] {
        case Ok(t),
        case Err(e)
    }

    pub enum List[t] {
        case Nil,
        case Cons(t, List[t])
    }

}
