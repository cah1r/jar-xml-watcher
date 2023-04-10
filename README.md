## JAR & XML File Watcher

This is simple program from example recruiting test which creates three folders: `home`, `dev` and `test`. It is listening home folder and when `jar` or `xml` file appears then moves it to `dev` or `test` folder after some conditions are met:
- `.jar` file created on even minute number goes to `dev` folder
- `.jar` file created on odd minute number goes to `test` folder
- `.xml` file goes to `test` folder

### Building
To build app just run command in terminal:
```
./gradlew build
```
Then run app by running command:
```
java -jar jar-xml-watcher-0.0.1.jar
```