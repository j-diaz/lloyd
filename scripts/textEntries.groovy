
import groovy.sql.Sql

def url = 'jdbc:hsqldb:mem:yourDB'
def user = this.args[0]
def password = this.args[1]
def driver = 'org.hsqldb.jdbcDriver'
def sql = Sql.newInstance(url, user, password, driver)


def createSqlStatements(first, last) {
   def s = """
  INSERT INTO Tweet (firstname, lastname)
  VALUES (${first}, ${last}, 0, 0, TIMESTAMP ,0)
""", myKeyNames

}

def readSqlStatements(){
    List<String> stmts = new LinkedList<>()

    return stmts
}


createSqlStatements(obj1, obj2)
List<String> stmts = readSqlStatements()
performInserts(stmts)