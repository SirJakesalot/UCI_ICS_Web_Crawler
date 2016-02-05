BIN = bin

JAVA_SRC_FOLDER = src/ir/assignments/two

# Crawler .jar file and dependencies
CRAWLER_JAR = lib/crawler4j-4.1-jar-with-dependencies.jar

# Logger .jar file
LOGGER_JAR = lib/slf4j-simple-1.7.14.jar

# Frequency .jar file
FREQUENCY_JAR = lib/Frequency.jar

# Apache HTTP .jar file and dependencies
# APACHE_JAR = lib/httpcore-4.4.3.jar

# Path to .class files
CLASSPATH = .:$(BIN):$(CRAWLER_JAR):$(LOGGER_JAR):$(FREQUENCY_JAR)

# Source files to compile
JAVAC_SRC = $(JAVA_SRC_FOLDER)/MyCrawler.java \
			$(JAVA_SRC_FOLDER)/Controller.java \
			$(JAVA_SRC_FOLDER)/CrawlStat.java

default: compile

compile: clean
	@mkdir -p $(BIN)
	@javac -d $(BIN) -cp $(CLASSPATH) $(JAVAC_SRC)

# Running the tests
run: compile
	@java -cp $(CLASSPATH) ir.assignments.two.Controller

clean:
	@rm -rf $(BIN)

# Editing shortcuts
vim_c:
	@vim $(JAVA_SRC_FOLDER)/Controller.java

vim_m:
	@vim $(JAVA_SRC_FOLDER)/MyCrawler.java
